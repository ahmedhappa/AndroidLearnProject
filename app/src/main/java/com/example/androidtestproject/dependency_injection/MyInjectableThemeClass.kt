package com.example.androidtestproject.dependency_injection

import dagger.hilt.android.scopes.ActivityScoped
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

//To make this class injectable by other classes we use @inject keyword on constructor
/* The annotation that scopes an instance to the application container is @Singleton.
 This annotation will make the application container always provide the same instance */
//@Singleton
// this scope will inject the object to the activity lifecycle only.
@ActivityScoped
// for more scopes https://developer.android.com/training/dependency-injection/hilt-android#component-scopes
class MyInjectableThemeClass @Inject constructor(val date: Date) {
    var backgroundColorName: String = "Red"
    val textColorName: String = "White"
}
