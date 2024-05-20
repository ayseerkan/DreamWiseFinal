package com.example.dreamwise

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dreamwise.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "SignUpActivity"
    private var errorEmail: String = "Please enter an email!"
    private var errorPassword: String = "Please enter a password!"
    private var errorPasswordShort: String = "Password must be at least 6 characters"
    private var errorUsername: String = "Please enter an username!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        Log.d(TAG, "FirebaseAuth instance initialized")

        binding.logInText.setOnClickListener(){
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signUpButton.setOnClickListener {
            val username = binding.username.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (TextUtils.isEmpty(username)) {
                Log.w(TAG, "Username field is empty")
                binding.username.error = errorUsername
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(email)) {
                Log.w(TAG, "Email field is empty")
                binding.email.error = errorEmail
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Log.w(TAG, "Password field is empty")
                binding.password.error = errorPassword
                return@setOnClickListener
            }

            if (password.length < 6) {
                Log.w(TAG, "Password is too short")
                binding.password.error = errorPasswordShort
                return@setOnClickListener
            }

            createUserWithRetry(email, password)
        }
    }

    private fun createUserWithRetry(email: String, password: String, retries: Int = 3) {
        Log.d(TAG, "Attempting to create user with email: $email")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(this, "Account created.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SignUpActivity, DenemeAct::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val exception = task.exception
                    if (exception is FirebaseNetworkException && retries > 0) {
                        Log.w(TAG, "Network error, retrying... ($retries retries left)")
                        createUserWithRetry(email, password, retries - 1)
                    } else {
                        Log.e(TAG, "createUserWithEmail:failure", exception)
                        Toast.makeText(this, "Authentication failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
