package com.example.dreamwise

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dreamwise.databinding.ActivityLoginBinding
import com.example.dreamwise.databinding.DialogForgotPasswordBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "LoginActivity"
    private var errorEmail: String = "Please enter your email!"
    private var errorPassword: String = "Please enter your password!"

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d(TAG, "User already signed in: ${currentUser.email}")
            val intent = Intent(this@LoginActivity, DenemeAct::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.d(TAG, "No user signed in")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        Log.d(TAG, "FirebaseAuth instance initialized")

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Log.w(TAG, "Email field is empty")
                binding.email.error = errorEmail
                return@setOnClickListener
            } else {
                binding.email.error = null
            }

            if (TextUtils.isEmpty(password)) {
                Log.w(TAG, "Password field is empty")
                binding.password.error = errorPassword
                return@setOnClickListener
            } else {
                binding.password.error = null
            }

            Log.d(TAG, "Attempting to sign in with email: $email")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, DenemeAct::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.forgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        binding.signUpPrompt.setOnClickListener(){
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showForgotPasswordDialog() {
        val dialogBinding = DialogForgotPasswordBinding.inflate(layoutInflater)

        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Forgot Password")
        builder.setView(dialogBinding.root)
        builder.setPositiveButton("Send") { dialog, which ->
            val email = dialogBinding.emailEditText.text.toString().trim()
            if (TextUtils.isEmpty(email)) {
                dialogBinding.emailEditText.error = "Please enter your email"
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(binding.root, "Password reset email sent.", Snackbar.LENGTH_SHORT).show()
                        } else {
                            Snackbar.make(binding.root, "Failed to send reset email: ${task.exception?.message}", Snackbar.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

}
