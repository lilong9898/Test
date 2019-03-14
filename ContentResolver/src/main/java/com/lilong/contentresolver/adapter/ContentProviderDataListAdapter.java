package com.lilong.contentresolver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lilong.contentresolver.R;
import com.lilong.contentresolver.entity.ContentProviderDataItem;

import java.util.ArrayList;

public class ContentProviderDataListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ContentProviderDataItem> data;

    public ContentProviderDataListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<ContentProviderDataItem> data) {
        this.data = data;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ContentProviderDataItem dataItem = data.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_data_item, parent, false);
            holder = new ViewHolder();
            holder.tvId = convertView.findViewById(R.id.tvId);
            holder.tvProduct = convertView.findViewById(R.id.tvProduct);
            holder.tvPrice = convertView.findViewById(R.id.tvPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvId.setText(dataItem.id + "");
        holder.tvProduct.setText("" + dataItem.product);
        holder.tvPrice.setText("" + dataItem.price);
        return convertView;
    }

    static class ViewHolder {
        public TextView tvId;
        public TextView tvProduct;
        public TextView tvPrice;
    }
}
