package com.example.androidlearnproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.androidlearnproject.databinding.ActivityMyCoroutineBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyCoroutineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyCoroutineBinding
    private var textToInterAct = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCoroutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTestCoroutine.setOnClickListener {
            var temp = 1
            lifecycleScope.launch {
                Log.i("Scope inside Before", temp.toString())
                temp = ioCoroutine(temp)
                Log.i("Scope inside After", temp.toString())
            }
            Log.i("Scope OutSide After", temp.toString())
        }

        binding.btnEnterAct.setOnClickListener {
            textToInterAct += "t"
            binding.tvEnterAct.text = textToInterAct
        }
    }

    private suspend fun mainCoroutineWithNoBlocking(number: Int): Int {
        val result = number * 5
        //    delay method freeze coroutine for a specific time without blocking any thread even ui thread
        delay(3000)
        return result
    }

    private suspend fun mainCoroutineWithBlocking(number: Int): Int {
        val result = number + 1
//        this way will freeze any thread and block the ui thread if this function is called from main thread
//        Thread.sleep(3000)
        //equivalent way for Thread.sleep(3000) but this way is handling exception automatically
        SystemClock.sleep(3000)
        // freeze with another way
//        for (i in Integer.MIN_VALUE..Int.MAX_VALUE) {
//            val x = true
//        }
        return result
    }

    private suspend fun ioCoroutine(number: Int): Int {
        var result: Int
        Log.i("Coroutine", "Before Io Coroutine")
        //this will suspend the scope which is calling it but will not block the main ui
        withContext(Dispatchers.IO) {
            result = number - 10
            SystemClock.sleep(3000)
        }
        Log.i("Coroutine", "After Io Coroutine")
        return result
    }
}