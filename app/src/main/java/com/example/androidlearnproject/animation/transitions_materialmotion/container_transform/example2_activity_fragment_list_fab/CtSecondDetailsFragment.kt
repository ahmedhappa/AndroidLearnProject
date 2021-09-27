package com.example.androidlearnproject.animation.transitions_materialmotion.container_transform.example2_activity_fragment_list_fab

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use
import androidx.fragment.app.Fragment
import com.example.androidlearnproject.R
import com.example.androidlearnproject.databinding.FragmentCtSecondDetailsBinding
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform

class CtSecondDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCtSecondDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*sharedElementEnterTransition is being set, as opposed to the sharedElementReturnTransition. By default,
        the Android Transition system will automatically reverse the enter transition when navigating back,
        if no return transition is set. */
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            /* drawingViewId controls where in the view hierarchy the animating container will be placed.
            This allows you to show the transition below or above other elements in your UI. In Reply's case,
            you're running the container transform at the same level as your fragment container to ensure it's drawn below
            example (the Bottom App Bar or Floating Action Button). */
            drawingViewId = R.id.frame_fragment_container
            duration = 500
            /* controls the color of a translucent shade drawn behind the animating container.
            By default, this is set to 32% black. Here it's set to transparent, meaning no scrim will be drawn */
            //this is set to transparent because is is better for the back transition
            scrimColor = Color.TRANSPARENT
            /* ~~ When MaterialContainerTransform animates between two views, there are three "containers"
            it draws to the canvas: 1) a background container 2) a container for the start view and 3) a
            container for the end view. All three of these containers can be given a fill color and are set to
            transparent by default. Setting these background fill colors can be useful if your start or end view doesn't
            itself draw a background, causing other elements to be seen beneath it during animation. Since all of the containers
            in this example should be set to colorSurface, we can use the setAllContainerColors helper method to ensure
            we don't run into any visual issues.*/
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCtSecondDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.apply {
            binding.tvName.text = getString("extraData1")
            binding.tvDescription.text = getString("extraData2")
        }
    }

    @ColorInt
    @SuppressLint("Recycle")
    fun Context.themeColor(
        @AttrRes themeAttrId: Int
    ): Int {
        return obtainStyledAttributes(
            intArrayOf(themeAttrId)
        ).use {
            it.getColor(0, Color.MAGENTA)
        }
    }
}