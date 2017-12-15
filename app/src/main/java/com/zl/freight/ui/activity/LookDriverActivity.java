package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 12/12/15
 * 查看司机位置
 */
public class LookDriverActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.lda_map)
    MapView ldaMap;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_driver);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mBaiduMap = ldaMap.getMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ldaMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ldaMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ldaMap.onDestroy();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
