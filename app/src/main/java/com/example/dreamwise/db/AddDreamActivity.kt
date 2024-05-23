package com.example.dreamwise

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.dreamwise.databinding.ActivityAddDreamBinding
import com.example.dreamwise.db.AddButtonActivity
import kotlinx.coroutines.launch
import java.util.Calendar

class AddDreamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDreamBinding
    private lateinit var dreamDatabase: DreamDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDreamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dreamDatabase = DreamDatabase.getDatabase(this)

        binding.saveDreamButton.setOnClickListener {
            val title = binding.dreamTitleEditText.text.toString()
            val description = binding.dreamDescriptionEditText.text.toString()
            val isNightmare = binding.radioNightmare.isChecked

            // Get the selected date from the DatePicker
            val year = binding.dreamDatePicker.year
            val month = binding.dreamDatePicker.month
            val day = binding.dreamDatePicker.dayOfMonth
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            val date = calendar.timeInMillis

            // Check if any field is empty
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dream = Dream(title = title, description = description, isNightmare = isNightmare, date = date)

            lifecycleScope.launch {
                try {
                    dreamDatabase.dreamDao().insert(dream)
                    Toast.makeText(this@AddDreamActivity, "Dream saved successfully", Toast.LENGTH_SHORT).show()
                    intent = Intent(this@AddDreamActivity, DreamDiaryActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this@AddDreamActivity, "Failed to save dream", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
