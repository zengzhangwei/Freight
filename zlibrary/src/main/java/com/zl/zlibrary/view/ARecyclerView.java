package com.zl.zlibrary.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by zhanglei on 2017/12/6.
 * 不对RecyclerView做任何调整
 * 只是为了向外提供可访问的原生RecyclerView
 */

public class ARecyclerView extends RecyclerView {
    public ARecyclerView(Context context) {
        super(context);
    }

    public ARecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ARecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
