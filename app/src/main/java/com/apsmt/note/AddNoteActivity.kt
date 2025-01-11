package com.apsmt.note

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.apsmt.note.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.min

class AddNoteActivity : AppCompatActivity() {

    private var _binding: ActivityAddNoteBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.txvDate.setOnClickListener {
            displayDatePickerDialog()
        }

        binding.txvTime.setOnClickListener {
            displayTimePickerDialog()
        }

        binding.imvBack.setOnClickListener {
            onBackPressed()
        }

        binding.imvSave.setOnClickListener {
            val title = binding.edtInputTitle.text.toString()
            val description = binding.edtInputDescription.text.toString()
            val date = binding.txvDate.text.toString()
            val time = binding.txvTime.text.toString()
            if(title.isNotEmpty() && description.isNotEmpty()) {
                val note = NoteList(0, title, description, date, time)
                databaseHelper.insertDataToDatabase(note)
                Toast.makeText(
                    this,
                    "Successfully saved",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
            else {
                Toast.makeText(
                    this,
                    "Please fill out title and description",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun displayDatePickerDialog() {
        val date = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            binding.txvDate.text = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault()).format(calendar.time)
        }

        DatePickerDialog(
            this,
            date,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun displayTimePickerDialog() {
        val time = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            binding.txvTime.text = SimpleDateFormat("HH: mm").format(calendar.time)
        }

        TimePickerDialog(
            this,
            time,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}