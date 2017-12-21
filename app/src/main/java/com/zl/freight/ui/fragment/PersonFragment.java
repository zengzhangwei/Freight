package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.freight.R;
import com.zl.freight.ui.activity.EditPersonDataActivity;
import com.zl.freight.ui.activity.GoodsOrderActivity;
import com.zl.freight.ui.activity.InfoQueryActivity;
import com.zl.freight.ui.activity.JiFenStoreActivity;
import com.zl.freight.ui.activity.MyMoneyActivity;
import com.zl.freight.ui.activity.PublishNewsActivity;
import com.zl.freight.ui.activity.RoleChooseActivity;
import com.zl.freight.ui.activity.UserCheckActivity;
import com.zl.freight.utils.SpUtils;
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
    @BindView(R.id.arl_person)
    AutoRelativeLayout arlPerson;
    @BindView(R.id.linear_my_ji_fen_store)
    AutoLinearLayout linearMyJiFenStore;

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

    @OnClick({R.id.arl_person, R.id.linear_my_order, R.id.linear_my_money, R.id.linear_user_check,
            R.id.linear_info_query, R.id.linear_news_push, R.id.btn_exit, R.id.linear_my_ji_fen_store})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //个人信息
            case R.id.arl_person:
                startActivity(new Intent(mActivity, EditPersonDataActivity.class));
                break;
            //我的订单
            case R.id.linear_my_order:
                //TODO 在这进行司机和货主订单的区分
                startActivity(new Intent(mActivity, GoodsOrderActivity.class));
                break;
            //我的钱包
            case R.id.linear_my_money:
                startActivity(new Intent(mActivity, MyMoneyActivity.class));
                break;
            //积分商城
            case R.id.linear_my_ji_fen_store:
                startActivity(new Intent(mActivity, JiFenStoreActivity.class));
                break;
            //用户审核
            case R.id.linear_user_check:
                startActivity(new Intent(mActivity, UserCheckActivity.class));
                break;
            //统计查询
            case R.id.linear_info_query:
                startActivity(new Intent(mActivity, InfoQueryActivity.class));
                break;
            //发布新闻和广告
            case R.id.linear_news_push:
                startActivity(new Intent(mActivity, PublishNewsActivity.class));
                break;
            //退出当前帐号
            case R.id.btn_exit:
                //取消登录状态
                SpUtils.setIsLogin(mActivity, false);
                //取消用户角色
                SpUtils.setRole(mActivity, -1);
                startActivity(new Intent(mActivity, RoleChooseActivity.class));
                mActivity.finish();
                break;
        }
    }
}
