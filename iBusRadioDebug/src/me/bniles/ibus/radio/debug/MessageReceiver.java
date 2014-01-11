/* This file is part of iBusRadio.

    iBusRadio is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    iBusRadio is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with iBusRadio.  If not, see <http://www.gnu.org/licenses/>.
    
*/

package me.bniles.ibus.radio.debug;

import me.bniles.ibus.radio.debug.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;

public class MessageReceiver extends BroadcastReceiver {
	
	private static final String TAG = MessageReceiver.class.getSimpleName();
	private String tempstring;
	private String tempCopyString;
	private String hexString;
	// private String messageType;
	private byte[] messageData;
	private byte[] tempbytes;
	
	private Activity act;
	private TextView stationTextView;
	private Switch enableSwitch;
	
	public MessageReceiver(Activity act) {
		this.act = act;
	}
	
	public void onReceive(Context context, Intent intent) {
		// messageType = intent.getStringExtra("MessageType");
		messageData = intent.getByteArrayExtra("MessageData");
		int lastindex;
		
		enableSwitch = (Switch) act.findViewById(R.id.switch1);

		// if (messageType.equals("stationText")) {
		if (enableSwitch.isChecked() && messageData[0] == 0x68) {
			lastindex = messageData.length - 1;
			while (messageData[lastindex] == 0x20) {
				lastindex--;
			}
			tempbytes = new byte[lastindex];
			for (int i = 0; i < tempbytes.length; i++) {
				tempbytes[i] = messageData[i];
			}

			try {
				tempstring = new String(tempbytes, "UTF-8");
			} catch (Exception e) {
				Log.e(TAG, "error encoding intent extra");
			}
			stationTextView = (TextView) act.findViewById(R.id.textView1);
			tempCopyString = (String) stationTextView.getText();
			hexString = bytesToHex(tempbytes);
			tempstring = tempCopyString + '\n' + hexString + '\n' + tempstring;
			act.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					stationTextView.setText(tempstring);
				}
			});
			// Toast.makeText(context, "Broadcast Intent processed by receiver." + tempstring, Toast.LENGTH_LONG).show();
			// abortBroadcast();
		}
		abortBroadcast();
		// Log.i(TAG, "" + t.getTimeInMillis() + ": sending response broadcast intent");
		// Intent outintent = new Intent();
		// intent.setAction("com.ebookfrenzy.AnswerBroadcast");
		// Log.i(TAG, "HelloIOIOService: Sending intent.");
		// context.sendBroadcast(outintent);
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 3];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 3] = hexArray[v >>> 4];
	        hexChars[j * 3 + 1] = hexArray[v & 0x0F];
	        hexChars[j * 3 + 2] = ' ';
	    }
	    return new String(hexChars);
	}

}
