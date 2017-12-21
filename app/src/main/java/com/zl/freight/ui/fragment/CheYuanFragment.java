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
 * @date 17/12/21
 */
public class CheYuanFragment extends BaseFragment {


    @BindView(R.id.che_yuan_tab)
    TabLayout cheYuanTab;
    Unbinder unbinder;
    @BindView(R.id.che_yuan_pager)
    ViewPager cheYuanPager;
    private List<String> mList = Arrays.asList("熟车", "找车");
    private FragmentStatePagerAdapter mAdapter;
    private List<Fragment> fList = new ArrayList<>();

    public CheYuanFragment() {
        // Required empty public constructor
    }

    public static CheYuanFragment newInstance() {

        Bundle args = new Bundle();

        CheYuanFragment fragment = new CheYuanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_che_yuan, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initData() {
        fList.add(CheYuanListFragment.newInstance(0));
        fList.add(CheYuanListFragment.newInstance(1));
        mAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        cheYuanPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(cheYuanTab));
        cheYuanTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                cheYuanPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initView() {

        mAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fList.get(position);
            }

            @Override
            public int getCount() {
                return fList.size();
            }
        };

        for (String s : mList) {
            TabLayout.Tab tab = cheYuanTab.newTab();
            tab.setText(s);
            cheYuanTab.addTab(tab);
        }

        cheYuanPager.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
