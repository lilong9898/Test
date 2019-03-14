package com.lilong.contentprovider.provider;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.lilong.contentprovider.db.MyDBHelper;

import static com.lilong.contentprovider.activity.MainActivity.TAG;
import static com.lilong.contentprovider.db.MyDBHelper.TABLE_NAME;

/**
 * -----------------关于{@link UriMatcher}-------------------------------------------------
 * URI_MATCHER是一个工具类，用来快速将一种uri对应到一个自定义的数字
 * 然后在contentProvider的CRUD方法中，会进一步将这个自定义的数字对应到一个数据库, 一个表和一种操作
 * 最终整个uri match流程，实现了一种uri对应到一个数据库, 一个表和一种操作
 *
 * 一种操作：增删改查中的哪一种操作
 *
 * 操作的参数（比如要更新的记录的id）不写到uri中，而是调用content resolver方法时，在方法参数里写
 * 甚至到底是哪种操作，也可以由调用content resolver的不同方法来区分
 * 如果provider的数据源只有一个表，连表名都不需指定，这样uri_matcher就彻底不需要了
 *
 * 如果需要uri match这种功能，也可以不用uri matcher，手工解析uri并对应到操作
 *
 * ------------------关于{@link ContentProvider}的初始化时机----------------------------------
 * 在ActivityThread中初始化, 其中
 * handleBindApplication -> installContentProviders -> installProvider, 其中
 * (1) 通过反射调用ContentProvider的构造函数
 * (2) 调用{@link ContentProvider#onCreate()}方法
 * 然后才构造{@link Application}对象并调用其{@link Application#onCreate()}
 */
public class MyContentProvider extends ContentProvider {

    private MyDBHelper myDBHelper;

    private static final String AUTHORITIES = "com.lilong.contentprovider.provider.MyContentProvider";

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * 查询test表中的(一条或多条)记录的uri的path字符串，以及其对应到的code
     * *是通配符，可匹配一串文字
     * #是通配符，可匹配一个数字
     */
    private static final String URI_PATH_TABLE_TEST_QUERY = TABLE_NAME + "/" + "query";
    private static final int URI_CODE_TABLE_TEST_QUERY = 1;

    /**
     * 向test表中添加(一条或多条)记录的uri的path字符串，以及其对应到的code
     */
    private final static String URI_PATH_TABLE_TEST_INSERT = TABLE_NAME + "/" + "insert";
    private final static int URI_CODE_TABLE_TEST_INSERT = 2;

    /**
     * 从test表中删除(一条或多条)记录的uri的path字符串，以及其对应到的code
     */
    private final static String URI_PATH_TABLE_TEST_DELETE = TABLE_NAME + "/" + "delete";
    private final static int URI_CODE_TABLE_TEST_DELETE = 3;

    /**
     * 更新test表中(一条或多条)记录的uri的path字符串，以及其对应到的code
     */
    private final static String URI_PATH_TABLE_TEST_UPDATE = TABLE_NAME + "/" + "update";
    private final static int URI_CODE_TABLE_TEST_UPDATE = 4;

    static {
        URI_MATCHER.addURI(AUTHORITIES, URI_PATH_TABLE_TEST_INSERT, URI_CODE_TABLE_TEST_INSERT);
        URI_MATCHER.addURI(AUTHORITIES, URI_PATH_TABLE_TEST_QUERY, URI_CODE_TABLE_TEST_QUERY);
        URI_MATCHER.addURI(AUTHORITIES, URI_PATH_TABLE_TEST_DELETE, URI_CODE_TABLE_TEST_DELETE);
        URI_MATCHER.addURI(AUTHORITIES, URI_PATH_TABLE_TEST_UPDATE, URI_CODE_TABLE_TEST_UPDATE);
    }

    public MyContentProvider(){
        super();
        Log.i(TAG, "content provider constructor called by ");
        Log.i(TAG, Log.getStackTraceString(new Throwable()));
    }

    @Override
    public boolean onCreate() {
        Log.i(TAG, "content provider onCreate");
        myDBHelper = new MyDBHelper(getContext());
        return false;
    }

    // uri指向的是多条数据（如果contentProvider由db作为数据源，则为多条表行），要返回vnd.android.cursor.dir/${自定义部分}
    // uri指向的是单条数据（如果contentProvider由db作为数据源，则为单条表行），要返回vnd.android.cursor.item/${自定义部分}
    // 如contentProvider不要求uri的mimeType，则return null
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int code = URI_MATCHER.match(uri);
        if (code == URI_CODE_TABLE_TEST_INSERT) {
            myDBHelper.getWritableDatabase().insert(TABLE_NAME, null, values);
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.i(TAG, "ContentProvider : query, uri = " + uri);
        int code = URI_MATCHER.match(uri);
        Cursor cursor = null;
        if (code == URI_CODE_TABLE_TEST_QUERY) {
            cursor = myDBHelper.getWritableDatabase().query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        }
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = URI_MATCHER.match(uri);
        int result = 0;
        if (code == URI_CODE_TABLE_TEST_DELETE) {
            result = myDBHelper.getWritableDatabase().delete(TABLE_NAME, selection, selectionArgs);
        }
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int code = URI_MATCHER.match(uri);
        int result = 0;
        if (code == URI_CODE_TABLE_TEST_UPDATE) {
            result = myDBHelper.getWritableDatabase().update(TABLE_NAME, values, selection, selectionArgs);
        }
        return result;
    }
}
