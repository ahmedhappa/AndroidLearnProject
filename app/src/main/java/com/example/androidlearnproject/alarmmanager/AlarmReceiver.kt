package com.example.androidlearnproject.alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    private val TAG = javaClass.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.apply {
            //it is recommended to use action with receiver to ensure the broadcast is coming from your assigned PendingIntent
            if (action == "alarm_action") {
                Toast.makeText(
                    context,
                    "This is a task triggerd from alarm manager : ${getStringExtra("EXTRA_DATA")}",
                    Toast.LENGTH_LONG
                ).show()
                Log.d(TAG, "This is a task triggerd from alarm manager")
            }
        }
    }
}