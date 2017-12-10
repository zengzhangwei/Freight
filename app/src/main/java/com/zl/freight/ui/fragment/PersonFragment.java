package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoLinearLayout;
import com.zl.freight.R;
import com.zl.freight.ui.activity.PublishNewsActivity;
import com.zl.zlibrary.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 17/12/7
 * 用户中心
 */
public class PersonFragment extends BaseFragment {


    @BindView(R.id.user_icon)
    ImageView userIcon;
    @BindView(R.id.tv_shoujihao)
    TextView tvShoujihao;
    @BindView(R.id.linear_my_order)
    AutoLinearLayout linearMyOrder;
    @BindView(R.id.linear_my_money)
    AutoLinearLayout linearMyMoney;
    @BindView(R.id.linear_user_check)
    AutoLinearLayout linearUserCheck;
    @BindView(R.id.linear_info_query)
    AutoLinearLayout linearInfoQuery;
    @BindView(R.id.linear_news_push)
    AutoLinearLayout linearNewsPush;
    @BindView(R.id.btn_exit)
    TextView btnExit;
    Unbinder unbinder;

    public PersonFragment() {
        // Required empty public constructor
    }

    public static PersonFragment newInstance() {

        Bundle args = new Bundle();

        PersonFragment fragment = new PersonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initListener() {

    }

    private void initData() {

    }

    private void initView() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.linear_my_order, R.id.linear_my_money, R.id.linear_user_check, R.id.linear_info_query, R.id.linear_news_push})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //我的订单
            case R.id.linear_my_order:

                break;
            //我的钱包
            case R.id.linear_my_money:

                break;
            //用户审核
            case R.id.linear_user_check:

                break;
            //统计查询
            case R.id.linear_info_query:

                break;
            //发布新闻和广告
            case R.id.linear_news_push:
                startActivity(new Intent(mActivity, PublishNewsActivity.class));
                break;
        }
    }
}
