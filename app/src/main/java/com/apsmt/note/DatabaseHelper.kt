package com.apsmt.note

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "noteDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "note_table"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DES = "description"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_TITLE TEXT," +
                " $COLUMN_DES TEXT, " +
                "$COLUMN_DATE TEXT, " +
                "$COLUMN_TIME TEXT)"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableIfExist = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableIfExist)
    }

    fun insertDataToDatabase(note: NoteList) {
        val db = writableDatabase
        val value = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_DES, note.description)
            put(COLUMN_DATE, note.date)
            put(COLUMN_TIME, note.time)
        }

        db.insert(TABLE_NAME, null, value)
        db.close()
    }

    fun readAllNote(): List<NoteList> {
        val noteList = mutableListOf<NoteList>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)
        while(cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DES))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))

            val note = NoteList(id, title, description, date, time)
            noteList.add(note)
        }
        db.close()
        cursor.close()
        return noteList
    }

    fun deleteNote(noteId: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(noteId.toString()))
        db.close()
    }

    @SuppressLint("Recycle")
    fun getNoteDataById(noteId: Int): NoteList {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DES))
        val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
        val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))

        val note = NoteList(id, title, description, date, time)
        db.close()
        return note
    }

    fun updateNote(note: NoteList){
        val db = writableDatabase
        val value = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_DES, note.description)
            put(COLUMN_DATE, note.date)
            put(COLUMN_TIME, note.time)
        }
        db.update(TABLE_NAME, value, "$COLUMN_ID=?", arrayOf(note.id.toString()))
        db.close()
    }
}