package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.ui.fragment.UserCheckListFragment;
import com.zl.zlibrary.utils.FragmentHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/12
 * 用户审核
 */
public class UserCheckActivity extends BaseActivity {

    @BindView(R.id.uc_tab)
    TabLayout ucTab;
    @BindView(R.id.uc_rl)
    RelativeLayout ucRl;
    @BindView(R.id.iv_uc_back)
    ImageView ivUcBack;
    @BindView(R.id.tv_uc_title)
    TextView tvUcTitle;
    private UserCheckListFragment fragment0, fragment1, fragment2, fragment3;
    private FragmentHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        ucTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        helper.showFragment(fragment0);
                        break;
                    case 1:
                        helper.showFragment(fragment1);
                        break;
                    case 2:
                        helper.showFragment(fragment2);
                        break;
                    case 3:
                        helper.showFragment(fragment3);
                        break;
                }
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
        tvUcTitle.setText(R.string.user_check);
        fragment0 = UserCheckListFragment.newInstance(0);
        fragment1 = UserCheckListFragment.newInstance(0);
        fragment2 = UserCheckListFragment.newInstance(0);
        fragment3 = UserCheckListFragment.newInstance(0);
        helper = FragmentHelper.builder(mActivity).attach(R.id.uc_rl)
                .addFragment(fragment0)
                .addFragment(fragment1)
                .addFragment(fragment2)
                .addFragment(fragment3)
                .commit();
        helper.showFragment(fragment0);
    }

    private void initData() {
        TabLayout.Tab tab1 = ucTab.newTab();
        TabLayout.Tab tab2 = ucTab.newTab();
        TabLayout.Tab tab3 = ucTab.newTab();
        TabLayout.Tab tab4 = ucTab.newTab();
        tab1.setText("全部");
        tab2.setText("审核中");
        tab3.setText("已通过");
        tab4.setText("未通过");
        ucTab.addTab(tab1, true);
        ucTab.addTab(tab2, false);
        ucTab.addTab(tab3, false);
        ucTab.addTab(tab4, false);
    }

    @OnClick(R.id.iv_uc_back)
    public void onViewClicked() {
        finish();
    }
}
