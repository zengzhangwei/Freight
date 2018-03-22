package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.KeyValueBean;
import com.zl.freight.ui.dialog.CarLengthDialog;
import com.zl.freight.ui.dialog.DriverSearchDialog;
import com.zl.freight.ui.dialog.MessageDialog;
import com.zl.freight.ui.fragment.CheYuanFragment;
import com.zl.freight.ui.fragment.FindGoodsFragment;
import com.zl.freight.ui.fragment.MyGoodsFragment;
import com.zl.freight.ui.fragment.NoLoginPersonFragment;
import com.zl.freight.ui.fragment.OftenFragment;
import com.zl.freight.ui.fragment.PersonFragment;
import com.zl.freight.ui.fragment.SendGoodsFragment;
import com.zl.freight.ui.fragment.TopLineFragment;
import com.zl.freight.ui.fragment.WebFragment;
import com.zl.freight.utils.API;
import com.zl.freight.utils.BottomNavigationViewHelper;
import com.zl.freight.utils.ShareUtils;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.utils.FragmentHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsMainActivity extends BaseActivity {

    @BindView(R.id.main_usericon)
    ImageView mainUsericon;
    @BindView(R.id.main_username)
    TextView mainUsername;
    @BindView(R.id.main_img_share)
    ImageView mainImgShare;
    @BindView(R.id.main_img_weChat)
    ImageView mainImgWeChat;
    @BindView(R.id.main_bottom)
    BottomNavigationView mainBottom;
    @BindView(R.id.main_title)
    AppBarLayout mainTitle;
    @BindView(R.id.main_rl)
    RelativeLayout mainRl;
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
    private FragmentHelper helper;
    //找货
    private FindGoodsFragment findGoodsFragment;
    //用户
    private PersonFragment personFragment;
    //发货
    private SendGoodsFragment sendGoodsFragment;
    //头条
    private TopLineFragment topLineFragment;
    private NoLoginPersonFragment noLoginPersonFragment;
    private long timecode = 0;
    private CheYuanFragment cheYuanFragment;
    private DriverSearchDialog searchDialog;
    private MyGoodsFragment myGoodsFragment;
    private OftenFragment oftenFragment;
    private MessageDialog alertDialog;
    private WebFragment webFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_main);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
        upLoadRegId();
    }

    private void initData() {
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        if (!userData.getIsCheck().equals("1")) {
            alertDialog.show();
        }
//        Map<String, String> params = new HashMap<>();
//        params.put("UserName", SpUtils.getUserData(mActivity).getUserName());
//        SoapUtils.Post(mActivity, API.SaveCheck, params, new SoapCallback() {
//            @Override
//            public void onError(String error) {
//
//                SpUtils.setIsReal(mActivity,false);
//            }
//
//            @Override
//            public void onSuccess(String data) {
//                SpUtils.setIsReal(mActivity,true);
//                alertDialog.show();
//            }
//        });
    }

    private void initListener() {
        mainBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.send_goods) {
                    mainTitle.setVisibility(View.GONE);
                    mainAppbar.setVisibility(View.VISIBLE);
                } else {
                    mainTitle.setVisibility(View.VISIBLE);
                    mainAppbar.setVisibility(View.GONE);
                }

                switch (item.getItemId()) {

//                    //头条
//                    case R.id.top_line:
//                        helper.showFragment(topLineFragment);
//                        break;
                    //保险
                    case R.id.bao_xian:
                        helper.showFragment(webFragment);
                        break;
                    //发货
                    case R.id.send_goods:
                        mainRg.check(R.id.main_rb_send);
                        helper.showFragment(sendGoodsFragment);
                        break;
                    //找货
                    case R.id.find_goods:
                        helper.showFragment(findGoodsFragment);
                        break;
                    //车源
                    case R.id.che_yuan:
                        helper.showFragment(cheYuanFragment);
                        break;
                    //个人中心
                    case R.id.person:
                        if (!SpUtils.isLogin(mActivity)) {
                            helper.showFragment(noLoginPersonFragment);
                        } else {
                            helper.showFragment(personFragment);
                        }
                        break;
                }
                return true;
            }
        });

        mainRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    //发货
                    case R.id.main_rb_send:
                        helper.showFragment(sendGoodsFragment);
                        break;
                    //我的货源
                    case R.id.main_rb_my_goods:
                        helper.showFragment(myGoodsFragment);
                        break;
                    //常发货源
                    case R.id.main_rb_often:
                        helper.showFragment(oftenFragment);
                        break;
                }
            }
        });

    }

    private void initView() {
        findGoodsFragment = FindGoodsFragment.newInstance();
        personFragment = PersonFragment.newInstance();
        sendGoodsFragment = SendGoodsFragment.newInstance();
//        topLineFragment = TopLineFragment.newInstance();
        noLoginPersonFragment = NoLoginPersonFragment.newInstance();
        cheYuanFragment = CheYuanFragment.newInstance();
        myGoodsFragment = MyGoodsFragment.newInstance();
        oftenFragment = OftenFragment.newInstance();
        webFragment = WebFragment.newInstance("https://www.baidu.com/");
        helper = FragmentHelper.builder(mActivity).attach(R.id.main_rl)
                .addFragment(findGoodsFragment)
                .addFragment(personFragment)
                .addFragment(sendGoodsFragment)
//                .addFragment(topLineFragment)
                .addFragment(noLoginPersonFragment)
                .addFragment(webFragment)
                .addFragment(cheYuanFragment)
                .addFragment(myGoodsFragment)
                .addFragment(oftenFragment)
                .commit();
        helper.showFragment(sendGoodsFragment);
        mainBottom.setSelectedItemId(R.id.send_goods);
        searchDialog = new DriverSearchDialog(mActivity);
        mainTitle.setVisibility(View.GONE);
        mainAppbar.setVisibility(View.VISIBLE);
        //默认选中发货
        mainRg.check(R.id.main_rb_send);
        //审核状态的对话框
        alertDialog = new MessageDialog(mActivity);

        BottomNavigationViewHelper.disableShiftMode(mainBottom);
    }

    @OnClick({R.id.main_usericon, R.id.main_img_share, R.id.main_img_weChat, R.id.iv_into_order, R.id.iv_main_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //进入用户中心页面
            case R.id.main_usericon:

                break;
            //分享应用
            case R.id.main_img_share:
                ShareUtils.share(mActivity);
                break;
            //关注微信公众号
            case R.id.main_img_weChat:

                break;
            //显示车长车宽选择器
            case R.id.tv_car_type:

                break;
            //进入货主运单页
            case R.id.iv_into_order:
                startActivity(new Intent(mActivity, GoodsOrderActivity.class));
                break;
            //分享
            case R.id.iv_main_share:
                ShareUtils.share(mActivity);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //从登录页返回到主界面
                case API.ISLOGIN:
                    if (!noLoginPersonFragment.isHidden()) {
                        if (!SpUtils.isLogin(mActivity)) {
                            helper.showFragment(noLoginPersonFragment);
                        } else {
                            helper.showFragment(personFragment);
                        }
                    }
                    break;
                default:
                    sendGoodsFragment.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            long time = System.currentTimeMillis();
//            if (time - timecode > 2000) {
//                showToast("再次点击退出程序");
//                timecode = time;
//            } else {
//                finish();
//            }
//        }
//        return true;
//    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mActivity.startActivity(intent);
        }
        return true;
    }
}
