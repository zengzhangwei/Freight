package com.zl.freight.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
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

    @BindView(R.id.pn_rl)
    RelativeLayout pnRl;
    @BindView(R.id.iv_uc_back)
    ImageView ivUcBack;
    @BindView(R.id.tv_uc_title)
    TextView tvUcTitle;
    @BindView(R.id.uc_tab)
    TabLayout ucTab;
    private NewPushFragment newPushFragment;
    private WebPushFragment webPushFragment;
    private FragmentHelper fragmentHelper;
    private AlertDialog alertDialog;

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
        int position = ucTab.getSelectedTabPosition();
        if (position == 0) {
            newPushFragment.onActivityResult(requestCode, resultCode, data);
        } else {
            webPushFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initListener() {
        ucTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        TabLayout.Tab tab1 = ucTab.newTab();
        TabLayout.Tab tab2 = ucTab.newTab();
        tab1.setText("文章编辑");
        tab2.setText("发布网页");
        ucTab.addTab(tab1);
        ucTab.addTab(tab2);
        newPushFragment = NewPushFragment.newInstance();
        webPushFragment = WebPushFragment.newInstance();
        fragmentHelper = FragmentHelper.builder(mActivity).attach(R.id.pn_rl)
                .addFragment(newPushFragment)
                .addFragment(webPushFragment).commit();
        fragmentHelper.showFragment(newPushFragment);
    }

    private void initView() {
        tvUcTitle.setText("文章发布");
        alertDialog = new AlertDialog.Builder(mActivity).setMessage("确认放弃编辑吗，退出将不会保存").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).create();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            alertDialog.show();
        }
        return true;
    }

    @OnClick(R.id.iv_uc_back)
    public void onViewClicked() {
        alertDialog.show();
    }
}
