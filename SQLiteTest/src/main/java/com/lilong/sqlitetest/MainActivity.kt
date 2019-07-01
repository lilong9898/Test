package com.lilong.sqlitetest

import android.app.Activity
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_NONE
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick

/**
 * Android SQLite的并发问题:
 * 结论:
 * (1) 使用同一个SQLiteOpenHelper时,多个线程访问数据库会被串行化,不会引起问题
 * (2) 使用不同SQLiteOpenHelper时,多个线程同时访问,会出现android.database.sqlite.SQLiteDatabaseLockedException: database is locked (code 5)
 *
 * 原因:
 * (1) SQLiteDatabase底层使用SQLiteSession访问数据库, 具体方法是SQLiteDatabase.getThreadSession
 * (2) SQLiteDatabase.getThreadSession内部通过SQLiteDatabase中存储的一个ThreadLocal变量,取得SQLiteSession,所以SQLiteSession和线程是一一对应的
 * (3) 同一个SQLiteOpenHelper得到的SQLiteDatabase使用同一个SQLiteConnection
 * (4) 由(2)和(3)可见, SQLiteDatabase将不同的SQLiteSession串行分配到同一个SQLiteConnection上,等效于将不同线程上的数据库操作("数据库操作"实质上是Transaction)串行分配到同一个SQLiteConnection上
 *
 * 不同线程使用不同SQLiteOpenHelper时,会有不同的SQLiteSession,虽然他们各自串行分配各自的SQLiteSession,但在db看来,会有不同线程上的Session同时发送给自己,会抛出SQLiteDatabase Locked Code = 5异常
 * 不同线程使用相同SQLiteOpenHelper时,SQLiteDatabase会将这些线程对应的SQLiteSession串行分配到这个SQLiteOpenHelper对应的SQLiteConnection上,一个Session执行完,立即执行下一个Session
 *
 * "串行分配"的底层操作,在SQLiteConnectionPool的waitForConnection方法里
 * */
class MainActivity : Activity() {

    val TAG = "STest"
    val DB_NAME = "test.db"
    val DB_VERSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var dbOpenHelper = TestDBOpenHelper(MainApplication.sInstance, DB_NAME, null, DB_VERSION)
        var db = dbOpenHelper.writableDatabase;
//        var dbOpenHelper2 = TestDBOpenHelper(MainApplication.sInstance, DB_NAME, null, DB_VERSION)
//        var db2 = dbOpenHelper2.writableDatabase
        btnStartConcurrentInsertDBThreads.onClick {
            var thread1 = Thread(InsertDataRunnable("thread1", db))
            var thread2 = Thread(InsertDataRunnable("thread2", db))
            thread1.start()
            thread2.start()
        }
        btnClearDB.onClick {
            db.delete(TestDBOpenHelper.TABLE_NAME, null, null)
        }
    }

    inner class InsertDataRunnable(val name: String, val db: SQLiteDatabase) : Runnable {

        override fun run() {
            try {
                Log.i(TAG, name + " begins transaction")
                // 一个transaction期间,会占用数据库
                // beginTransaction到endTransaction期间的所有数据库操作都属于这个transaction
                db.beginTransaction()
                var contentValues = ContentValues()
                contentValues.put(TestDBOpenHelper.COLUMN_ID, "1")
                contentValues.put(TestDBOpenHelper.COLUMN_THREAD, name)
                db.insertWithOnConflict(TestDBOpenHelper.TABLE_NAME, null, contentValues, CONFLICT_NONE)
                // 让线程休眠5秒,让transaction占用数据库的时间为5秒
                Thread.sleep(5000)
                Log.i(TAG, name + " inserts data successfully")
                db.setTransactionSuccessful()
            } catch (t: Throwable) {
                Log.i(TAG, name + " fails, throwable = " + t)
            } finally {
                db.endTransaction()
            }
        }
    }

}
