package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.ui.fragment.MyNewsFragment;
import com.zl.freight.ui.fragment.TopUpFragment;

import java.util.ArrayList;
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
public class MyNewsActivity extends BaseActivity implements MyNewsFragment.OnOpenMyNewsListener, TopUpFragment.OnPayListener {

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
    private List<String> mList = Arrays.asList("未支付", "已支付");
    private List<Fragment> fList = new ArrayList<>();
    private FragmentStatePagerAdapter pagerAdapter;
    private MyNewsFragment myNewsFragment;

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
        myNewsFragment = MyNewsFragment.newInstance(0);
        myNewsFragment.setOnOpenMyNewsListener(this);
        fList.add(myNewsFragment);
        fList.add(MyNewsFragment.newInstance(1));
        pagerAdapter.notifyDataSetChanged();
    }

    private void initView() {
        tvTitle.setText("我的文章与广告");
        pagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fList.get(position);
            }

            @Override
            public int getCount() {
                return fList.size();
            }
        };
        myNewPager.setAdapter(pagerAdapter);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 去充值
     */
    @Override
    public void openMyNews() {
        TopUpFragment topUpFragment = new TopUpFragment();
        topUpFragment.setOnPayListener(this);
        getSupportFragmentManager().beginTransaction().addToBackStack("topup")
                .replace(R.id.my_news_rl, topUpFragment).commit();
    }

    @Override
    public void onPaySuccess(int money) {
        showToast("成功充值：" + money + "元");
    }

    @Override
    public void onPayError(String message) {
        showToast(message);
    }
}
