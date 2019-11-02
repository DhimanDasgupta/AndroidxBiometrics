package com.dhimandasgupta.androidxbiometrics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity

class MainActivity : AppCompatActivity() {
    private var executor: MainThreadExecutor? = null
    private var promptInfo: BiometricPrompt.PromptInfo? = null
    private var biometricPrompt: BiometricPrompt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpBiometricPrompt()

        findViewById<AppCompatButton>(R.id.authenticate_button).also {
            it.setOnClickListener { showBiometricPrompt() }
        }
    }

    override fun onPause() {
        super.onPause()
        biometricPrompt?.cancelAuthentication()
        executor?.shutDown()
    }

    private fun setUpBiometricPrompt() {
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setSubtitle("Please confirm the biometric prompt")
            .setNegativeButtonText("Cancel")
            .setConfirmationRequired(true)
            .build()

        executor = MainThreadExecutor()
        val activity: FragmentActivity = this // reference to activity

        executor?.let { mainExecutor ->
            biometricPrompt = BiometricPrompt(activity, mainExecutor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(applicationContext,
                            "Authentication error: $errString", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        val authenticatedCryptoObject: BiometricPrompt.CryptoObject? = result.cryptoObject

                        Toast.makeText(applicationContext, "Authentication success",
                            Toast.LENGTH_SHORT)
                            .show()

                        authenticatedCryptoObject?.let {

                        }
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(applicationContext, "Authentication failed",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
    }

    private fun showBiometricPrompt() {
        // Displays the "log in" prompt.
        promptInfo?.let {
            biometricPrompt?.authenticate(it)
        }

    }
}
