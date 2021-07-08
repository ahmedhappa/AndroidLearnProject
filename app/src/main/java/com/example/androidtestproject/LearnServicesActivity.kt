package com.example.androidtestproject

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import com.example.androidtestproject.databinding.ActivityLearnServicesBinding
import com.google.android.exoplayer2.util.NotificationUtil.createNotificationChannel
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class LearnServicesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearnServicesBinding
    private val myIntentServiceJobId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //======= job intent service =====================
        // yoc can also use WorkManager to do the same thing with JobIntentService.
        binding.btnStartIntentService.setOnClickListener {
            JobIntentService.enqueueWork(
                this,
                MyIntentService::class.java,
                /* this id must be unique for all services with the same class
                 for example if you give id 1 then you enqueued same intent service class with id 2 app will crash */
                myIntentServiceJobId,
                Intent(this, MyIntentService::class.java).apply {
                    putExtra("EXTRA_INTENT_STRING", binding.etIntentServiceData.text.toString())
                }
            )
        }
        //====== background service =====================

        /*
        for a background thread that running always in the background it is better to use WorkManager or JobService
        as from my research.
        after android oreo the system will shutdown any service that run in the background after the app is killed
        and in some situations during the running of the app so the WorkManager or JobService schedule the service and run
        it in some conditions and can rerun it again and again and again.
        */

        //====== foreground service =====================
        binding.btnStartForegroundService.setOnClickListener {
            val serviceIntent = Intent(this, MyForegroundService::class.java).apply {
                putExtra("EXTRA_DATA", binding.etForegroundServiceData.text.toString())
            }
            //for android oreo there is special cases
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                /* using this method allow you to start foreground service in the foreground or background status
                but if this service doesn't contains the startForeground() fun that makes it start in the foreground
                then the system will treat it as a background service and will shutdown it immediately after 5 seconds
                 */
                startForegroundService(serviceIntent)
            } else {
                //normal calls for services below oreo but if you tried to use this method in the background the application will crash
                startService(serviceIntent)
            }
        }

        binding.btnStopForegroundService.setOnClickListener {
            stopService(Intent(this, MyForegroundService::class.java))
        }
    }
}


//this service starts in a background thread and close it self after finishing code in onHandleWork()
//## you can't force stop JobIntentService , it will be stopped automatically when the work in onHandleWork() is done
/*if the application is killed or cleared from recent tasks before intent service finishes it's work it will
launch again from the last intent it was working on.
for example if you enqueued this intentService 3 times and it finished the first intent (first enqueue) and  the app killed
or removed from recent takes this intentService will launch again but from the queue number 2 with the intent number 2 passed to it
* */
//JobIntentService is a background non-binding service which runs in a separate thread from the ui thread
/*if we enqueued the same intent service multiple time and this intent service is still in work it will not
   recreated instead it will just call the onHandleWork() method with the new intent given in a stack of intents
   which means any new incoming intent will wait until all the preceding intents executed then the passed intent will
   be executed*/
class MyIntentService : JobIntentService() {

    private val mainThreadCoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        Log.d("Intent Service", "Service Created")
    }

    //this function will be called with every new intent
    override fun onHandleWork(intent: Intent) {
        Log.d("Intent Service", "New Intent Data From Launched = ${intent.getStringExtra("EXTRA_INTENT_STRING")}")
        printLogMessage()
    }

    private fun printLogMessage() {
        for (i in 1..10) {
            try {
                Log.d("Intent Service", "Code From inside Service $i")
                if (i == 5) {
                    toastNumber5()
                }
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    private fun toastNumber5() {
        mainThreadCoroutineScope.launch {
            Toast.makeText(this@MyIntentService, "Number 5", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Intent Service", "Service Destroyed")
        mainThreadCoroutineScope.cancel()
    }
}


/* this is a foreground service that from it's name has a ui (not like activity) ex (notifications,music player app,...)
which runs even if the app is killed or removed from recent tasks this forground service will contuin to function and
will be stopped or killed manually only */
//this foreground service runs in the app main ui thread so for heavy work you need to create another thread
//this foreground service only created once and for each call with this service using startService() the onStartCommand() will be triggered
class MyForegroundService : Service() {
    private val notificationChannelId = "channel_id"
    private val notificationChannelName = "channel_name"
    private val notificationId = 1
    private val ioCoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {
        super.onCreate()
        Log.d("Foreground Service", "Service Created")
        createNotificationChannel()
    }

    /* this method get called when more than one component get attached to this service example(activity1,activity2,broadCastReceiver)
    using bindService() fun */
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val extraData = intent?.getStringExtra("EXTRA_DATA") ?: "No Data"

        val myNotification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Notification Title")
            .setContentText(extraData)
            .setSmallIcon(R.drawable.ic_add)
            .setContentIntent(getPendingIntent())
            .build()
        //this notification id must be more than one
        startForeground(notificationId, myNotification)

// heavy work must be done in another thread because this service run in the ui thread
        ioCoroutineScope.launch {
            doHeavyWork()
        }

        // this is another way to stop this service manually the other way is to stop it using stopService() from other components
//        stopSelf()

        /* here you have 3 options to return to deal with when android system kills the service
        * 1- START_NOT_STICKY : service it won't run again
        * 2- START_STICKY : service will be recreated again and passing a null intent to it
        * 3- START_REDELIVER_INTENT : service will be recreated and passing the last intent passed to it
        * */
        return START_REDELIVER_INTENT
    }

    private suspend fun doHeavyWork() {
        for (i in 1..10) {
            Log.d("Foreground Service", "Number $i")
            freezeThread()
        }
    }

    private fun freezeThread() {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, LearnServicesActivity::class.java)
        return TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                notificationChannelName,
                //least importance requied is IMPORTANCE_LOW
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Foreground Service", "Service Destroyed")
    }

}