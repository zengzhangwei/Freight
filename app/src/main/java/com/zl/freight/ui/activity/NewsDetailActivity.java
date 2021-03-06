package com.zl.freight.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.TopNewsBean;
import com.zl.freight.utils.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/9
 * 文章详情页
 */
public class NewsDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_news_title)
    TextView tvNewsTitle;
    @BindView(R.id.iv_news_icon)
    ImageView ivNewsIcon;
    @BindView(R.id.tv_news_content)
    TextView tvNewsContent;

    public static final String PICTURE = "img";
    public static final String TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("文章详情");
        TopNewsBean data = (TopNewsBean) getIntent().getSerializableExtra("data");
        ImageLoader.loadImageUrl(mActivity, data.getInfoPic(), ivNewsIcon);
        ViewCompat.setTransitionName(ivNewsIcon, PICTURE);
        tvNewsTitle.setText(data.getInfoTitle());
        tvNewsContent.setText(data.getInfoContent());
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }
}
