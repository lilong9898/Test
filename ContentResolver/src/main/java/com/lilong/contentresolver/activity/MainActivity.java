package com.lilong.contentresolver.activity;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.lilong.contentresolver.R;
import com.lilong.contentresolver.adapter.ContentProviderDataListAdapter;
import com.lilong.contentresolver.entity.ContentProviderDataItem;

import java.util.ArrayList;

public class MainActivity extends Activity {

    public static final String TAG = "CTest";

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

    private Button btnQueryAll;
    private Button btnClearAll;
    private ListView lvContentProviderData;
    private ContentProviderDataListAdapter contentProviderDataListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnQueryAll = findViewById(R.id.btnQueryAll);
        btnClearAll = findViewById(R.id.btnClearAll);
        lvContentProviderData = findViewById(R.id.lvData);
        contentProviderDataListAdapter = new ContentProviderDataListAdapter(this);
        ArrayList<ContentProviderDataItem> data = queryItems();
        contentProviderDataListAdapter.setData(data);
        lvContentProviderData.setAdapter(contentProviderDataListAdapter);
        btnQueryAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ContentProviderDataItem> data = queryItems();
                contentProviderDataListAdapter.setData(data);
                contentProviderDataListAdapter.notifyDataSetChanged();
            }
        });
        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ContentProviderDataItem> data = new ArrayList<>();
                contentProviderDataListAdapter.setData(data);
                contentProviderDataListAdapter.notifyDataSetChanged();
            }
        });
    }

    /** 查询contentprovider中test表中所有记录*/
    private ArrayList<ContentProviderDataItem> queryItems() {
        ArrayList<ContentProviderDataItem> result = new ArrayList<ContentProviderDataItem>();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content");
        builder.authority(CONTENT_PROVIDER_AUTHORITIES);
        builder.path("test/query");
        Uri uri = builder.build();

        // 如果要访问的其他应用的content provider没有设置exported=true的话, 这里会报异常
        // java.lang.SecurityException: Permission Denial: opening provider com.lilong.contentprovider.provider.MyContentProvider from ProcessRecord{ee9f470 27480:com.lilong.contentresolver/u0a844} (pid=27480, uid=10844) that is not exported from UID 10843
        Cursor c = getContentResolver().query(uri, null, null, null, null);

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
