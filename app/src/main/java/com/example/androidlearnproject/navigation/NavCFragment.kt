package com.example.androidlearnproject.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.androidlearnproject.R
import com.example.androidlearnproject.databinding.FragmentNavBBinding
import com.example.androidlearnproject.databinding.FragmentNavCBinding

class NavCFragment : Fragment() {
    private lateinit var binding: FragmentNavCBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNavCBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToFragmentA.setOnClickListener {
            it.findNavController().navigate(R.id.action_navCFragment_to_navAFragment)
        }
    }
}