package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.dialog.PhotoDialog;
import com.zl.zlibrary.utils.MiPictureHelper;

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
    private PhotoDialog photoDialog;

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
                    ivAddImg.setImageBitmap(BitmapFactory.decodeFile(photoDialog.imagePath));
                    break;
                case PhotoDialog.SELECT_PHOTO:
                    String imagePath = MiPictureHelper.getPath(mActivity, data.getData());
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
                mActivity.finish();
                break;
        }
    }
}
