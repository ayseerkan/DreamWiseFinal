package com.example.dreamwise

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamwise.databinding.ActivityDreamDiaryBinding
import com.example.dreamwise.db.AddButtonActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DreamDiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDreamDiaryBinding
    private lateinit var dreamDatabase: DreamDatabase
    private lateinit var happyDreamAdapter: DreamAdapter
    private lateinit var nightmareAdapter: DreamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDreamDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dreamDatabase = DreamDatabase.getDatabase(this)

        happyDreamAdapter = DreamAdapter(emptyList())
        nightmareAdapter = DreamAdapter(emptyList())

        binding.category1RecyclerView.layoutManager = LinearLayoutManager(this)
        binding.category1RecyclerView.adapter = happyDreamAdapter

        binding.category2RecyclerView.layoutManager = LinearLayoutManager(this)
        binding.category2RecyclerView.adapter = nightmareAdapter

        // Observe the data
        dreamDatabase.dreamDao().getHappyDreams().observe(this, { dreams ->
            happyDreamAdapter.updateDreams(dreams)
        })

        dreamDatabase.dreamDao().getNightmares().observe(this, { nightmares ->
            nightmareAdapter.updateDreams(nightmares)
        })

        // Set up BottomNavigationView
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Navigate to Add Dream Activity
                    val intent = Intent(this@DreamDiaryActivity, AddButtonActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_diary -> {
                    // Stay in DreamDiaryActivity
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.selectedItemId = R.id.navigation_diary
    }
}
