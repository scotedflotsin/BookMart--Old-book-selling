package com.bookmart.bookmart.LocalDB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HistoryDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "history_database"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "history_table"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_SEARCH_TITLE = "search_title"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_SEARCH_TITLE TEXT NOT NULL)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addHistory(searchTitle: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SEARCH_TITLE, searchTitle)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun deleteHistory(searchTitle: String): Int {
        val db = this.writableDatabase
        val whereClause = "$COLUMN_SEARCH_TITLE = ?"
        val whereArgs = arrayOf(searchTitle)
        val deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
        return deletedRows
    }

    fun getAllHistory(): List<String> {
        val historyList = mutableListOf<String>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                // Use the column index directly or make sure the column name is correct
                val columnIndex = cursor.getColumnIndex(COLUMN_SEARCH_TITLE)
                val searchTitle = cursor.getString(columnIndex)
                historyList.add(searchTitle)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return historyList
    }

}
