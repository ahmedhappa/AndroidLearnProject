package com.example.androidlearnproject.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import com.example.androidlearnproject.databinding.ActivityLearnAlarmManagerBinding

class LearnAlarmManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearnAlarmManagerBinding
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnAlarmManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            // ## Android does not save any alarms when the device is turned off. Therefore, all alarms will be missed when device is booted
            //AlarmManager is actually a system service and thus can be retrieved from Context.getSystemService()
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            //create pending intent to pass it to alarm manager so that it can run it after specified amount of time
            val receiverIntent = Intent(this@LearnAlarmManagerActivity, AlarmReceiver::class.java).apply {
                action = "alarm_action"
                putExtra("EXTRA_DATA", "this is extra data with intent")
            }
            val pendingReceiverIntent = PendingIntent.getBroadcast(
                this@LearnAlarmManagerActivity,
                0,
                receiverIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            btnRunAlarmManager.setOnClickListener {
                // alarm will fire up after 10 seconds from the current system time
                /* minmum amount of delay time is 5 second in the future so if you make it 2 seconds the android system will force
                it to 5 seconds */
                val alarmDelayTime = System.currentTimeMillis() + (10 * 1_000L)
                //AlarmManager provides two ways to listen to an alarm broadcast. They are
                //1 - BroadcastReceiver specified at the Intent wrapped inside a PendingIntent
                /* if it is launched more than one time before intent is executed the time will
                   be updated with the new one without launching the old intent */
                /*there are four different functions for setting an alarm:set(),setExact(),setAndAllowWhileIdle() and setExactAndAllowWhileIdle()
                According to their names, 1-set() will let Android interrupt the scheduled time and 2-setExact()
                will ask Android to fire exactly at what the requested time is 3-setAndAllowWhileIdle()
                and setExactAndAllowWhileIdle() make exactly the same thing set and setExact do but it also launch when the app
                is standby(idle) mode or device in the doze mode.*/
                /* AlarmManager accepts two types of time to fire an alarm:Real Time Clock (RTC),Elapsed Real Time
                   Real Time Clock is the absolute time since January 1, 1970 UTC and Elapsed Real Time is the relative time
                   since the device is booted.
                   By default, AlarmManager fires an alarm only when device is not sleeping.
                   The types are RTC and ELAPSED_REALTIME. In order to have an alarm goes off during device is sleeping,
                   the type of alarm must be set to either RTC_WAKE_UP or ELAPSED_REALTIME_WAKEUP.*/
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmDelayTime, pendingReceiverIntent)
                //2 - Local listener (AlarmManager.OnAlarmListener)
                /* There is a limitation on AlarmManager.OnAlarmListener over PendingIntent. It cannot work when the
                corresponding Activity or Fragment is destroyed since the callback object is released at the same time */
                /* if it is launched more than one time it creates a queue of tasks and execute it in the given time of each
                  task */
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    alarmManager.setExact(
//                        AlarmManager.RTC_WAKEUP,
//                        alarmDelayTime,
//                        "alarmTag",
//                        onAlarmListener,
//                        null/*passing null so that is can run on ui thread*/
//                    )
//                }
                // 3 - set repeating alarm
                /* minimum amount of interval time is 60 seconds so if you make it like 50 seconds the android system will force
                it to 60 seconds */
//                val alarmInterval = 60 * 1_000L
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmDelayTime, alarmInterval, pendingReceiverIntent)

                //4 - set with window
                // Let Android defer alarm but must fire within the given period of time
                alarmManager.setWindow(AlarmManager.RTC_WAKEUP, alarmDelayTime, 10_000L, pendingReceiverIntent)
            }

            //cancel alarm
            btnCancelAlarmManager.setOnClickListener {
                //cancel with pending intent
                alarmManager.cancel(pendingReceiverIntent)

                //cancel with listener
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    alarmManager.cancel(onAlarmListener)
//                }
            }

            btnStartCountDownTimer.setOnClickListener {
                countDownTimer.start()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

    private val onAlarmListener = AlarmManager.OnAlarmListener {
        doSomeWork()
    }

    private val countDownTime = 3000L //3 seconds
    private val countTick = 1000L //1 second each tick
    private val countDownTimer = object : CountDownTimer(countDownTime, countTick) {
        override fun onTick(millisUntilFinished: Long) {
            Log.d(TAG, "Count Down Timer : this is a tick.remaining $millisUntilFinished until finished")
        }

        override fun onFinish() {
            Log.d(TAG, "Count Down Timer : count down finished")
        }

    }

    private fun doSomeWork() {
        Toast.makeText(
            this,
            "This is a task triggered from alarm manager",
            Toast.LENGTH_LONG
        ).show()
        Log.d(TAG, "This is a task triggered from alarm manager")
    }
}