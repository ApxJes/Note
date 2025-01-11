package com.apsmt.note

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    var noteList: List<NoteList>,
    val context: Context): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

        private lateinit var databaseHelper: DatabaseHelper

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.txvTitle)
        val date: TextView = itemView.findViewById(R.id.txvDisplayDateOnCardBg)
        val delete: ImageView = itemView.findViewById(R.id.imvDelete)
        val edit: ConstraintLayout = itemView.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        )
    }

    override fun getItemCount(): Int  = noteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNote = noteList[position]
        holder.title.text = currentNote.title
        holder.date.text = currentNote.date

        holder.delete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete Note!")
                .setMessage("Are you sure to delete ${currentNote.id}")
                .setCancelable(true)
                .setPositiveButton("Yes"){_, _ ->
                    databaseHelper = DatabaseHelper(context)
                    databaseHelper.deleteNote(currentNote.id)
                    refreshData(databaseHelper.readAllNote())
                    Toast.makeText(
                        context,
                        "Successfully deleted ${currentNote.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton("Cancel"){_, _ -> }
                .create().show()
        }

        holder.edit.setOnClickListener {
            Intent(
                context,
                UpdateNoteActivity::class.java
            ).also{
                it.putExtra("id", currentNote.id)
                context.startActivity(it)
            }
        }
    }

    fun refreshData(note: List<NoteList>) {
        this.noteList = note
        notifyDataSetChanged()
    }
}