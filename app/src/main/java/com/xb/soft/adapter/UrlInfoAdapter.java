package com.xb.soft.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xb.soft.R;

import java.util.List;

public class UrlInfoAdapter extends BaseAdapter {
    private final List<String> list;
    private final Context context;

    public UrlInfoAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int count = 0;
        count = i + 1;
        ViewHolder mHolder;
        if (view == null) {
            mHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_url, null, true);
            mHolder.tv_title = view.findViewById(R.id.tv_title);
            mHolder.tv_info = view.findViewById(R.id.tv_info);
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        mHolder.tv_title.setText("线路" + count);
        mHolder.tv_info.setText(list.get(i));
        return view;
    }

    private class ViewHolder {
        private TextView tv_title;
        private TextView tv_info;
    }
}
