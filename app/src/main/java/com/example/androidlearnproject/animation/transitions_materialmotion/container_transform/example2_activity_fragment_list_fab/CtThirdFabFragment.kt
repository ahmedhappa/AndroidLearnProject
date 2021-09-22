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
import androidx.transition.Slide
import com.example.androidlearnproject.R
import com.example.androidlearnproject.databinding.FragmentCtThiredFabBinding
import com.google.android.material.transition.MaterialContainerTransform

class CtThirdFabFragment : Fragment() {
    private lateinit var binding: FragmentCtThiredFabBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCtThiredFabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterTransition = MaterialContainerTransform().apply {
            /*In addition to parameters used to configure our previous container transform,
            startView and endView are being set manually here. Instead of using transitionName attributes to
            let the Android Transition system know which views should be transformed, you can specify these manually
            when necessary.*/
            startView = requireActivity().findViewById(R.id.fab_ct_main_activity) // view from previous screen
            endView = binding.linearThirdScreen // view in this screen
            duration = 500
            scrimColor = Color.TRANSPARENT
            containerColor = requireContext().themeColor(R.attr.colorSurface)
            startContainerColor = requireContext().themeColor(R.attr.colorSecondary)
            endContainerColor = requireContext().themeColor(R.attr.colorSurface)
        }

        returnTransition = Slide().apply {
            duration = 500
            addTarget(binding.linearThirdScreen)
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