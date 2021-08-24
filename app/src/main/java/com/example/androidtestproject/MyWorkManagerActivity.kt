package com.example.androidtestproject

import android.app.Notification
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.androidtestproject.MyWorkManagerActivity.Companion.passedDataKey
import com.example.androidtestproject.databinding.ActivityMyWorkManagerBinding
import java.util.concurrent.TimeUnit

class MyWorkManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyWorkManagerBinding
    private val myWorkTag = "cleanup"

    companion object {
        const val passedDataKey = "myData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyWorkManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Work is defined in WorkManager via a WorkRequest
        //work manager instance take application context
        val workManager = WorkManager.getInstance(applicationContext)

        //The WorkRequest object contains all of the information needed by WorkManager to schedule and run your work
        /*
        * WorkRequest itself is an abstract base class. There are two derived implementations of this class that you can use to create the request,
        * OneTimeWorkRequest and PeriodicWorkRequest. As their names imply, OneTimeWorkRequest is useful for scheduling non-repeating work,
        * whilst PeriodicWorkRequest is more appropriate for scheduling work that repeats on some interval.
        * */
        //this is the simple default way
        val mySimpleOneTimeWorkRequest = OneTimeWorkRequest.from(MyFirstWork::class.java)
//        val mySimpleOneTimeWorkRequest = OneTimeWorkRequestBuilder<MyFirstWork>().build()

        /*
        * You can apply constraints to work. For example,
        * you could add a constraint to your work request such that the work only runs when the user’s device is charging
        * */
        val myConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED) //	Constrains the type of network required for your work to run. For example, Wi-Fi (UNMETERED).
            .setRequiresCharging(true) //When set to true, your work will only run when the device is charging.
            .setRequiresBatteryNotLow(true) //When set to true, your work will not run if the device is in low battery mode.
            .build()

        //advanced way to create work request for Additional configuration
        val myOneTimeWorkRequest = OneTimeWorkRequestBuilder<MyFirstWork>()
            .setConstraints(myConstraints)
            //specify your work to start after a minimum initial delay.
//            .setInitialDelay(
//                1,
//                TimeUnit.MINUTES
//            )
            /*If you require that WorkManager retry your work, you can return Result.retry() from your worker.
        Your work is then rescheduled according to a backoff delay and backoff policy.

            *Backoff delay specifies the minimum amount of time to wait before retrying your work after the first attempt.
        This value can be no less than 10 seconds (or MIN_BACKOFF_MILLIS).

            *Backoff policy defines how the backoff delay should increase over time for subsequent retry attempts.
        WorkManager supports 2 backoff policies, LINEAR and EXPONENTIAL.

        Every work request has a backoff policy and backoff delay. The default policy is EXPONENTIAL with a delay of 10 seconds,
        but you can override this in your work request configuration.
        */
            /*
            the minimum backoff delay is set to the minimum allowed value, 10 seconds.
            Since the policy is LINEAR the retry interval will increase by approximately 10 seconds with each new attempt.
            For instance, the first run finishing with Result.retry() will be attempted again after 10 seconds,
            followed by 20, 30, 40, and so on, if the work continues to return Result.retry() after subsequent attempts.
            If the backoff policy were set to EXPONENTIAL, the retry duration sequence would be closer to 20, 40, 80, and so on.
            * */
            .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
            /*
            If you have a group of logically related work, you may also find it helpful to tag those work items.
            Tagging allows you to operate with a group of work requests together.

            For example, WorkManager.cancelAllWorkByTag(String) cancels all Work Requests with a particular tag, and WorkManager.
            getWorkInfosByTag(String) returns a list of the WorkInfo objects which can be used to determine the current work state.
            */
            .addTag(myWorkTag)
            //this function is used to pass data to work as map
            .setInputData(workDataOf(passedDataKey to "this is my passed data to work"))
            .build()

        binding.btnOneTimeRequest.setOnClickListener {
            //simple way to start a work
            //if the task request finished and you enqueue it again it won't work you need to define a new object request for the task again
//            workManager.enqueue(mySimpleOneTimeWorkRequest)
//            workManager.enqueue(myOneTimeWorkRequest)

            //to enqueue work so that the same work doesn't executed twice
            //Now, if the code runs while a MyFirstWork is already in the queue, the existing job is kept and no new job is added.
            //if the task request finished you can enqueue it again without creating another object from task request
            workManager.enqueueUniqueWork(
                "myWorkName", ExistingWorkPolicy.KEEP, mySimpleOneTimeWorkRequest
            )
        }

        //Schedule periodic work
        //## The minimum repeat interval that can be defined is 15 minutes
        val myPeriodicWorkRequest =
            PeriodicWorkRequestBuilder<MyFirstWork>(15, TimeUnit.MINUTES).build()
        binding.btnPeriodicWork.setOnClickListener {
            workManager.enqueue(myPeriodicWorkRequest)
        }


        //Observing work
        // you can also observe task by it's name or tag.
        workManager.getWorkInfoByIdLiveData(myOneTimeWorkRequest.id).observe(this, { workInfo ->
            if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                Log.e("WorkManager", "Work is done with success")
            } else {
                Log.e("WorkManager", "Work is still in progress")
            }
        })

        //Cancelling and stopping work
        binding.btnCancelSpecificWork.setOnClickListener {
            // you can also cancel work by it's name or tag
            workManager.cancelWorkById(myPeriodicWorkRequest.id)
            //cancel all tasks
//            workManager.cancelAllWork()
        }

        //chain work excute
        /*WorkManager allows you to create and enqueue a chain of work that specifies multiple dependent
         tasks and defines what order they should run in*/
        val mySecondOneTimeRequest = OneTimeWorkRequestBuilder<MySecondWork>().build()
        binding.btnChainWork.setOnClickListener {
            workManager.beginWith(myOneTimeWorkRequest)
                .then(mySecondOneTimeRequest)
                .enqueue()
        }

        val foregroundWorkManager = OneTimeWorkRequest.from(ForegroundWorkManager::class.java)
        binding.btnStartForegroundWorkManager.setOnClickListener {
            workManager.enqueueUniqueWork(
                "foregroundWorkManager", ExistingWorkPolicy.KEEP, foregroundWorkManager
            )
        }
    }
}

