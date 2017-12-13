package com.zl.freight.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.utils.ImageLoader;

/**
 * @author zhanglei
 * @date 17/12/13
 * 图片预览页
 */
public class PictureActivity extends BaseActivity {

    public static final String PICTURE = "picture";
    private ImageView img;
    private Toolbar toolbar;
    private boolean isShow = false;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        initView();
        initListener();
    }

    protected void initView() {
        toolbar = getView(R.id.picture_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        setSupportActionBar(toolbar);
        img = getView(R.id.picture_img);
        ViewCompat.setTransitionName(img, PICTURE);
        url = getIntent().getStringExtra("url");
        ImageLoader.loadImageUrl(mActivity, url, img);
    }

    protected void initData() {

    }

    protected void initListener() {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.animate().translationY(isShow ? 0 : -toolbar.getHeight()).setDuration(200).start();
                isShow = !isShow;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
    }

}
