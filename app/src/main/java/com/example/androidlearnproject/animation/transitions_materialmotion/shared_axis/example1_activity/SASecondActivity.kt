package com.example.androidlearnproject.animation.transitions_materialmotion.shared_axis.example1_activity

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.androidlearnproject.R
import com.google.android.material.transition.platform.MaterialElevationScale
import com.google.android.material.transition.platform.MaterialSharedAxis


class SASecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

        window.enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            excludeTarget(R.id.action_bar_container, true)
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
            duration = 300
        }
        window.returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            excludeTarget(R.id.action_bar_container, true)
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
            duration = 300
        }

        /* optional Allow Activity A’s exit transition to play at the same time as this Activity’s
           enter transition instead of playing them sequentially. */
        window.allowEnterTransitionOverlap = true

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sa_second)

        title = "Second Activity"

    }
}