package com.zhouyou.blurred.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhouyou.blurred.R;

/**
 * 作者：ZhouYou
 * 日期：2016/8/16.
 */
public class ListViewAdapter extends BaseAdapter {

    private Activity activity;

    public ListViewAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_view, null);
        }
        return convertView;
    }
}
