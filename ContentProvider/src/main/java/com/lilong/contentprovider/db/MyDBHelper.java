package com.lilong.contentprovider.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * db helper，对应一个sqliteDBConnection
 */
public class MyDBHelper extends SQLiteOpenHelper {

    private final static int VERSION = 1;
    public final static String DB_NAME = "test.db";
    public final static String TABLE_NAME = "test";
    public final static String COLUMN_ID = "id";
    public final static String COLUMN_PRODUCT = "product";
    public final static String COLUMN_PRICE = "price";
    private final static String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_PRODUCT + " text, " + COLUMN_PRICE + " integer)";

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDBHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 创建表
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);

        // 添加两行初始数据
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_PRODUCT, "product1");
        contentValues.put(COLUMN_PRICE, "price1");
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        contentValues.clear();

        contentValues.put(COLUMN_PRODUCT, "product2");
        contentValues.put(COLUMN_PRICE, "price2");
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
