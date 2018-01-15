package com.zl.freight.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.RegexUtils;
import com.zl.freight.R;
import com.zl.freight.mode.CarInformationEntity;
import com.zl.freight.ui.activity.MyNewsActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.dialog.PhotoDialog;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.ImageFactory;
import com.zl.zlibrary.utils.MiPictureHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 17/12/9
 * 发布网页
 */
public class WebPushFragment extends BaseFragment {


    @BindView(R.id.iv_add_img)
    ImageView ivAddImg;
    @BindView(R.id.tv_news_content)
    EditText tvNewsContent;
    @BindView(R.id.tv_web_url)
    EditText tvWebUrl;
    @BindView(R.id.np_tab_push)
    FloatingActionButton npTabPush;
    Unbinder unbinder;
    @BindView(R.id.tv_title_news)
    EditText tvTitleNews;
    private PhotoDialog photoDialog;
    private String imagePath = "";
    private AlertDialog payDialog;

    public WebPushFragment() {
        // Required empty public constructor
    }

    public static WebPushFragment newInstance() {

        Bundle args = new Bundle();

        WebPushFragment fragment = new WebPushFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_push, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvNewsContent.setVisibility(View.GONE);
        photoDialog = new PhotoDialog(mActivity);
        payDialog = new AlertDialog.Builder(mActivity)
                .setMessage("文章已暂存到我的文章与广告模块，需完成付费后方可被别人浏览。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mActivity.finish();
                    }
                })
                .setPositiveButton("去付费", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pay();
                    }
                }).create();
    }

    /**
     * 支付发布文章的费用
     */
    private void pay() {
        startActivity(new Intent(mActivity, MyNewsActivity.class));
        mActivity.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK) {
            switch (requestCode) {
                case PhotoDialog.PICK_FROM_CAMERA:
                    imagePath = photoDialog.imagePath;
                    break;
                case PhotoDialog.SELECT_PHOTO:
                    imagePath = MiPictureHelper.getPath(mActivity, data.getData());
                    break;
            }
            ImageLoader.loadImageFile(imagePath, ivAddImg);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_add_img, R.id.np_tab_push})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_img:
                photoDialog.show(view);
                break;
            case R.id.np_tab_push:
                push();
                break;
        }
    }

    /**
     * 发送文章
     */
    private void push() {
        String title = tvTitleNews.getText().toString().trim();
        String url = tvWebUrl.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            showToast("标题不能为空");
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

        if (TextUtils.isEmpty(imagePath)) {
            showToast("图片不能为空");
            return;
        }

        CarInformationEntity entity = new CarInformationEntity();
        entity.setInfoLink(url);
        entity.setInfoTitle(title);
        entity.setUserId(SpUtils.getUserData(mActivity).getId());
        entity.setInfoKey(SpUtils.getUserData(mActivity).getId());
        entity.setInfoType(1);
        entity.setInfoPic(ImageFactory.base64Encode(ImageFactory.getimage(imagePath)));
        showDialog("文章发布中...");
        Map<String, String> params = new HashMap<>();
        params.put("InfoJson", GsonUtils.toJson(entity));
        SoapUtils.Post(mActivity, API.AddInfo, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                hideDialog();
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                hideDialog();
                payDialog.show();
            }
        });
    }
}
