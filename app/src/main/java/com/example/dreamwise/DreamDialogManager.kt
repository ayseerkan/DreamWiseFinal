package com.example.dreamwise

import android.content.Context
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DreamDialogManager(private val context: Context) {

    fun showSelectDreamTypeDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle("Select Dream Type")
            .setSingleChoiceItems(arrayOf("Dream", "Nightmare"), -1) { dialog, which ->
                val selectedType = if (which == 0) "Dream" else "Nightmare"
                dialog.dismiss()
                showEnterDescriptionDialog(selectedType)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showEnterDescriptionDialog(dreamType: String) {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val editText = EditText(context).apply {
            hint = "Describe your dream"
        }
        layout.addView(editText)

        MaterialAlertDialogBuilder(context)
            .setTitle("Enter Dream Description")
            .setView(layout)
            .setPositiveButton("Next") { dialog, _ ->
                val description = editText.text.toString()
                if (description.isEmpty()) {
                    editText.error = "Description cannot be empty"
                } else {
                    dialog.dismiss()
                    showSelectDateDialog(dreamType, description)
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showSelectDateDialog(dreamType: String, description: String) {
        val datePicker = DatePicker(context)

        MaterialAlertDialogBuilder(context)
            .setTitle("Select Date")
            .setView(datePicker)
            .setPositiveButton("Save") { dialog, _ ->
                val day = datePicker.dayOfMonth
                val month = datePicker.month
                val year = datePicker.year
                saveDream(dreamType, description, year, month, day)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun saveDream(dreamType: String, description: String, year: Int, month: Int, day: Int) {
        // Implementation depends on how and where you want to save this data
        // For example, inserting it into a Room database or sending it to a server
    }
}
