package com.example.androidlearnproject.animation.transitions_materialmotion.container_transform.example1_activity

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.androidlearnproject.R
import com.google.android.material.transition.platform.MaterialElevationScale


class CTSecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        // Keep system bars (status bar, navigation bar) persistent throughout the transition.
        window.enterTransition = MaterialElevationScale(false).apply {
            //excludeTarget is used to exclude view from animiation transition if you have a custom action bar you can add it here too
            excludeTarget(R.id.action_bar_container, true)
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ct_second)

        title = "Second Activity"

    }
}