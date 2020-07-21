package com.biometriclock.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity() {
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener {
            startBiometricLock()

        }

    }

    private fun startBiometricLock() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                showLoginFingerPrintPrompt()
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
            ->
                Toast.makeText(
                    this,
                    "Sorry your phone doesn't have any biometric ",
                    Toast.LENGTH_SHORT
                ).show()
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(
                    this,
                    "Sorry your phone doesn't support for biometric  ",
                    Toast.LENGTH_SHORT
                ).show()
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Log.e(
                    "MY_APP_TAG", "The user hasn't associated " +
                            "any biometric credentials with their account."
                )
        }
    }

    private fun showLoginFingerPrintPrompt() {
        executor = ContextCompat.getMainExecutor(this)
//        getting authentication of user
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@MainActivity, "Sorry invalid user", Toast.LENGTH_SHORT)
                        .show()
                }

                @SuppressLint("SetTextI18n")
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(this@MainActivity, "Successfully Login", Toast.LENGTH_SHORT)
                        .show()
                    txtViewMsg.text = "Welcome! User"
                    txtViewMsg.gravity = Gravity.CENTER
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@MainActivity, "Authentication Failed", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        createPrompt()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun createPrompt() {
        /*
        *   create fingerprint prompt
        *   if mobile have fac pattern then  it also work
        */
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Lock ")
            .setSubtitle("Login using your biometric")
            .setNegativeButtonText("Cancel")
            .build()
    }
}