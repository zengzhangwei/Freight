package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.ui.fragment.UserCheckListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/12
 * 用户审核
 */
public class UserCheckActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.uc_tab)
    TabLayout ucTab;
    @BindView(R.id.uc_rl)
    RelativeLayout ucRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        getSupportFragmentManager().beginTransaction().replace(R.id.uc_rl, UserCheckListFragment.newInstance(0)).commit();
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
        ucTab.addTab(tab1);
        ucTab.addTab(tab2);
        ucTab.addTab(tab3);
        ucTab.addTab(tab4);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
