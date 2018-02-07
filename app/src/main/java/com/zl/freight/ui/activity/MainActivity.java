package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.KeyValueBean;
import com.zl.freight.service.LocationService;
import com.zl.freight.ui.dialog.CarLengthDialog;
import com.zl.freight.ui.dialog.MessageDialog;
import com.zl.freight.ui.fragment.FindGoodsFragment;
import com.zl.freight.ui.fragment.MyGoodsFragment;
import com.zl.freight.ui.fragment.NoLoginPersonFragment;
import com.zl.freight.ui.fragment.PersonFragment;
import com.zl.freight.ui.fragment.SendGoodsFragment;
import com.zl.freight.ui.fragment.TopLineFragment;
import com.zl.freight.utils.API;
import com.zl.freight.utils.BottomNavigationViewHelper;
import com.zl.freight.utils.ShareUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.utils.FragmentHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author zhanglei
 * @date 17/12/06
 * 车主版主页
 */
public class MainActivity extends BaseActivity {

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
    @BindView(R.id.rl_title)
    AutoRelativeLayout rlTitle;
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
    private MyGoodsFragment myGoodsFragment;
    private MessageDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    case R.id.top_line:
                        helper.showFragment(topLineFragment);
                        break;
                    case R.id.send_goods:
                        mainRg.check(R.id.main_rb_send);
                        helper.showFragment(sendGoodsFragment);
                        break;
                    case R.id.find_goods:
                        helper.showFragment(findGoodsFragment);
                        break;
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
                }
            }
        });
    }

    private void initView() {
        findGoodsFragment = FindGoodsFragment.newInstance();
        personFragment = PersonFragment.newInstance();
        sendGoodsFragment = SendGoodsFragment.newInstance();
        topLineFragment = TopLineFragment.newInstance();
        noLoginPersonFragment = NoLoginPersonFragment.newInstance();
        myGoodsFragment = MyGoodsFragment.newInstance();
        helper = FragmentHelper.builder(mActivity).attach(R.id.main_rl)
                .addFragment(findGoodsFragment)
                .addFragment(personFragment)
                .addFragment(sendGoodsFragment)
                .addFragment(topLineFragment)
                .addFragment(noLoginPersonFragment)
                .addFragment(myGoodsFragment)
                .commit();
        helper.showFragment(findGoodsFragment);
        mainBottom.setSelectedItemId(R.id.find_goods);
        //开启定位服务，上报司机位置
        if (SpUtils.isLogin(mActivity)) {
            startService(new Intent(mActivity, LocationService.class));
        }

        //默认选中发货
        mainRg.check(R.id.main_rb_send);
        //审核状态的对话框
        alertDialog = new MessageDialog(mActivity);

        BottomNavigationViewHelper.disableShiftMode(mainBottom);
    }

    @OnClick({R.id.main_usericon, R.id.main_img_share, R.id.iv_into_order, R.id.main_img_weChat})
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
            //进入司机运单页
            case R.id.iv_into_order:
                startActivity(new Intent(mActivity, GoodsOrderActivity.class));
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long time = System.currentTimeMillis();
            if (time - timecode > 2000) {
                showToast("再次点击退出程序");
                timecode = time;
            } else {
                finish();
            }
        }
        return true;
    }
}
