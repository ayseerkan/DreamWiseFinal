package com.example.dreamwise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dreamwise.databinding.ActivityDenemeBinding
import com.example.dreamwise.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DenemeAct : AppCompatActivity() {
    private lateinit var binding: ActivityDenemeBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        var user : FirebaseUser

        super.onCreate(savedInstanceState)

        binding = ActivityDenemeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        user = auth.currentUser!!

        if(user == null){
            val intent = Intent(this@DenemeAct, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else {
            binding.textViewDeneme.setText(user.email)
        }

        binding.logOut.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@DenemeAct, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}