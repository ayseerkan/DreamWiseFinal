package com.example.dreamwise

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dreamwise.databinding.ActivityLoginBinding
import com.example.dreamwise.databinding.DialogForgotPasswordBinding
import com.example.dreamwise.db.AddButtonActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "LoginActivity"

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d(TAG, "User already signed in: ${currentUser.email}")
            val intent = Intent(this@LoginActivity, AddButtonActivity::class.java)
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

        //Sign Up page
        binding.signUpPrompt.setOnClickListener(){
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        Log.d(TAG, "FirebaseAuth instance initialized")

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Log.w(TAG, "Email field is empty")
                binding.email.error = "Please enter email!"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Log.w(TAG, "Password field is empty")
                binding.password.error = "Please enter password!"
                return@setOnClickListener
            }

            Log.d(TAG, "Attempting to sign in with email: $email")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, DreamDiaryActivity::class.java)
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
    }

    private fun showForgotPasswordDialog() {
        val dialogBinding = DialogForgotPasswordBinding.inflate(layoutInflater)

        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Forgot Password")
        builder.setView(dialogBinding.root)
        builder.setPositiveButton("Send") { dialog, which ->
            val email = dialogBinding.emailEditText.text.toString().trim()
            if (TextUtils.isEmpty(email)) {
                dialogBinding.emailEditText.error = "Please enter email"
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(binding.root, "Password reset email sent.", Snackbar.LENGTH_SHORT).show()
                            requestNotificationPermission()
                        } else {
                            Snackbar.make(binding.root, "Failed to send reset email: ${task.exception?.message}", Snackbar.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            } else {
                showResetPasswordNotification()
            }
        } else {
            showResetPasswordNotification()
        }
    }

    private fun showResetPasswordNotification() {
        Notifications(this).showNotification("Password Reset", "Your password reset email has been sent.")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                showResetPasswordNotification()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}