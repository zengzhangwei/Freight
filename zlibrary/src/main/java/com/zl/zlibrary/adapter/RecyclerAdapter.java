package com.zl.zlibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/11/19.
 */

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private List<T> list;
    private int layoutId;

    public RecyclerAdapter(Context context, List<T> list, int layoutId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.get(context, parent, layoutId);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    protected abstract void convert(ViewHolder holder, T t, int position);
}
