package com.lilong.sqlitetest

import android.app.Activity
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle

class MainActivity : Activity() {

    val DB_NAME:String = "test.db"
    val DB_VERSION:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var dbOpenHelper:TestDBOpenHelper = TestDBOpenHelper(MainApplication.Companion.sInstance, DB_NAME, null, DB_VERSION)
        var db:SQLiteDatabase = dbOpenHelper.writableDatabase;
    }

    class InsertDataRunnable(val name:String, val db:SQLiteDatabase) : Runnable {

        override fun run() {
            try {
                db.beginTransaction()
                for (i in 0..100){
                    var contentValues: ContentValues = ContentValues()
                    contentValues.put(TestDBOpenHelper.COLUMN_ID, i)
                    contentValues.put(TestDBOpenHelper.COLUMN_PRODUCT, "product_" + i)
                    contentValues.put(TestDBOpenHelper.COLUMN_PRICE, "price_" + i)
                    db.insert(TestDBOpenHelper.TABLE_NAME, null, contentValues)
                }
                db.setTransactionSuccessful()
            }catch (e:Exception){

            }finally {
                db.endTransaction()
            }
        }
    }
}
