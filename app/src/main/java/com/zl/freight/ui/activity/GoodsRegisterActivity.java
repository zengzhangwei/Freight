package com.zl.freight.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.dialog.PhotoDialog;
import com.zl.zlibrary.utils.ImageFactory;
import com.zl.zlibrary.utils.MiPictureHelper;
import com.zl.zlibrary.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/12
 */
public class GoodsRegisterActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.et_input_name)
    EditText etInputName;
    @BindView(R.id.tv_choose_sex)
    TextView tvChooseSex;
    @BindView(R.id.et_input_phone)
    EditText etInputPhone;
    @BindView(R.id.et_input_code)
    EditText etInputCode;
    @BindView(R.id.iv_hours_photo)
    ImageView ivHoursPhoto;
    @BindView(R.id.gr_img_grid)
    GridView grImgGrid;
    @BindView(R.id.tab_add_icon)
    FloatingActionButton tabAddIcon;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.et_input_password)
    EditText etInputPassword;
    @BindView(R.id.tv_commit_register)
    TextView tvCommitRegister;

    private String[] sexs = {"男", "女"};
    private String[] personTypes = {"公司性质", "个人性质"};
    private AlertDialog sexDialog, typeDialog;
    private PhotoDialog photoDialog;
    private int type = 1;
    private ArrayList<String> photoList = new ArrayList<>();
    private String imagePath = "";
    private final int REQUEST_CAMERA_CODE = 0x427;
    private UniversalAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_register);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("货主注册");
        tvTitleRight.setText("切换");
        mAdapter = new UniversalAdapter<String>(mActivity, photoList, R.layout.image_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                ImageView view = holder.getView(R.id.iv_item);
                view.setImageBitmap(ImageFactory.getSimpeImage(s));
            }
        };
        grImgGrid.setAdapter(mAdapter);
        sexDialog = new AlertDialog.Builder(mActivity).setTitle("选择性别").setSingleChoiceItems(sexs, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sexDialog.dismiss();
                tvChooseSex.setText(i == 0 ? "男" : "女");
            }
        }).create();
        typeDialog = new AlertDialog.Builder(mActivity).setTitle("选择类别").setSingleChoiceItems(personTypes, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                typeDialog.dismiss();
                type = i;
                if (i == 0) {
                    grImgGrid.setVisibility(View.VISIBLE);
                    tabAddIcon.setVisibility(View.VISIBLE);
                    ivHoursPhoto.setVisibility(View.GONE);
                } else {
                    grImgGrid.setVisibility(View.GONE);
                    tabAddIcon.setVisibility(View.GONE);
                    ivHoursPhoto.setVisibility(View.VISIBLE);
                }
            }
        }).create();
        photoDialog = new PhotoDialog(mActivity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK) {
            switch (requestCode) {
                case PhotoDialog.PICK_FROM_CAMERA:
                    imagePath = photoDialog.imagePath;
                    setImage();
                    break;
                case PhotoDialog.SELECT_PHOTO:
                    imagePath = MiPictureHelper.getPath(mActivity, data.getData());
                    setImage();
                    break;
                case REQUEST_CAMERA_CODE:
                    photoList.clear();
                    photoList.addAll(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_title_right, R.id.tv_choose_sex, R.id.tab_add_icon, R.id.iv_hours_photo, R.id.tv_commit_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                finish();
                break;
            //切换身份
            case R.id.tv_title_right:
                typeDialog.show();
                break;
            //选择性别
            case R.id.tv_choose_sex:
                sexDialog.show();
                break;
            //添加图片
            case R.id.tab_add_icon:
                openAlbum();
                break;
            //添加店面实体图片
            case R.id.iv_hours_photo:
                photoDialog.show(view);
                break;
            //提交注册信息
            case R.id.tv_commit_register:
                commit();
                break;
        }
    }

    private void commit() {
        String name = etInputName.getText().toString().trim();
        String phone = etInputPhone.getText().toString().trim();
        String code = etInputCode.getText().toString().trim();
        String password = etInputPassword.getText().toString().trim();
        String sex = tvChooseSex.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            showToast("请输入真实姓名");
            return;
        }

        if (TextUtils.isEmpty(sex)) {
            showToast("请选择性别");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            showToast("请输入手机号");
            return;
        }

        if (!RegexUtils.isMobileExact(phone)) {
            showToast("手机号格式不对");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            showToast("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }

        if (password.length() < 6 || password.length() > 12) {
            showToast("密码长度不符合标准");
            return;
        }


        if (type == 0) {//公司性质

        } else {//个人性质

        }

    }

    private void setImage() {
        byte[] getimage = ImageFactory.getimage(imagePath);
        ivHoursPhoto.setImageBitmap(BitmapFactory.decodeByteArray(getimage, 0, getimage.length));
    }

    /**
     * 打开相册进行照片选择
     */
    public void openAlbum() {
        PhotoPickerIntent intent = new PhotoPickerIntent(mActivity);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(false); // 是否显示拍照， 默认false
        intent.setMaxTotal(3); // 最多选择照片数量，默认为9
        intent.setSelectedPaths(photoList); // 已选中的照片地址， 用于回显选中状态
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }
}
