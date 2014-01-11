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
import android.content.IntentFilter;
import android.os.Bundle;

public class IBusRadioDebug extends Activity {
	
	private BroadcastReceiver messageReceiver;
	private boolean isReceiverRegistered;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        isReceiverRegistered = false;
        
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	final IntentFilter theFilter = new IntentFilter();
    	theFilter.addAction("me.bniles.ibus.inputMessageBroadcast");
    	theFilter.setPriority(99);
    	messageReceiver = new MessageReceiver(this);
    	/* messageReceiver = new BroadcastReceiver() {

    		@Override
    		public void onReceive(Context context, Intent intent) {
    			Toast.makeText(context, "Broadcast Intent processed by receiver.", Toast.LENGTH_LONG).show();
    			abortBroadcast();
    		}
    	}; */

    	if (! isReceiverRegistered) {
    		registerReceiver(messageReceiver, theFilter);
    		isReceiverRegistered = true;
    	}
    }

    @Override
    protected void onPause() {
    	super.onPause();
    	if (isReceiverRegistered) {
    		try {
    			unregisterReceiver(messageReceiver);
    		} catch (IllegalArgumentException e) {
    			// Do nothing.
    		}
    		isReceiverRegistered = false;
    	}
    }

    // Use this broadcastOutput to send a literal byte stream. Set messageType to 0x00.
    /* private void broadcastOutput(byte[] outputMessage) {
    	Intent intent = new Intent();
		intent.setAction("me.bniles.ibus.addOutputMessage");
		intent.putExtra("iBusMessageType", 255);
		intent.putExtra("iBusOutputMessage", outputMessage);
		sendOrderedBroadcast(intent, null);
    } */
    
}