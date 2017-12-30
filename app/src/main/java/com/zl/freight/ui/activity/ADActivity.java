package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.CarInformationEntity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.ImageFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/30
 * 发布广告页
 */
public class ADActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_title_ad)
    EditText tvTitleAd;
    @BindView(R.id.tv_ad_content)
    EditText tvAdContent;
    @BindView(R.id.tv_ad_url)
    EditText tvAdUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("发布广告");
    }

    @OnClick({R.id.iv_back, R.id.np_tab_push})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.np_tab_push:
                commit();
                break;
        }
    }

    /**
     * 发布广告
     */
    private void commit() {
        String title = tvTitleAd.getText().toString().trim();
        String content = tvAdContent.getText().toString().trim();
        String url = tvAdUrl.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            showToast("文章标题不能为空");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            showToast("文章内容不能为空");
            return;
        }
        if (TextUtils.isEmpty(url)) {
            showToast("网址不能为空");
            return;
        }
        if (!RegexUtils.isURL(url)) {
            showToast("请输入正确的网址");
            return;
        }

//        CarInformationEntity entity = new CarInformationEntity();
//        entity.setInfoContent(content);
//        entity.setInfoTitle(title);
//        entity.setInfoKey(SpUtils.getUserData(mActivity).getId());
//        entity.setInfoType(0);
//        showDialog("文章发布中...");
//        Map<String, String> params = new HashMap<>();
//        params.put("InfoJson", GsonUtils.toJson(entity));
//        SoapUtils.Post(mActivity, API.AddInfo, params, new SoapCallback() {
//            @Override
//            public void onError(String error) {
//                hideDialog();
//                showToast(error);
//            }
//
//            @Override
//            public void onSuccess(String data) {
//                hideDialog();
//                showToast("发布成功");
//                mActivity.finish();
//            }
//        });

        mActivity.finish();
    }
}
