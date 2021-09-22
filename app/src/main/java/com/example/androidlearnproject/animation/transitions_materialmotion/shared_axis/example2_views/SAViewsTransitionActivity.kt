package com.example.androidlearnproject.animation.transitions_materialmotion.shared_axis.example2_views

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.transition.TransitionManager
import com.example.androidlearnproject.databinding.ActivityCtviewsTransitionBinding
import com.example.androidlearnproject.databinding.ActivitySaviewsTransitionBinding
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis

class SAViewsTransitionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaviewsTransitionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaviewsTransitionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up a new MaterialSharedAxis in the specified axis and direction.
        val transitionBtnToImg = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = 500
        }

        val transitionImageToButton = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = 500
        }



        binding.btnFirstView.setOnClickListener {
            // Begin watching for changes in the View hierarchy.
            /* Begin the transition by changing properties on the start and end views or
               removing/adding them from the hierarchy. */
            TransitionManager.beginDelayedTransition(binding.root, transitionBtnToImg)
            binding.btnFirstView.visibility = View.GONE
            binding.ivSecondView.visibility = View.VISIBLE
        }
        binding.ivSecondView.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root, transitionImageToButton)
            binding.btnFirstView.visibility = View.VISIBLE
            binding.ivSecondView.visibility = View.GONE
        }
    }
}