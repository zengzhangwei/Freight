package com.zl.freight.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.ui.fragment.NewPushFragment;
import com.zl.freight.ui.fragment.WebPushFragment;
import com.zl.zlibrary.utils.FragmentHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/9
 * 发布文章
 */
public class PublishNewsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.pn_tab)
    TabLayout pnTab;
    @BindView(R.id.pn_rl)
    RelativeLayout pnRl;
    private NewPushFragment newPushFragment;
    private WebPushFragment webPushFragment;
    private FragmentHelper fragmentHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_news);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int position = pnTab.getSelectedTabPosition();
        if (position == 0) {
            newPushFragment.onActivityResult(requestCode, resultCode, data);
        } else {
            webPushFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initListener() {
        pnTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragmentHelper.showFragment(newPushFragment);
                        break;
                    case 1:
                        fragmentHelper.showFragment(webPushFragment);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initData() {
        TabLayout.Tab tab1 = pnTab.newTab();
        TabLayout.Tab tab2 = pnTab.newTab();
        tab1.setText("文章编辑");
        tab2.setText("发布网页");
        pnTab.addTab(tab1);
        pnTab.addTab(tab2);
        newPushFragment = NewPushFragment.newInstance();
        webPushFragment = WebPushFragment.newInstance();
        fragmentHelper = FragmentHelper.builder(mActivity).attach(R.id.pn_rl)
                .addFragment(newPushFragment)
                .addFragment(webPushFragment).commit();
        fragmentHelper.showFragment(newPushFragment);
    }

    private void initView() {
        tvTitle.setText("文章发布");
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
