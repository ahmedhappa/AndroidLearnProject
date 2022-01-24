package com.example.androidlearnproject

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.androidlearnproject.databinding.ActivityLearnExecutorsBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

//Executor is An object that executes submitted Runnable tasks which has a queue that stores tasks that can't run at the moment
// ## it is better and recommended to use coroutine over executors
class LearnExecutors : AppCompatActivity() {
    private lateinit var binding: ActivityLearnExecutorsBinding
    private val TAG = javaClass.simpleName

    private lateinit var backgroundExecutor: ScheduledExecutorService
    private lateinit var backgroundExecutorsWithThreads: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnExecutorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create an executor that executes tasks in the main thread.
        val mainExecutor = ContextCompat.getMainExecutor(this)

        binding.btnRunUiExecutor.setOnClickListener {
            // Execute a task in the main thread
            mainExecutor.execute {
                binding.tvDummy.text = "Executor Run On Main Thread"
            }
        }

        // Create an executor that executes tasks in a background thread. with only 1 thread that execute one task at a time
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()
        binding.btnRunBackgroundExecutor.setOnClickListener {
            // Execute a task in the background thread.
            backgroundExecutor.execute {
                Log.e(TAG, "Code Runs On Background Thread")
                Thread.sleep(1000)
            }
            //this task will run after 1 second because theres is only one thread in this executor and the last task took 1 second to finish
            backgroundExecutor.execute {
                Log.e(TAG, "Another Code Runs on background thread")
            }
            // Execute a task in the background thread after 3 seconds.
            backgroundExecutor.schedule({
                Log.e(TAG, "Code Runs On Background Thread After 3 seconds")
                //run code on ui thread from backgroundExecutor using mainExecutor
                mainExecutor.execute {
                    binding.tvDummy.text = "Update Ui From Background Thread"
                }
            }, 3, TimeUnit.SECONDS)

        }

        // Create an executor that executes tasks in a background thread. with more than one thread ->3 in this example
        backgroundExecutorsWithThreads = Executors.newFixedThreadPool(3)
        binding.btnRunBackgroundExecutorWithThreads.setOnClickListener {
            // Execute a task in the background thread.
            backgroundExecutorsWithThreads.execute {
                Log.e(TAG, "Code Runs On Background Thread")
                Thread.sleep(3000)
            }
            //this task will run immediately because this executor has 3 threads so it can run 3 tasks at the same time
            backgroundExecutorsWithThreads.execute {
                Log.e(TAG, "Another Code Runs on background thread")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //Remember to shut down the executor after using.
        //this will prevent executor from accepting more tasks but will execute the tasks it have even the tasks in the queue
        backgroundExecutor.shutdown()
        //this will prevent executor from accepting more tasks and will try to stop running tasks and stop executing tasks in queue
        backgroundExecutorsWithThreads.shutdownNow()
    }
}