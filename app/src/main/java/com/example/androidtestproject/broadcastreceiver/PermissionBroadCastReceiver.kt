package com.example.androidtestproject.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/* adding permission to broad cast receiver allow it to not be called by any one only the ones which is using the permission
that this broadcast receiver declared */
/* we can either declare broadcast receiver permission in the manifest file using permission attribute inside the receiver tag or
we can declare it in the registerReceiver method if we register receiver dynamically */
// ## both sender and receiver must declare permission either in code or in the manifest file
class PermissionBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "this is a broadcast receiver with permission", Toast.LENGTH_LONG).show()
    }
}