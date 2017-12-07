package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.ui.fragment.FindGoodsFragment;
import com.zl.freight.ui.fragment.PersonFragment;
import com.zl.freight.ui.fragment.SendGoodsFragment;
import com.zl.freight.ui.fragment.StoreFragment;
import com.zl.freight.ui.fragment.TopLineFragment;
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
    private FragmentHelper helper;
    //找货
    private FindGoodsFragment findGoodsFragment;
    //用户
    private PersonFragment personFragment;
    //发货
    private SendGoodsFragment sendGoodsFragment;
    //商城
    private StoreFragment storeFragment;
    //头条
    private TopLineFragment topLineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initListener() {
        mainBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.top_line:
                        helper.showFragment(topLineFragment);
                        break;
                    case R.id.send_goods:
                        helper.showFragment(sendGoodsFragment);
                        break;
                    case R.id.find_goods:
                        helper.showFragment(findGoodsFragment);
                        break;
                    case R.id.store:
                        helper.showFragment(storeFragment);
                        break;
                    case R.id.person:
                        helper.showFragment(personFragment);
                        break;
                }
                return true;
            }
        });
    }

    private void initView() {
        findGoodsFragment = FindGoodsFragment.newInstance();
        personFragment = PersonFragment.newInstance();
        sendGoodsFragment = SendGoodsFragment.newInstance();
        storeFragment = StoreFragment.newInstance();
        topLineFragment = TopLineFragment.newInstance();
        helper = FragmentHelper.builder(mActivity).attach(R.id.main_rl)
                .addFragment(findGoodsFragment)
                .addFragment(personFragment)
                .addFragment(sendGoodsFragment)
                .addFragment(storeFragment)
                .addFragment(topLineFragment)
                .commit();
        helper.showFragment(findGoodsFragment);
        mainBottom.setSelectedItemId(R.id.find_goods);
    }

    @OnClick({R.id.main_usericon, R.id.main_img_share, R.id.main_img_weChat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //进入用户中心页面
            case R.id.main_usericon:

                break;
            //分享应用
            case R.id.main_img_share:

                break;
            //关注微信公众号
            case R.id.main_img_weChat:

                break;
        }
    }
}
