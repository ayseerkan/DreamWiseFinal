package com.example.dreamwise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.dreamwise.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for login button
        binding.loginButton.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signupButton.setOnClickListener {
            intent = Intent(this@MainActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Schedule a one-time work request to test the worker
        scheduleOneTimeWork()
    }

    private fun scheduleOneTimeWork() {
        // Define input data
        val inputData = Data.Builder()
            .putInt("num", 10)
            .putString("name", "TestWorker")
            .build()

        // Create a OneTimeWorkRequest
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(CustomWorker::class.java)
            .setInputData(inputData)
            .build()

        // Enqueue the work
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)

        // Monitor the work status
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null) {
                    when (workInfo.state) {
                        WorkInfo.State.ENQUEUED -> Log.d("WorkStatus", "Work is enqueued")
                        WorkInfo.State.RUNNING -> Log.d("WorkStatus", "Work is running")
                        WorkInfo.State.SUCCEEDED -> {
                            Log.d("WorkStatus", "Work succeeded")
                            val result = workInfo.outputData.getInt("result", -1)
                            Log.d("WorkStatus", "Result: $result")
                        }
                        WorkInfo.State.FAILED -> Log.d("WorkStatus", "Work failed")
                        WorkInfo.State.BLOCKED -> Log.d("WorkStatus", "Work is blocked")
                        WorkInfo.State.CANCELLED -> Log.d("WorkStatus", "Work is cancelled")
                    }
                }
            })
    }
}
