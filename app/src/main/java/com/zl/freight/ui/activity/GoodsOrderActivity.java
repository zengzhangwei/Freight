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
import com.zl.freight.ui.fragment.GoodsOrderListFragment;
import com.zl.freight.ui.fragment.MyNewsFragment;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SpUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/15
 * 货主的订单页
 */
public class GoodsOrderActivity extends BaseActivity {

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
    private List<String> mList = Arrays.asList("未完成", "已完成");
    private List<Fragment> fList = new ArrayList<>();
    private FragmentStatePagerAdapter pagerAdapter;

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
        fList.add(GoodsOrderListFragment.newInstance(0));
        fList.add(GoodsOrderListFragment.newInstance(1));
        pagerAdapter.notifyDataSetChanged();
    }

    private void initView() {
        if (SpUtils.getUserData(mActivity).getUserRole().equals(API.DRIVER + "")) {
            tvTitle.setText("临时货物运单");
        } else {
            tvTitle.setText("我的运单");
        }
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
}
