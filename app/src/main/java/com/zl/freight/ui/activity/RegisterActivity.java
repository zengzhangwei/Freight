package com.zl.freight.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.zlibrary.dialog.PhotoDialog;
import com.zl.zlibrary.utils.ImageFactory;
import com.zl.zlibrary.utils.MiPictureHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/7
 * 用户注册页
 */
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.et_input_name)
    EditText etInputName;
    @BindView(R.id.et_input_phone)
    EditText etInputPhone;
    @BindView(R.id.et_input_code)
    EditText etInputCode;
    @BindView(R.id.iv_person_photo)
    ImageView ivPersonPhoto;
    @BindView(R.id.iv_hand_photo)
    ImageView ivHandPhoto;
    @BindView(R.id.iv_driving_photo)
    ImageView ivDrivingPhoto;
    @BindView(R.id.iv_run_photo)
    ImageView ivRunPhoto;
    @BindView(R.id.iv_car_front_photo)
    ImageView ivCarFrontPhoto;
    @BindView(R.id.iv_car_back_photo)
    ImageView ivCarBackPhoto;
    private final int PERSONTYPE = 0x1;
    private final int HANDTYPE = 0x2;
    private final int DRIVINGTYPE = 0x3;
    private final int RUNTYPE = 0x4;
    private final int FRONTTYPE = 0x5;
    private final int BACKTYPE = 0x6;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.et_input_password)
    EditText etInputPassword;
    @BindView(R.id.et_car_content)
    EditText etCarContent;
    private int type = PERSONTYPE;
    private PhotoDialog photoDialog;
    private String imagePath = "";
    private String IMGPERSONPATH = "";
    private String IMGHANDPATH = "";
    private String IMGDRIVINGPATH = "";
    private String IMGRUNPATH = "";
    private String IMGFRONTPATH = "";
    private String IMGBACKPATH = "";
    private String[] sexs = {"男", "女"};
    private AlertDialog sexDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("司机注册");
        tvTitleRight.setText("提交");
        photoDialog = new PhotoDialog(mActivity);
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

            switch (type) {
                case PERSONTYPE:
                    IMGPERSONPATH = imagePath;
                    setImage(ivPersonPhoto);
                    break;
                case HANDTYPE:
                    IMGHANDPATH = imagePath;
                    setImage(ivHandPhoto);
                    break;
                case DRIVINGTYPE:
                    IMGDRIVINGPATH = imagePath;
                    setImage(ivDrivingPhoto);
                    break;
                case RUNTYPE:
                    IMGRUNPATH = imagePath;
                    setImage(ivRunPhoto);
                    break;
                case FRONTTYPE:
                    IMGFRONTPATH = imagePath;
                    setImage(ivCarFrontPhoto);
                    break;
                case BACKTYPE:
                    IMGBACKPATH = imagePath;
                    setImage(ivCarBackPhoto);
                    break;
            }
        }
    }

    private void setImage(ImageView image) {
        byte[] getimage = ImageFactory.getimage(imagePath);
        image.setImageBitmap(BitmapFactory.decodeByteArray(getimage, 0, getimage.length));
    }

    @OnClick({R.id.iv_back, R.id.tv_title_right,R.id.iv_person_photo, R.id.iv_hand_photo,
            R.id.iv_driving_photo, R.id.iv_run_photo, R.id.iv_car_front_photo, R.id.iv_car_back_photo, R.id.tv_send_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                finish();
                break;
            //提交注册信息
            case R.id.tv_title_right:
                commit();
                break;
            //上传身份证照片
            case R.id.iv_person_photo:
                choosePhoto(PERSONTYPE, view);
                break;
            //上传手持身份证照片
            case R.id.iv_hand_photo:
                choosePhoto(HANDTYPE, view);
                break;
            //上传驾驶证照片
            case R.id.iv_driving_photo:
                choosePhoto(DRIVINGTYPE, view);
                break;
            //上传行驶证照片
            case R.id.iv_run_photo:
                choosePhoto(RUNTYPE, view);
                break;
            //上传车辆前牌照照片
            case R.id.iv_car_front_photo:
                choosePhoto(FRONTTYPE, view);
                break;
            //上传车辆后牌照照片
            case R.id.iv_car_back_photo:
                choosePhoto(BACKTYPE, view);
                break;
            //上传车辆后牌照照片
            case R.id.tv_send_code:
                choosePhoto(BACKTYPE, view);
                break;
        }
    }

    /**
     * 提交注册
     */
    private void commit() {
        String name = etInputName.getText().toString().trim();
        String phone = etInputPhone.getText().toString().trim();
        String code = etInputCode.getText().toString().trim();
        String password = etInputPassword.getText().toString().trim();
        String content = etCarContent.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            showToast("请输入真实姓名");
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

        if (TextUtils.isEmpty(IMGBACKPATH) || TextUtils.isEmpty(IMGFRONTPATH) || TextUtils.isEmpty(IMGRUNPATH)
                || TextUtils.isEmpty(IMGDRIVINGPATH) || TextUtils.isEmpty(IMGHANDPATH) || TextUtils.isEmpty(IMGPERSONPATH)){
            showToast("请上传完整的图片信息");
            return;
        }

    }

    /**
     * 选取照片
     *
     * @param type
     * @param view
     */
    private void choosePhoto(int type, View view) {
        this.type = type;
        photoDialog.show(view);
    }
}
