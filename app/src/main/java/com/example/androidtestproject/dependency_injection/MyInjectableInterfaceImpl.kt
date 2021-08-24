package com.example.androidtestproject.dependency_injection

import androidx.fragment.app.FragmentActivity
import javax.inject.Inject

class MyInjectableInterfaceImpl @Inject constructor(
    /*
     AppNavigatorImpl depends on a FragmentActivity. Because an AppNavigator instance is provided in the Activity
      container , FragmentActivity is already available as a predefined binding.
     */
    private val activity: FragmentActivity
) : MyInjectableInterface {

    override fun showTestString(): String {
        return "Hello from sub class that implement injectable interface"
    }

}