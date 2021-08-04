package com.example.androidtestproject.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import kotlinx.coroutines.*

// ## (implicit : will not called directly instead it listen for action using intent Filter it can be triggered by system or other apps)

/*broadcast receiver which means it doesn't registered in the app manifest file instead it gets
called from the activity context or the application context depending on your use case , also means it will not be
Triggered if the application is destroyed unlike the static broadcast receiver but the benefit from it is that it doesn't
have the limitation that the static broadcast receiver has it almost listen to all event registered to it*/

class MyDynamicBroadcastReceiver : BroadcastReceiver() {
    // ## any broadcast receiver runs on the ui thread
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.action?.also {
            //this action will not work for static broadcast receiver
            if (it == Intent.ACTION_POWER_CONNECTED) {
                Toast.makeText(context, "Toast From My Dynamic Broadcast receiver Power Connected", Toast.LENGTH_LONG).show()
            }
        }
    }
}