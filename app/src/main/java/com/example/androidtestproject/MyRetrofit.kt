package com.example.androidtestproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_my_retrofit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MyRetrofit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_retrofit)
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://e89385ae-5f61-4249-b3de-7c84d99d58c2.mock.pstmn.io/apis/")
            .build()
        val retrofitService = retrofit.create(MyRetroApi::class.java)
        btn_call_api.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val myTime = System.currentTimeMillis()
                    // using async instead of usual 2 api call makes it faster because async makes 2 apis run at the same time while the usual one makes 1 api run then after it finishes the second api runs and so on
                    val firstApiName = async {retrofitService.getApi1()}
//                    val firstApiName = retrofitService.getApi1()
                    Log.e(MainActivity::class.qualifiedName,"first current Time =  ${System.currentTimeMillis() - myTime}")
//                    Log.e(MainActivity::class.qualifiedName,"first name =  ${firstApiName.await().name}")
//                    Log.e(MainActivity::class.qualifiedName,"first name =  ${firstApiName.name}")
                    val secondApiName = async {retrofitService.getApi2()}
//                    val secondApiName =retrofitService.getApi2()
                    Log.e(MainActivity::class.qualifiedName,"second current Time =  ${System.currentTimeMillis() - myTime}")
//                    Log.e(MainActivity::class.qualifiedName,"second name =  ${secondApiName.await().name}")
//                    Log.e(MainActivity::class.qualifiedName,"second name =  ${secondApiName.name}")
                    Toast.makeText(
                        this@MyRetrofit,
                        "${firstApiName.await().name} ${secondApiName.await().name}",
//                        "${firstApiName.name} ${secondApiName.name}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    data class Api1Response(val name: String, val age: Int)

    data class Api2Response(val name: String, val age: Int, val gender: String)

    interface MyRetroApi {
        @GET("first_api")
        suspend fun getApi1(): Api1Response

        @GET("second_api")
        suspend fun getApi2(): Api2Response
    }
}