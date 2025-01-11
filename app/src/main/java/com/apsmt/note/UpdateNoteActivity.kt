package com.apsmt.note

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apsmt.note.databinding.ActivityUpdateNoteBinding

class UpdateNoteActivity : AppCompatActivity() {

    private var _binding: ActivityUpdateNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(this)

        binding.imvBack.setOnClickListener {
            onBackPressed()
        }

        val id = intent.getIntExtra("id", -1)
        val noteList = databaseHelper.getNoteDataById(id)
        binding.edtUpdateTitle.setText(noteList.title)
        binding.edtUpdateDescription.setText(noteList.description)
        binding.txvDate.text = noteList.date
        binding.txvTime.text = noteList.time

        binding.imvUpdate.setOnClickListener {
            val title = binding.edtUpdateTitle.text.toString()
            val description = binding.edtUpdateDescription.text.toString()
            val date = binding.txvDate.text.toString()
            val time = binding.txvTime.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val note = NoteList(id, title, description, date, time)
                databaseHelper.updateNote(note)
                Toast.makeText(
                    this,
                    "Successfully update",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Please fill out title and description",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}