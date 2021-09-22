package com.example.androidlearnproject.animation.transitions_materialmotion.fade_through

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.androidlearnproject.R
import com.google.android.material.transition.platform.MaterialElevationScale
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.google.android.material.transition.platform.MaterialSharedAxis


class FTSecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

        window.enterTransition = MaterialFadeThrough().apply {
            excludeTarget(R.id.action_bar_container, true)
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
            duration = 300
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fa_second)

        title = "Second Activity"

    }
}