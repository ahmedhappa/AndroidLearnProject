package com.example.androidlearnproject.animation.transitions_materialmotion.container_transform.example2_act_fra_list_fab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.example.androidlearnproject.R
import com.example.androidlearnproject.databinding.FragmentCtFirstListBinding
import com.google.android.material.transition.MaterialElevationScale

class CtFirstListFragment : Fragment() {
    private lateinit var binding: FragmentCtFirstListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCtFirstListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* ## this is really important for the back transition
        Typically, this first issue of the collapse not working is because when the Android
        Transition system is trying to run your return transition, the list of emails hasn't been inflated and
        populated into the RecyclerView yet. We need a way to wait until our HomeFragment lays out our list before
        we start our transitions.

        The Android Transition system provides methods to do just that - postponeEnterTransition and startPostponedEnterTransition.
        If postponeEnterTransition is called, any entering transition to be run will be held until a closing call to
        startPostponedEnterTransition is called. This gives us the opportunity to "schedule" our transitions until after the
        RecyclerView has been populated with emails and the transition is able to find the mappings you configured.*/
        //this will pause the transition until we start it again
        postponeEnterTransition()
        //this will resume the transition after the inflation and before the draw of views on screen
        view.doOnPreDraw { startPostponedEnterTransition() }

        val dataList = listOf(
            Pair("Item1", "this is the description of the first item"),
            Pair("Item2", "this is the description of the second item"),
            Pair("Item3", "this is the description of the third item"),
            Pair("Item4", "this is the description of the forth item"),
        )

        val secondFragment = CtSecondDetailsFragment()
        binding.rvCtFirstScreen.adapter = AdapterFirstScreen(dataList) { position, sharedElementView ->
            /* The issue of the email list disappearing is because when navigating to a new Fragment using the Navigation
            Component, the current Fragment is immediately removed and replaced with our new, incoming Fragment MDC-Android provides two
            transitions to do this for you - Hold and MaterialElevationScale. The Hold transition simply keeps its target
            in its current position while MaterialElevationScale runs a subtle scale animation.*/
            /* Set exit and reenter transitions here as opposed to in onCreate because these transitions
               will be set and overwritten on HomeFragment for other navigation actions. */
            // exitTransition = Hold() it is better to use the other it gives a better animation
            exitTransition = MaterialElevationScale(false).apply {
                duration = 500 // preferred to be the same duration in the second fragment
            }
            /*A Fragment is reentering when it is coming back into view after the current Fragment is popped off the back stack.
            For FirstFragment, this happens when pressing back from SecondFragment. Here, this will cause our list of
            emails to subtly scale in when we return to the list.*/
            reenterTransition = MaterialElevationScale(true).apply {
                duration = 500
            }

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            val bundles = Bundle().apply {
                putString("extraData1", dataList[position].first)
                putString("extraData2", dataList[position].second)
            }
            secondFragment.arguments = bundles
            //
            /* the first parameter is the view in the item list in the FirstFragment
                the second parameter is the sharedElement string set to the view in the SecondFragment
             */
            /*
            The key part of the above snippet is The Android Transition system will get the transition name set on the first parameter
            (cardView), and create a mapping between that transition name and the transition name string passed in as the second
            parameter in the secondFragment
             */
            transaction.addSharedElement(sharedElementView, "secondScreenCardViewTransition")
            /* ## //If using the Navigation Architecture Component, use the following.
            // Map the start View in FragmentA and the transitionName of the end View in FragmentB
            val extras = FragmentNavigatorExtras(view to "secondScreenCardView")
            findNavController().navigate(R.id.action_fragmentA_to_fragmentB, null, null, extras)*/
            transaction.replace(R.id.frame_fragment_container, secondFragment).addToBackStack("secondFragment").commit()
        }
    }
}