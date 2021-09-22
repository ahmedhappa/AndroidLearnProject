package com.example.androidlearnproject.animation.transitions_materialmotion.fade_through

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.example.androidlearnproject.R
import com.example.androidlearnproject.databinding.ActivityFaFirstBinding
import com.example.androidlearnproject.databinding.ActivitySaFirstBinding
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.google.android.material.transition.platform.MaterialSharedAxis


class FTFirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // Enable Activity Transitions.
        /* Optionally enable Activity transitions in your theme for all activities with
           <item name=”android:windowActivityTransitions”>true</item>. */
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

        window.exitTransition = MaterialFadeThrough().apply {
            excludeTarget(R.id.action_bar_container, true)
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
            duration = 300
        }

        super.onCreate(savedInstanceState)
        binding = ActivityFaFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "First Activity"

        binding.btnGotToSecondActivity.setOnClickListener {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
            Intent(this, FTSecondActivity::class.java).also {
                startActivity(it, options.toBundle())
            }
        }

    }
}