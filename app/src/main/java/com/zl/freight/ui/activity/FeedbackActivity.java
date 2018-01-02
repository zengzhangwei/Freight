package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.ui.fragment.FeedbackFragment;
import com.zl.freight.ui.fragment.FeedbackListFragment;
import com.zl.freight.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 18/1/2
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private BaseUserEntity userData;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        userData = SpUtils.getUserData(mActivity);
        transaction = getSupportFragmentManager().beginTransaction();
        openFeedback();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 反馈方法
     */
    private void openFeedback() {
        boolean equals = userData.getUserRole().equals("0");
        if (equals) {//管理员权限，查看用户反馈
            tvTitle.setText(R.string.user_feedback);
            transaction.replace(R.id.feedback_rl, FeedbackListFragment.newInstance()).commit();
        } else { //其他权限，进行意见的填写和提交
            tvTitle.setText(R.string.opinion_feedback);
            transaction.replace(R.id.feedback_rl, FeedbackFragment.newInstance()).commit();
        }
    }
}
