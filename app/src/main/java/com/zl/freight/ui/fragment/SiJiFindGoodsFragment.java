package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zl.freight.R;
import com.zl.zlibrary.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 18/2/7
 * 司机的找货页面
 */
public class SiJiFindGoodsFragment extends BaseFragment {


    @BindView(R.id.iv_into_order)
    ImageView ivIntoOrder;
    @BindView(R.id.main_rb_send)
    RadioButton mainRbSend;
    @BindView(R.id.main_rb_my_goods)
    RadioButton mainRbMyGoods;
    @BindView(R.id.main_rb_often)
    RadioButton mainRbOften;
    @BindView(R.id.main_rg)
    RadioGroup mainRg;
    @BindView(R.id.iv_main_share)
    ImageView ivMainShare;
    @BindView(R.id.main_appbar)
    AppBarLayout mainAppbar;
    @BindView(R.id.find_goods_viewpager)
    ViewPager findGoodsViewpager;
    Unbinder unbinder;
    private List<Fragment> mList = new ArrayList<>();
    private FragmentStatePagerAdapter mAdapter;

    public SiJiFindGoodsFragment() {
        // Required empty public constructor
    }

    public static SiJiFindGoodsFragment newInstance() {

        Bundle args = new Bundle();

        SiJiFindGoodsFragment fragment = new SiJiFindGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_si_ji_find_goods, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        mainRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    //订阅路线
                    case R.id.main_rb_send:
                        findGoodsViewpager.setCurrentItem(0);
                        break;
                    //货源搜索
                    case R.id.main_rb_my_goods:
                        findGoodsViewpager.setCurrentItem(1);
                        break;
                }
            }
        });
        findGoodsViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mainRg.check(R.id.main_rb_send);
                } else {
                    mainRg.check(R.id.main_rb_my_goods);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        mList.add(AddPathFragment.newInstance());
        mList.add(FindGoodsFragment.newInstance());
        mAdapter.notifyDataSetChanged();
        findGoodsViewpager.setCurrentItem(1);
    }

    private void initView() {
        mAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mList.get(position);
            }

            @Override
            public int getCount() {
                return mList.size();
            }
        };

        findGoodsViewpager.setAdapter(mAdapter);
        mainRg.check(R.id.main_rb_my_goods);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_into_order)
    public void onViewClicked() {
    }
}
