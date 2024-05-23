package com.example.dreamwise.db

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dreamwise.AddDreamActivity
import com.example.dreamwise.DreamData
import com.example.dreamwise.DreamDiaryActivity
import com.example.dreamwise.DreamResponse
import com.example.dreamwise.MainActivity
import com.example.dreamwise.R
import com.example.dreamwise.RetrofitInstance
import com.example.dreamwise.databinding.ActivityAddButtonBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddButtonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup for the Add New Dream button
        binding.addNewDreamButton.setOnClickListener {
            startActivity(Intent(this, AddDreamActivity::class.java))
        }

        // Setup for the Fetch JSON button
        binding.fetchJsonButton.setOnClickListener {
            fetchDreamData()
        }

        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_diary -> {
                    navigateTo(DreamDiaryActivity::class.java)
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

    private fun fetchDreamData() {
        val apiService = RetrofitInstance.api
        apiService.getDreams().enqueue(object : Callback<DreamResponse> {
            override fun onResponse(call: Call<DreamResponse>, response: Response<DreamResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        displayDreams(it.dreams)
                    }
                } else {
                    Toast.makeText(applicationContext, "Failed to load dreams: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    Log.d("AddButtonActivity", "Response not successful: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<DreamResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                Log.d("AddButtonActivity", "Error: ${t.message}")
            }
        })
    }

    private fun displayDreams(dreams: List<DreamData>) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dreamText = dreams.joinToString(separator = "\n\n") {
            "Title: ${it.title}\nDescription: ${it.description}\nIs Nightmare: ${it.isNightmare}\nDate: ${dateFormat.format(Date(it.date))}"
        }
        binding.dreamsTextView.text = dreamText
    }

    private fun navigateTo(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
    }

    private fun navigateToMain() {
        navigateTo(MainActivity::class.java)
        finish()
    }
}
