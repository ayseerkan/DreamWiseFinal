package com.example.dreamwise.db

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dreamwise.AddDreamActivity
import com.example.dreamwise.DreamDiaryActivity
import com.example.dreamwise.LoginActivity
import com.example.dreamwise.MainActivity
import com.example.dreamwise.R
import com.example.dreamwise.databinding.ActivityAddButtonBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class AddButtonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Add New Dream button
        binding.addNewDreamButton.setOnClickListener {
            val intent = Intent(this, AddDreamActivity::class.java)
            startActivity(intent)
        }

        // Set up BottomNavigationView
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Stay in AddButtonActivity
                    true
                }
                R.id.navigation_diary -> {
                    // Navigate to Dream Diary Activity
                    val intent = Intent(this@AddButtonActivity, DreamDiaryActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    navigateToMain()
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.selectedItemId = R.id.navigation_home
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
