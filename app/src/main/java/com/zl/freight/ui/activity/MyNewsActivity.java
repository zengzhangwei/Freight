package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.ui.fragment.MyNewsFragment;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/30
 * 我的文章、新闻、广告
 */
public class MyNewsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.my_new_tab)
    TabLayout myNewTab;
    @BindView(R.id.my_new_pager)
    ViewPager myNewPager;
    private List<String> mList = Arrays.asList("全部", "未支付");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_news);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        myNewTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myNewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        myNewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myNewTab));
    }

    private void initData() {
        for (String s : mList) {
            myNewTab.addTab(myNewTab.newTab().setText(s));
        }
    }

    private void initView() {
        tvTitle.setText("我的文章与广告");
        getSupportFragmentManager().beginTransaction().replace(R.id.my_news_rl, MyNewsFragment.newInstance()).commit();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
