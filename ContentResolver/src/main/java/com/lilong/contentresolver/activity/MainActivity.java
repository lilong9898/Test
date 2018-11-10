package com.lilong.contentresolver.activity;

import com.lilong.contentresolver.R;
import com.lilong.contentresolver.adapter.ContentProviderDataListAdapter;
import com.lilong.contentresolver.entity.ContentProviderDataItem;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    /**
     * contentProvider的authorities
     */
    private static final String CONTENT_PROVIDER_AUTHORITIES = "com.lilong.contentprovider.provider.MyContentProvider";
    /**
     * 本demo操作的contentProvider中的表名
     */
    private static final String TABLE_NAME = "test";
    /**
     * test表中的字段名
     */
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_PRODUCT = "product";
    private static final String COLUMN_NAME_PRICE = "price";

    private ListView lvContentProviderData;
    private ContentProviderDataListAdapter contentProviderDataListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvContentProviderData = findViewById(R.id.lvData);
        contentProviderDataListAdapter = new ContentProviderDataListAdapter(this);
        ArrayList<ContentProviderDataItem> data = queryItems();
        contentProviderDataListAdapter.setData(data);
        lvContentProviderData.setAdapter(contentProviderDataListAdapter);
    }

    /** 查询contentprovider中test表中所有记录*/
    private ArrayList<ContentProviderDataItem> queryItems() {
        ArrayList<ContentProviderDataItem> result = new ArrayList<ContentProviderDataItem>();
//        Uri uri = Uri.parse("content://" + CONTENT_PROVIDER_AUTHORITIES + "/" + TABLE_NAME + "/query");
//        Uri uri = Uri.parse("content://com.lilong.contentprovider.provider.MyContentProvider/test/query");
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content");
        builder.authority(CONTENT_PROVIDER_AUTHORITIES);
        builder.path("test/query");
        Uri uri = builder.build();
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        ContentProviderClient client = getContentResolver().acquireContentProviderClient(uri);

        if (c == null) {
            return result;
        }

        while (c.moveToNext()) {
            ContentProviderDataItem item = new ContentProviderDataItem();
            item.id = c.getInt(c.getColumnIndex(COLUMN_NAME_ID));
            item.product = c.getString(c.getColumnIndex(COLUMN_NAME_PRODUCT));
            item.price = c.getInt(c.getColumnIndex(COLUMN_NAME_PRICE));
            result.add(item);
        }

        c.close();
        return result;
    }
}
