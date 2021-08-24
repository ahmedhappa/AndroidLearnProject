package com.example.androidtestproject.navigation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.androidtestproject.R
import com.example.androidtestproject.databinding.FragmentNavABinding

class NavAFragment : Fragment() {
    private lateinit var binding: FragmentNavABinding

    /*this way is to create a view model that is shared between all fragments in the navigation graph this view model will be
    removed when there is no any fragments in the graph and the graph is destroyed */
    private val viewModel: SharedViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNavABinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToFragmentB.setOnClickListener {
            /*to navigate from one fragment to another we have to gain access to navController which can be achieved by calling
                    findNavController() function on any view */
            /* to pass data between fragments using navigation library you need to call the Class Directions which is created for
            every class using navigation ex NavAFragmentDirections then call the action and pass the arguments */
            it.findNavController()
                .navigate(NavAFragmentDirections.actionNavAFragmentToNavBFragment("Data passed from fragment A"))
        }


        binding.btnChangeData.setOnClickListener {
            viewModel.setNewDummyString(binding.etChangeData.text.toString())
        }

        viewModel.dummyStringLiveData.observe(viewLifecycleOwner, Observer {
            binding.tvSharedViewModelString.text = it
        })

        //to get data passed from next fragment in the stack
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("extra_data_passed_from_next_fragment")
            ?.observe(viewLifecycleOwner, {
                binding.tvDataFromNextFragment.text = it
            })

        binding.btnNavigateWithDeepLink.setOnClickListener {
            /*To navigate to a destination using a deep link, you must first build a NavDeepLinkRequest
            and then pass that deep link request into the Navigation controller's call to navigate()*/
            val navDeepLink = NavDeepLinkRequest.Builder
                .fromUri(Uri.parse("https://www.example.com/fragmentc"))
                .setMimeType("myType/mySubtype")
                .setAction("android.intent.action.myAction")
                .build()
            findNavController().navigate(navDeepLink)
        }
    }
}