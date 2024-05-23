package com.example.dreamwise

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamwise.databinding.ActivityDreamDiaryBinding
import com.example.dreamwise.db.AddButtonActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

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

        happyDreamAdapter = DreamAdapter(emptyList()) { dream ->
            deleteDream(dream)
        }
        nightmareAdapter = DreamAdapter(emptyList()) { dream ->
            deleteDream(dream)
        }

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
                R.id.navigation_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    navigateToMain()
                    true
                }
                else -> false
            }
        }

        // Gesture Detector to scroll to the bottom
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 != null && e2 != null) {
                    if (e2.y - e1.y > 100) {
                        // Fling down detected
                        scrollToBottom()
                        return true
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })

        // Attach the gesture detector to the RecyclerView
        binding.category1RecyclerView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
        binding.category2RecyclerView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.selectedItemId = R.id.navigation_diary
    }

    private fun deleteDream(dream: Dream) {
        lifecycleScope.launch {
            dreamDatabase.dreamDao().delete(dream)
        }
    }

    private fun scrollToBottom() {
        binding.category1RecyclerView.smoothScrollToPosition(happyDreamAdapter.itemCount - 1)
        binding.category2RecyclerView.smoothScrollToPosition(nightmareAdapter.itemCount - 1)
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