/* This class is where all the background work happen
* just extend the Worker class and override the doWork() method and place the code there
 */

/* if the application is killed or cleared from recent tasks before work class finishes it's work it will
launch again
*/
class MyFirstWork(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {
    /* every time a new object of this class is created that is why innerData doesn't change when enqueue the
     same class more then one time */
    private var innerData: String = "initial value"

    //this is where the WorkManager starts the work
    /* you can return 3 results object to that method :
    * Result.success() ->  The work finished successfully.
    * Result.failure() -> The work failed.
    * Result.retry() -> The work failed and should be tried at another time according to its retry policy.
      */
    override fun doWork(): Result {
        return try {
            val data = inputData.getString(passedDataKey) ?: "No Data Passed"
            // you can do some work here in the background
            Log.e("WorkManager", "Doing some tasks")
            Log.e("WorkManager", "passed data : $data")
            for (i in 1..10) {
                Log.e("WorkManager", "Doing Work $i")
                SystemClock.sleep(1_000)
            }
            Log.e("WorkManager", "Inner data $innerData")
            innerData = "inner data changed"
            Log.e("WorkManager", "Task executed successfully")
//            Result.success()
            //can return data
            Result.success(workDataOf("returendData" to " this is a data to return"))
        } catch (e: Exception) {
            Log.e("WorkManager", "Task Failed")
            Result.failure()
            // if you want to retry task,return Result.retry()
//            Result.retry()
        }
    }

    override fun onStopped() {
        super.onStopped()
        //WorkManager ignores the Result set by a Worker that has received the onStop signal
        //Worker has been stopped. close any resources you may be holding onto.
        Log.e("WorkManager", "Work Stopped")
    }

}

class MySecondWork(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {

    override fun doWork(): Result {
        return try {
            //talking result form the previous work
            val data = inputData.getString("returendData") ?: ""
            Log.e("WorkManager", "Second task done $data")
            Result.success()
        } catch (e: Exception) {
            Log.e("WorkManager", "Second Failed")
            Result.failure()
        }
    }
}

//this foreground work manager is used to run immediate long running operations
class ForegroundWorkManager(private val appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {
    override fun doWork(): Result {
        return try {
            /*
             Call setForegroundAsync() before your long running task is a must.
             Otherwise your worker will be treated as non-foreground service until setForegroundAsync() has been called,
             which might result in unwanted results such as work being cancelled.
             */
            setForegroundAsync(getForegroundInfo())
            Log.e("Foreground WorkManager", "Started")
            SystemClock.sleep(5000)
            Log.e("Foreground WorkManager", "Finished Successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("Foreground WorkManager", "Failed")
            Result.failure()
        }
    }

    private fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(1, createForegroundNotification())
    }

    private fun createForegroundNotification(): Notification {
        return NotificationCompat.Builder(appContext, AndroidTestProjectApp.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_like)
            .setContentTitle("Foreground WorkManager")
            .setContentText("running a long task")
            .setOngoing(true)
            .build()
    }
}