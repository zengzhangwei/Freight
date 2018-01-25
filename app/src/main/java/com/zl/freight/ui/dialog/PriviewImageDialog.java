package com.zl.freight.ui.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.zl.freight.R;
import com.zl.freight.utils.ImageLoader;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.base.BaseDialog;

import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class PriviewImageDialog extends BaseDialog {

    private List<String> imgs;
    private GridView gridView;
    private UniversalAdapter<String> mAdapter;

    public PriviewImageDialog(Activity mActivity, List<String> imgs) {
        super(mActivity);
        this.imgs = imgs;
        initView();
    }

    private void initView() {
        mAdapter = new UniversalAdapter<String>(mActivity, imgs, R.layout.image_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                ImageView view = holder.getView(R.id.iv_item);
                ImageLoader.loadImageUrl(mActivity, s, view);
            }
        };
        View view = LayoutInflater.from(mActivity).inflate(R.layout.priview_icon_grid, null);
        gridView = view.findViewById(R.id.img_grid);
        gridView.setAdapter(mAdapter);
        initPopupWindow(view);
    }
}
