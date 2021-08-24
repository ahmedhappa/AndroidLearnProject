package com.example.androidtestproject.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.androidtestproject.R
import com.example.androidtestproject.databinding.FragmentNavBBinding

class NavBFragment : Fragment() {
    private lateinit var binding: FragmentNavBBinding

    // to get the data passed to this fragment by safe navigation arguments
    private val navArgs: NavBFragmentArgs by navArgs()
    private val viewModel: SharedViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNavBBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvFragmentB.text = navArgs.extraData
        binding.btnGoToFragmentC.setOnClickListener {
            it.findNavController().navigate(R.id.action_navBFragment_to_navCFragment)
        }

        viewModel.dummyStringLiveData.observe(viewLifecycleOwner, {
            binding.tvSharedViewModelString.text = it
        })

        //to pass data to previous fragment in the backstack
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "extra_data_passed_from_next_fragment",
            "data backed from fragment b"
        )
    }
}