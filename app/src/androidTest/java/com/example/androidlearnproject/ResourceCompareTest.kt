package com.example.androidlearnproject

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ResourceCompareTest {

    /* you shouldn't declare the object globally because each function can manipulate it and will affect the other one instead.
     each fun should declare it's own object*/
    private lateinit var resourceCompare: MyJunitTest.ResourceCompare

    //before annotation makes this function run or work before every test function
    //that will lead to the resourceCompare would be declared before every test function
    @Before
    fun setUp(){
        resourceCompare = MyJunitTest.ResourceCompare()
    }

    @Test
    fun stringResourceIsLikeGivenString_isTrue() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = resourceCompare.compareStringResource(context, R.string.my_name, "ahmed")
        assertThat(result).isTrue()
    }

    @Test
    fun stringResourceIsDifferentFromGivenString_isFalse() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = resourceCompare.compareStringResource(context, R.string.my_name, "ali")
        assertThat(result).isFalse()
    }
}