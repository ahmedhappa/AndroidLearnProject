package com.example.androidtestproject.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.androidtestproject.R
import com.example.androidtestproject.databinding.FragmentNavBBinding
import com.example.androidtestproject.databinding.FragmentNavCBinding

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