package com.zl.zlibrary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by zhanglei on 2016/12/15.
 */

public abstract class UniversalAdapter<T> extends BaseAdapter {

    private Context context;
    private List<T> list;
    private int layoutId;

    public UniversalAdapter(Context context, List<T> list, int layoutId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UniversalViewHolder holder = UniversalViewHolder.get(convertView, parent, context, layoutId);
        convert(holder, position, list.get(position));

        return holder.getConvertView();
    }

    public abstract void convert(UniversalViewHolder holder, int position, T t);

}
