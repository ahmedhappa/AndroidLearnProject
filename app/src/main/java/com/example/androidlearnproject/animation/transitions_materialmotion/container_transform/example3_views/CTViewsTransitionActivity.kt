package com.example.androidlearnproject.animation.transitions_materialmotion.container_transform.example3_views

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.transition.TransitionManager
import com.example.androidlearnproject.databinding.ActivityCtviewsTransitionBinding
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform

class CTViewsTransitionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCtviewsTransitionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCtviewsTransitionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transitionBtnToImg = MaterialContainerTransform().apply {
            // Manually tell the container transform which Views to transform between.
            startView = binding.btnFirstView
            endView = binding.ivSecondView
            // Ensure the container transform only runs on a single target (endView)
            addTarget(binding.ivSecondView)
            // Optionally add a curved path to the transform
            setPathMotion(MaterialArcMotion())
            /* Since View to View transforms often are not transforming into full screens,
               remove the transition's scrim. */
            scrimColor = Color.TRANSPARENT
            duration = 500
        }

        val transitionImageToButton = MaterialContainerTransform().apply {
            startView = binding.ivSecondView
            endView = binding.btnFirstView

            addTarget(binding.btnFirstView)
            scrimColor = Color.TRANSPARENT
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