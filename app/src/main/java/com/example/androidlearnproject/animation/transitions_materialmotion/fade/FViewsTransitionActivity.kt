package com.example.androidlearnproject.animation.transitions_materialmotion.fade

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.transition.TransitionManager
import com.example.androidlearnproject.databinding.ActivityFviewsTransitionBinding
import com.google.android.material.transition.MaterialFade

class FViewsTransitionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFviewsTransitionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFviewsTransitionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up a new MaterialSharedAxis in the specified axis and direction.
        val transition = MaterialFade().apply {
            duration = 500
        }

        binding.btnFirstView.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root, transition)
            if (binding.ivSecondView.isVisible)
                binding.ivSecondView.visibility = View.GONE
            else
                binding.ivSecondView.visibility = View.VISIBLE
        }
    }
}