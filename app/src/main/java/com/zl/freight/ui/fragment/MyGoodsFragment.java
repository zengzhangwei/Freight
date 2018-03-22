package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zl.freight.R;
import com.zl.zlibrary.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 17/12/28
 * 我的货源
 */
public class MyGoodsFragment extends BaseFragment {


    @BindView(R.id.my_goods_tab)
    TabLayout myGoodsTab;
    @BindView(R.id.my_goods_pager)
    ViewPager myGoodsPager;
    Unbinder unbinder;
    private List<String> mList = Arrays.asList("发布中", "已删除", "已关闭");
    private List<Fragment> fList = new ArrayList<>();
    private FragmentStatePagerAdapter pagerAdapter;

    public MyGoodsFragment() {
        // Required empty public constructor
    }

    public static MyGoodsFragment newInstance() {

        Bundle args = new Bundle();

        MyGoodsFragment fragment = new MyGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_goods, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        myGoodsPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myGoodsTab));
        myGoodsTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myGoodsPager.setCurrentItem(tab.getPosition());
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
        for (String s : mList) {
            myGoodsTab.addTab(myGoodsTab.newTab().setText(s));
        }
        fList.add(MyGoodsListFragment.newInstance(0));
        fList.add(MyGoodsListFragment.newInstance(1));
        fList.add(MyGoodsListFragment.newInstance(2));
        pagerAdapter.notifyDataSetChanged();
    }

    private void initView() {
        pagerAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fList.get(position);
            }

            @Override
            public int getCount() {
                return fList.size();
            }
        };
        myGoodsPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
