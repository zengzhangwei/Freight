package com.zl.zlibrary.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/8/25.
 */

public class MRecyclerView extends RecyclerView {
    public MRecyclerView(Context context) {
        super(context);
    }

    public MRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (isSlideToBottom()) {
            if (onBottomListener != null) {
                onBottomListener.onBottom();
            }
        }

//        if (!canScrollVertically(1)) {
//            if (onBottomListener != null) {
//                onBottomListener.onBottom();
//            }
//        }
    }

    private boolean isSlideToBottom() {
        if (this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset()
                >= this.computeVerticalScrollRange())
            return true;
        return false;
    }


    private OnBottomListener onBottomListener;

    public void setOnBottomListener(OnBottomListener onBottomListener) {
        this.onBottomListener = onBottomListener;
    }

    public interface OnBottomListener {
        void onBottom();
    }
}

