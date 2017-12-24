package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.zl.freight.R;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.dialog.PhotoDialog;
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
 * 发布文章
 */
public class NewPushFragment extends BaseFragment {


    @BindView(R.id.iv_add_img)
    ImageView ivAddImg;
    @BindView(R.id.tv_news_content)
    EditText tvNewsContent;
    @BindView(R.id.np_tab_push)
    FloatingActionButton npTabPush;
    Unbinder unbinder;
    @BindView(R.id.tv_web_url)
    EditText tvWebUrl;
    @BindView(R.id.tv_title_news)
    EditText tvTitleNews;
    private PhotoDialog photoDialog;
    private String imagePath;

    public NewPushFragment() {
        // Required empty public constructor
    }

    public static NewPushFragment newInstance() {

        Bundle args = new Bundle();

        NewPushFragment fragment = new NewPushFragment();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK) {
            switch (requestCode) {
                case PhotoDialog.PICK_FROM_CAMERA:
                    imagePath = photoDialog.imagePath;
                    ivAddImg.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    break;
                case PhotoDialog.SELECT_PHOTO:
                    imagePath = MiPictureHelper.getPath(mActivity, data.getData());
                    ivAddImg.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    break;
            }
        }
    }

    private void initView() {
        photoDialog = new PhotoDialog(mActivity);
        tvWebUrl.setVisibility(View.GONE);
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
                commit();
                break;
        }
    }

    /**
     * 发布文章
     */
    private void commit() {
        String title = tvTitleNews.getText().toString().trim();
        String content = tvNewsContent.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            showToast("文章标题不能为空");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            showToast("文章内容不能为空");
            return;
        }
        if (TextUtils.isEmpty(imagePath)) {
            showToast("请上传图片");
            return;
        }

        Map<String, String> params = new HashMap<>();
        //TODO 待补充接口（发布文章）
//        SoapUtils.Post(mActivity, API.BaseDict, params, new SoapCallback() {
//            @Override
//            public void onError(String error) {
//
//            }
//
//            @Override
//            public void onSuccess(String data) {
//
//            }
//        });

        mActivity.finish();

    }
}
