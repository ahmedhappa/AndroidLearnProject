package com.example.androidtestproject

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtestproject.databinding.ActivityFirebaseOTPAuthenticationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class FirebaseOTPAuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirebaseOTPAuthenticationBinding
    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var mAuth: FirebaseAuth? = null
    private val countryCodeList = listOf("+20", "+1")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirebaseOTPAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = Firebase.auth
        // Check if there is user signed in on this device is signed in.
        if (mAuth?.currentUser != null) {
            binding.btnSignOut.isEnabled = true
            binding.btnVerifyNumber.isEnabled = false
            Toast.makeText(
                this@FirebaseOTPAuthenticationActivity,
                "already signed in sign out first",
                Toast.LENGTH_LONG
            ).show()
        } else {
            binding.btnVerifyNumber.isEnabled = true
            binding.btnSignOut.isEnabled = false
        }

        binding.spinnerCountryCode.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            countryCodeList
        )

        binding.btnVerifyNumber.setOnClickListener {
            binding.etPhoneNumber.text?.let {

                if (it.toString().length == 10) {
                    val phoneNumber =
                        "${countryCodeList[binding.spinnerCountryCode.selectedItemPosition]}${it.toString()}"

                    val phoneAuthOptions = PhoneAuthOptions.newBuilder(mAuth!!)
                        .setPhoneNumber(phoneNumber)
                        .setActivity(this)
                        .setTimeout(60, TimeUnit.SECONDS)
                        .setCallbacks(object :
                            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            override fun onCodeSent(
                                verificationId: String,
                                token: PhoneAuthProvider.ForceResendingToken
                            ) {
                                binding.btnVerifyCode.isEnabled = true
                                //This method is called after the verification code has been sent by SMS to the provided phone number.
                                Log.i("Auth Call", "onCodeSent")
                                // Save verification ID and resending token so we can use them later
                                storedVerificationId = verificationId
                                resendToken = token
                            }

                            //For Automatic Verification without user interaction
                            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                Log.i("Auth Call", "onVerificationCompleted")
                                // This callback will be invoked in two situations:
                                // 1 - Instant verification. In some cases the phone number can be instantly
                                //     verified without needing to send or enter a verification code.
                                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                                //     detect the incoming verification SMS and perform verification without
                                //     user action.

                                signInWithPhoneAuthCredential(credential)
                            }

                            override fun onVerificationFailed(e: FirebaseException) {
                                Log.i("Auth Call", "onVerificationFailed")
                                // This callback is invoked in an invalid request for verification is made,
                                // for instance if the the phone number format is not valid.

                                when (e) {
                                    is FirebaseAuthInvalidCredentialsException -> {
                                        // Invalid request
                                        Toast.makeText(
                                            this@FirebaseOTPAuthenticationActivity,
                                            "Invalid request",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    is FirebaseTooManyRequestsException -> {
                                        // The SMS quota for the project has been exceeded
                                        Toast.makeText(
                                            this@FirebaseOTPAuthenticationActivity,
                                            "The SMS quota for the project has been exceeded",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    else -> {
                                        e.printStackTrace()
                                    }
                                }
                            }

                        })
                        .build()

                    PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
                } else {
                    Toast.makeText(this, "Enter Correct Phone Number", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }

        //For Manual Verification using user interaction
        binding.btnVerifyCode.setOnClickListener {
            //create credential  from user sent code and verification id
            storedVerificationId?.let {
                val credential = PhoneAuthProvider.getCredential(
                    storedVerificationId!!,
                    binding.etFirebaseCode.text.toString()
                )
                signInWithPhoneAuthCredential(credential)
            }
        }

        binding.btnSignOut.setOnClickListener {
            // to sign out user from firebase.
            binding.btnSignOut.isEnabled = false
            binding.btnVerifyCode.isEnabled = false
            mAuth?.signOut()
            binding.btnVerifyNumber.isEnabled = true
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                binding.btnVerifyCode.isEnabled = false
                binding.btnSignOut.isEnabled = true
                binding.btnVerifyNumber.isEnabled = false
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(this, "signInWithCredential:success", Toast.LENGTH_SHORT).show()
            } else {
                // Sign in failed, display a message and update the UI
                Toast.makeText(
                    this,
                    "signInWithCredential:failure , ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    Toast.makeText(
                        this,
                        "verification code entered was invalid",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}