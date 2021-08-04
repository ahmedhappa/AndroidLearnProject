package com.example.androidtestproject.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

// ## (implicit : will not called directly instead it listen for action using intent Filter it can be triggered by system or other apps)

/*broadcast receiver which means it is registered in the manifest file with intent filter,
actions which listens to the system events like device reboot and local changes, you can listen to more than one event at the same
broadcast receiver class but it has some limitations to actions which isn't working on api 24+ and 26+ see list on internet */
// ## it may take sometimes to launch the static broadcast receiver from the system
class MyStaticBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.action?.also {
            if (it == Intent.ACTION_LOCALE_CHANGED) {
                Toast.makeText(context, "Toast From My Static Broadcast receiver Language Changed", Toast.LENGTH_LONG).show()
            }
        }
    }
}