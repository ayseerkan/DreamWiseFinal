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

        // Set up Add New Dream button
        binding.addNewDreamButton.setOnClickListener {
            val intent = Intent(this, AddDreamActivity::class.java)
            startActivity(intent)
        }

        // Fetch and display JSON data
        fetchDreamData()

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

    private fun fetchDreamData() {
        val apiService = RetrofitInstance.api
        val call = apiService.getDreams()
        call.enqueue(object : Callback<DreamResponse> {
            override fun onResponse(call: Call<DreamResponse>, response: Response<DreamResponse>) {
                if (response.isSuccessful) {
                    val dreamResponse = response.body()
                    if (dreamResponse != null) {
                        displayDreams(dreamResponse.dreams)
                    }
                } else {
                    Log.d("AddButtonActivity", "Response not successful: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<DreamResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG).show()
                Log.d("AddButtonActivity", "Error: ${t.message}")
            }
        })
    }

    private fun displayDreams(dreams: List<DreamData>) {
        // Format and display all dream information
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dreamText = dreams.joinToString(separator = "\n\n") {
            val date = dateFormat.format(Date(it.date))
            "Title: ${it.title}\nDescription: ${it.description}\nIs Nightmare: ${it.isNightmare}\nDate: $date"
        }
        binding.dreamsTextView.text = dreamText
    }
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
