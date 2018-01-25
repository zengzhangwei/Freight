package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.freight.R;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.CarUserBean;
import com.zl.freight.ui.activity.ADActivity;
import com.zl.freight.ui.activity.EditPersonDataActivity;
import com.zl.freight.ui.activity.FeedbackActivity;
import com.zl.freight.ui.activity.GoodsOrderActivity;
import com.zl.freight.ui.activity.InfoQueryActivity;
import com.zl.freight.ui.activity.JiFenStoreActivity;
import com.zl.freight.ui.activity.LoginActivity;
import com.zl.freight.ui.activity.MyMoneyActivity;
import com.zl.freight.ui.activity.MyNewsActivity;
import com.zl.freight.ui.activity.MyOrderActivity;
import com.zl.freight.ui.activity.UserCheckActivity;
import com.zl.freight.ui.activity.register.UserDataActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.utils.GsonUtils;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author zhanglei
 * @date 17/12/7
 * 用户中心
 */
public class PersonFragment extends BaseFragment {


    @BindView(R.id.user_icon)
    CircleImageView userIcon;
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
    @BindView(R.id.linear_my_news)
    AutoLinearLayout linearMyNews;
    @BindView(R.id.tv_feedback)
    TextView tvFeedback;
    @BindView(R.id.linear_user_feedback)
    AutoLinearLayout linearUserFeedback;
    @BindView(R.id.linear_real_user)
    AutoLinearLayout linearRealUser;
    @BindView(R.id.tv_my_order)
    TextView tvMyOrder;
    @BindView(R.id.linear_my_run_order)
    AutoLinearLayout linearMyRunOrder;
    @BindView(R.id.tv_check)
    TextView tvCheck;
    private BaseUserEntity userData;
    private int userRole;

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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            updateUi();
        }
    }

    /**
     * 更新积分
     */
    private void updateUi() {
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        Map<String, String> params = new HashMap<>();
        params.put("UserId", userData.getId());
        params.put("UserRole", userData.getUserRole());
        SoapUtils.Post(mActivity, API.ShowUserInfo, params, new SoapCallback() {

            @Override
            public void onError(String error) {
                showToast("用户信息获取失败，请新进入");
            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONArray array = new JSONArray(data);
                    CarUserBean carUserBean = GsonUtils.fromJson(array.optString(0), CarUserBean.class);
                    String realName = carUserBean.getRealName();
                    String userPic = carUserBean.getUserIcon();
                    if (carUserBean.getIsCheck().equals("1")) {
                        tvCheck.setText("实名认证（已认证）");
                        linearRealUser.setEnabled(false);
                    } else {
                        tvCheck.setText("实名认证（未通过认证）");
                        linearRealUser.setEnabled(true);
                    }
                    if (!TextUtils.isEmpty(realName)) {
                        tvShoujihao.setText(realName);
                    }
                    ImageLoader.loadUserIcon(mActivity, userPic, userIcon);
                } catch (Exception e) {

                }
            }
        });
    }

    private void initView() {
        userData = SpUtils.getUserData(mActivity);
        userRole = new Integer(TextUtils.isEmpty(userData.getUserRole()) ? "0" : userData.getUserRole());
        switch (userRole) {
            //管理员权限
            case 0:
                linearMyOrder.setVisibility(View.GONE);
                linearMyJiFenStore.setVisibility(View.GONE);
                linearMyMoney.setVisibility(View.GONE);
                linearMyNews.setVisibility(View.GONE);
                linearRealUser.setVisibility(View.GONE);
                tvFeedback.setText(R.string.user_feedback);
                break;
            //普通用户
            default:
                linearNewsPush.setVisibility(View.GONE);
                linearUserCheck.setVisibility(View.GONE);
                linearInfoQuery.setVisibility(View.GONE);
                linearRealUser.setVisibility(View.VISIBLE);
                tvFeedback.setText("意见反馈");
                break;
        }

        try {
            if (SpUtils.getUserData(mActivity).getUserRole().equals(API.DRIVER + "")) {
                tvMyOrder.setText("临时货物运单");
                linearMyRunOrder.setVisibility(View.VISIBLE);
            } else {
                linearMyRunOrder.setVisibility(View.GONE);
                tvMyOrder.setText("我的运单");
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.arl_person, R.id.linear_my_order, R.id.linear_my_money, R.id.linear_user_check,
            R.id.linear_info_query, R.id.linear_news_push, R.id.btn_exit, R.id.linear_my_ji_fen_store,
            R.id.linear_my_news, R.id.linear_real_user, R.id.linear_user_feedback, R.id.linear_my_run_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //个人信息
            case R.id.arl_person:
                startActivity(new Intent(mActivity, EditPersonDataActivity.class));
                break;
            //我的订单
            case R.id.linear_my_order:
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
                startActivity(new Intent(mActivity, ADActivity.class));
                break;
            //退出当前帐号
            case R.id.btn_exit:
                //取消登录状态
                SpUtils.setIsLogin(mActivity, false);
                Intent intent = new Intent(mActivity, LoginActivity.class);
                intent.putExtra("role", SpUtils.getRole(mActivity));
                startActivity(intent);
                mActivity.finish();
                break;
            //进入我的文章与广告
            case R.id.linear_my_news:
                startActivity(new Intent(mActivity, MyNewsActivity.class));
                break;
            //用户反馈
            case R.id.linear_user_feedback:
                startActivity(new Intent(mActivity, FeedbackActivity.class));
                break;
            //用户实名认证
            case R.id.linear_real_user:
                startActivity(new Intent(mActivity, UserDataActivity.class));
                break;
            //司机的运单
            case R.id.linear_my_run_order:
                startActivity(new Intent(mActivity, MyOrderActivity.class));
                break;
        }
    }

}
