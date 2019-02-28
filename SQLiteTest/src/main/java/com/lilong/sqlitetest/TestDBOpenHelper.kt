package com.lilong.sqlitetest

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TestDBOpenHelper(context: Context?, name: String?, cursorFactory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, cursorFactory, version) {

    companion object {
        val TABLE_NAME = "test"
        val COLUMN_ID = "id"
        val COLUMN_THREAD = "thread"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSQL: String = "create table " + TABLE_NAME + "(" + COLUMN_ID + " integer, " + COLUMN_THREAD + " text)";
        db?.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}