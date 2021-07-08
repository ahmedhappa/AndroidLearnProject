package com.example.androidtestproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.androidtestproject.databinding.ActivityMyCoroutineBinding
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
                temp = ioCoroutine(temp)
                Log.i("Coroutine Test InSide", temp.toString())
            }
            Log.i("Coroutine Test OutSide", temp.toString())
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
        Thread.sleep(3000)
        // freeze with another way
//        for (i in Integer.MIN_VALUE..Int.MAX_VALUE) {
//            val x = true
//        }
        return result
    }

    private suspend fun ioCoroutine(number: Int): Int {
        var result = 0
        Log.i("Coroutine", "Before Io Coroutine")
        withContext(Dispatchers.IO) {
            result = number - 10
            Thread.sleep(3000)
        }
        Log.i("Coroutine", "After Io Coroutine")
        return result
    }
}