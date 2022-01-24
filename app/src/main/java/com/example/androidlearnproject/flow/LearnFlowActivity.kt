package com.example.androidlearnproject.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.androidlearnproject.databinding.ActivityLearnFlowBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/* ##
* Warning: Never collect a flow from the UI directly from launch or the launchIn extension function if the UI needs to be updated.
*  These functions process events even when the view is not visible. This behavior can lead to app crashes.
* To avoid that, use the repeatOnLifecycle API as shown above.
*  The repeatOnLifecycle API is available only in versions of the androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha01
* library and higher.
* */
class LearnFlowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearnFlowBinding
    private val viewModel: LearnFlowViewModel by viewModels()
    private val TAG = "LearnFlowActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnFlowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //this flow will be only called once the activity is created it is not recommended to attach data to the ui here
        /*  lifecycleScope.launch {
              viewModel.normalFLow.collect {
                  when (it) {
                      LearnFlowViewModel.LearnViewModelEnum.LOADING -> Log.e(TAG, "Normal flow Loading")
                      LearnFlowViewModel.LearnViewModelEnum.SUCCESS -> Log.e(TAG, "Normal flow SUCCESS")
                      LearnFlowViewModel.LearnViewModelEnum.COMPLETE -> Log.e(TAG, "Normal flow COMPLETE")
                  }
              }
          }*/
        /* this will lead the flow to be executed from the beginning every time the activity calls the onStart callback
        because this flow is a cold flow and has been called in repeatOnLifecycle(Lifecycle.State.STARTED)
         */
        /*lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.normalFLow.collect {
                    when (it) {
                        LearnFlowViewModel.LearnViewModelEnum.LOADING -> Log.e(TAG, "Normal flow Loading")
                        LearnFlowViewModel.LearnViewModelEnum.SUCCESS -> Log.e(TAG, "Normal flow SUCCESS")
                        LearnFlowViewModel.LearnViewModelEnum.COMPLETE -> Log.e(TAG, "Normal flow COMPLETE")
                    }
                }
            }
        }*/

        // ## collect stateflow and shared flow using  repeatOnLifecycle(Lifecycle.State.STARTED)
        // using this the state flow will act exactly like livedata
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myStateFlow.collect {
                    when (it) {
                        LearnFlowViewModel.LearnViewModelEnum.LOADING -> Log.e(TAG, "State flow Loading")
                        LearnFlowViewModel.LearnViewModelEnum.SUCCESS -> Log.e(TAG, "State flow SUCCESS")
                        LearnFlowViewModel.LearnViewModelEnum.COMPLETE -> Log.e(TAG, "State flow COMPLETE")
                    }
                }
            }
        }
        binding.btnUseStateFlow.setOnClickListener {
            viewModel.useStateFlow()
        }
        // using this the shared flow will act exactly like livedata but it doesn't preserve data
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mySharedFlow.collect {
                    when (it) {
                        LearnFlowViewModel.LearnViewModelEnum.LOADING -> Log.e(TAG, "Shared flow Loading")
                        LearnFlowViewModel.LearnViewModelEnum.SUCCESS -> Log.e(TAG, "Shared flow SUCCESS")
                        LearnFlowViewModel.LearnViewModelEnum.COMPLETE -> Log.e(TAG, "Shared flow COMPLETE")
                    }
                }
            }
        }
        binding.btnUseSharedFlow.setOnClickListener {
            viewModel.useSharedFlow()
        }
        //using flow with operators
        //use operators to get even numbers less than 10 and add 0.5 to them
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flowUseWithOperators.filter {
                    Log.e(TAG, "filter $it")
                    it < 10 && it % 2 == 0
                }.map {
                    Log.e(TAG, "map $it")
                    it + 0.5
                }.collect {
                    Log.e(TAG, "flow after applying operators $it")
                    binding.tvFlowResult.text = it.toString()
                }
            }
        }
        binding.btnUseFlowWithOperators.setOnClickListener {
            viewModel.useFlowWithOperators()
        }
    }
}