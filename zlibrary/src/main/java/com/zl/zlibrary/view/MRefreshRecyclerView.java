package com.zl.zlibrary.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/8/25.
 */

public class MRefreshRecyclerView extends SwipeRefreshLayout {

    private MRecyclerView mRecyclerView;

    public MRefreshRecyclerView(Context context) {
        super(context, null);
    }

    public MRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mRecyclerView = new MRecyclerView(context);
        mRecyclerView.setLayoutParams(params);
        mRecyclerView.setOnBottomListener(new MRecyclerView.OnBottomListener() {
            @Override
            public void onBottom() {
                if (onMRefreshListener != null && mRecyclerView.getHeight() > 0) {
                    setRefreshing(true);
                    onMRefreshListener.onLoadMore();
                }
            }
        });
        addView(mRecyclerView);
        this.setOnRefreshListener(onRefreshListener);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);
    }

    private OnRefreshListener onRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (onMRefreshListener != null) {
                onMRefreshListener.onRefresh();
            }
        }
    };

    private OnMRefreshListener onMRefreshListener;

    public void setOnMRefreshListener(OnMRefreshListener onMRefreshListener) {
        this.onMRefreshListener = onMRefreshListener;
    }

    public interface OnMRefreshListener {
        void onRefresh();

        void onLoadMore();
    }
}
