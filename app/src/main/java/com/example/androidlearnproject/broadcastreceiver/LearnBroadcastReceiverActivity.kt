package com.example.androidlearnproject.broadcastreceiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidlearnproject.databinding.ActivityLearnBroadcastReceiverBinding

class LearnBroadcastReceiverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearnBroadcastReceiverBinding

    private val dynamicBroadcastReceiver = MyDynamicBroadcastReceiver()

    // we can Order the broadCast Receiver and changed passed data between them.
    private val broadCastReceiver1 = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val resultCode = resultCode
            val resultData = resultData
            // passing true to make empty Bundle if it is null
            val extraData = getResultExtras(true)
            val extraString = extraData.getString("extra_string")

            val msg = "receiver 1 triggered with Result Code : $resultCode \n"
                .plus("Result Data : ${resultData ?: ""}\n")
                .plus("Extra Data : ${extraString ?: ""}")

            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

    private val broadCastReceiver2 = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val resultCode = resultCode
            val resultData = resultData
            // passing true to make empty Bundle if it is null
            val extraData = getResultExtras(true)
            val extraString = extraData.getString("extra_string")

            val msg = "receiver 2 triggered with Result Code : $resultCode \n"
                .plus("Result Data : ${resultData ?: ""}\n")
                .plus("Extra Data : ${extraString ?: ""}")
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

            //to change data that will be passed to the next broadcast receiver
            setResultCode(20) // it is recommended to use a number between -1000 to 1000
            setResultData("Data Changed From Receiver 2")
            extraData.putString("extra_string", "Run next receiver")
            setResultExtras(extraData)
//            setResult(20,"Data Changed From Receiver 2",extraData) another way to change the data passed to the next receiver
            //this method is used to stop all the following broadcasts that will run after this broadcast with same action
//            abortBroadcast()
        }
    }

    private val permissionBroadCastReceiver = PermissionBroadCastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnBroadcastReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnExplicitStaticBroadcast.setOnClickListener {
            val explicitStaticBroadCastIntent = Intent(this, MyExplicitStaticBroadcastReceiver::class.java)
            sendBroadcast(explicitStaticBroadCastIntent)
        }

        //this is how we can order dynamic broadcast receiver same can be done for static in manifest file with priority attribute
        val intentFilter1 = IntentFilter("com.example.androidlearnproject.MyBroadCastAction")
        intentFilter1.priority = 1 // higher priority number will be called first
        registerReceiver(broadCastReceiver1, intentFilter1)

        val intentFilter2 = IntentFilter("com.example.androidlearnproject.MyBroadCastAction")
        intentFilter2.priority = 2 // higher priority number will be called first
        registerReceiver(broadCastReceiver2, intentFilter2)

        binding.btnStartOrderedBroadcast.setOnClickListener {
            //it is recommended to use custom action name prefixed with app package name
            val intent = Intent("com.example.androidlearnproject.MyBroadCastAction")
            //this is how we can control the order of the broadcast receiver
            sendOrderedBroadcast(intent, null)
        }

        binding.btnStartPermissionBroadcast.setOnClickListener {
            val intent = Intent("com.example.androidlearnproject.PermissionBroadCastAction")
            // ## both sender and receiver must declare permission either in code or in the manifest file
            sendBroadcast(intent, Manifest.permission.INTERNET)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadCastReceiver1)
        unregisterReceiver(broadCastReceiver2)
    }

    /* ## we can still register broadcast receiver in onCreate and unRegister broadcast receiver in onDestroy if we want
    it to run while the app is in the background it depends on our use case */
    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(Intent.ACTION_POWER_CONNECTED)
        registerReceiver(dynamicBroadcastReceiver, intentFilter)


        val intentFilterPermission = IntentFilter("com.example.androidlearnproject.PermissionBroadCastAction")
        //we can use one of the already existing permission or create a custom permission in the manifest file and use it here
        // ## both sender and receiver must declare permission either in code or in the manifest file
        registerReceiver(permissionBroadCastReceiver, intentFilterPermission, Manifest.permission.INTERNET, null)
    }

    override fun onStop() {
        super.onStop()
        //best practise is to unregister dynamic broadcast in the end of lifecycle
        unregisterReceiver(dynamicBroadcastReceiver)
        unregisterReceiver(permissionBroadCastReceiver)
    }
}