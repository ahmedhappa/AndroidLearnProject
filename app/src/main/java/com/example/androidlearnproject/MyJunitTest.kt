package com.example.androidlearnproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidlearnproject.databinding.ActivityMyJunitTestBinding

// see MyLocalJunit4Test for using junit4 test on this class
class MyJunitTest : AppCompatActivity() {
    private lateinit var binding: ActivityMyJunitTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyJunitTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSubmit.setOnClickListener {
            if (RegistrationUtil.validateRegistrationInputs(
                    binding.etName.text.toString(),
                    binding.etPass.text.toString(),
                    binding.etConfirmPass.text.toString()
                )
            ) {
                Toast.makeText(
                    this,
                    "Correct Data",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Please Enter A Correct Data",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // this test is a local unit test that is not contain any android framework so we put the test class in test package named (RegistrationUtilTest.kt)
    object RegistrationUtil {
        private val existingUsers = listOf("ahmed", "ali")
        fun validateRegistrationInputs(
            userName: String,
            pass: String,
            confirmPass: String
        ): Boolean {
            return when {
                userName.isEmpty() -> false
                userName in existingUsers -> false
                pass != confirmPass -> false
                else -> true
            }
        }
    }

    /* this test is a integrated unit test that is contain android framework(context)
     so we put the test class in androidTest package named (ResourceCompareTest.kt)*/
    class ResourceCompare {
        fun compareStringResource(
            context: Context,
            stringResource: Int,
            myString: String
        ): Boolean {
            return context.getString(stringResource) == myString
        }
    }
}