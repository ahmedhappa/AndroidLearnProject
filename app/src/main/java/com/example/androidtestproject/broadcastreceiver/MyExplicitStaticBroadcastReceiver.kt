package com.example.androidtestproject.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import kotlinx.coroutines.*

// ## (explicit : will be called directly by class name inside or from outside the app depends on you define it as exported or not)

class MyExplicitStaticBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Toast From My explicit Static Broadcast Triggered", Toast.LENGTH_SHORT).show()

        /* to do a background work this is telling the system that broadcast receiver is doing a background work and needs time
            so don't kill the background thread work */
        // it has a limitation to 10 seconds after that the system may kill the receiver with background work
        val pendingResult = goAsync()
//        if we want to get data of this receiver using with pending result we should use it example pendingResult.resultData
        Toast.makeText(context, "start doing a heavy background work", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            doHeavyBackgroundWork()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "finished doing a heavy background work", Toast.LENGTH_SHORT).show()
                // we must finish it to till the system that the background work has finished and the system can now free the broadcast receiver
                pendingResult.finish()
            }
        }
    }

    private suspend fun doHeavyBackgroundWork() {
        delay(5000)
    }
}