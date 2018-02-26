package com.zl.freight.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.ui.fragment.PushPersonFragment;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.SendCodeUtils;
import com.zl.freight.utils.SoapCallback;
import com.zl.zlibrary.dialog.PhotoDialog;
import com.zl.zlibrary.utils.GzipUtils;
import com.zl.zlibrary.utils.ImageFactory;
import com.zl.zlibrary.utils.MiPictureHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhanglei
 * @date 17/12/25
 * 实名注册页
 */
public class URegisterActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.et_input_name)
    EditText etInputName;
    @BindView(R.id.et_person_code)
    EditText etPersonCode;
    @BindView(R.id.et_input_phone)
    EditText etInputPhone;
    @BindView(R.id.et_input_code)
    EditText etInputCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.et_input_password)
    EditText etInputPassword;
    @BindView(R.id.tv_choose_push_p)
    TextView tvChoosePushP;
    @BindView(R.id.iv_person_photo)
    ImageView ivPersonPhoto;
    @BindView(R.id.iv_hand_photo)
    ImageView ivHandPhoto;
    @BindView(R.id.tv_next)
    TextView tvNext;
    private PushPersonFragment pushPersonFragment;
    private PhotoDialog photoDialog;
    private String imagePath;
    private String IMGPERSONPATH = "";
    private String IMGHANDPATH = "";
    private int type;
    private String Referral;
    private String ReferralTel;
    private String sendCode;
    private final int PERSONTYPE = 0x1;
    private final int HANDTYPE = 0x2;
    private int role;
    public static URegisterActivity uRegisterActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uregister);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initListener() {
        //获取推介人信息的回调
        pushPersonFragment.setOnRetrunDataListener(new PushPersonFragment.OnReturnDataListener() {
            @Override
            public void onReturnData(String name, String phone) {
                Referral = name;
                ReferralTel = phone;
                tvChoosePushP.setText(name + "  " + phone);
            }
        });
    }

    private void initView() {
        uRegisterActivity = this;
        role = getIntent().getIntExtra("role", API.DRIVER);
        tvTitle.setText("实名认证");
        photoDialog = new PhotoDialog(mActivity);
        pushPersonFragment = PushPersonFragment.newInstance();
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

            switch (requestCode) {
                case PERSONTYPE:
                    imagePath = data.getStringExtra("path");
                    IMGPERSONPATH = imagePath;
                    setImage(ivPersonPhoto);
                    break;
                case HANDTYPE:
                    imagePath = data.getStringExtra("path");
                    IMGHANDPATH = imagePath;
                    setImage(ivHandPhoto);
                    break;
            }

//            switch (type) {
//                case PERSONTYPE:
//                    IMGPERSONPATH = imagePath;
//                    setImage(ivPersonPhoto);
//                    break;
//                case HANDTYPE:
//                    IMGHANDPATH = imagePath;
//                    setImage(ivHandPhoto);
//                    break;
//            }
        }
    }

    /**
     * 显示图片
     *
     * @param image
     */
    private void setImage(final ImageView image) {
        ImageLoader.loadImageFile(imagePath, image);
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

    @OnClick({R.id.iv_back, R.id.tv_send_code, R.id.tv_choose_push_p, R.id.iv_person_photo, R.id.iv_hand_photo, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                finish();
                break;
            //发送验证码
            case R.id.tv_send_code:
                sendCode();
                break;
            //选择推介人
            case R.id.tv_choose_push_p:
                startPersonFragment();
                break;
            //上传身份证照片
            case R.id.iv_person_photo:
//                choosePhoto(PERSONTYPE, view);
                startActivityForResult(new Intent(mActivity, CameraActivity.class), PERSONTYPE);

                break;
            //上传手持身份证照片
            case R.id.iv_hand_photo:
//                choosePhoto(HANDTYPE, view);
                startActivityForResult(new Intent(mActivity, FrontCameraActivity.class), HANDTYPE);
                break;
            //下一步
            case R.id.tv_next:
//                testZip();
                next();
                break;
        }
    }

    private void sendCode() {
        String trim = etInputPhone.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            showToast("请输入手机号");
            return;
        }
        SendCodeUtils.sendCode(trim, tvSendCode, new SoapCallback() {
            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String data) {
                sendCode = data;
            }
        });
    }


    /**
     * 测试zip压缩
     */
    private void testZip() {
        byte[] getimage = ImageFactory.getimage(IMGPERSONPATH);
        Log.e("testZip", getimage.length + "");
        byte[] nameByte = GzipUtils.gZip(getimage);
        Log.e("testZip", nameByte.length + "");
        String zipStr = ImageFactory.base64Encode(nameByte);
        Log.e("testZip", zipStr);
        byte[] bytes1 = ImageFactory.base64Decode(zipStr);
        Log.e("testZip", bytes1.length + "");
        byte[] bytes = GzipUtils.unGZip(bytes1);
        Log.e("testZip", bytes.length + "");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ivHandPhoto.setImageBitmap(bitmap);

    }

    /**
     * 下一步操作
     */
    private void next() {
        String name = etInputName.getText().toString().trim();
        String phone = etInputPhone.getText().toString().trim();
        String code = etInputCode.getText().toString().trim();
        String password = etInputPassword.getText().toString().trim();
        String personCode = etPersonCode.getText().toString().trim();

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

        if (TextUtils.isEmpty(personCode)) {
            showToast("请输入身份证号");
            return;
        }

        if (!RegexUtils.isIDCard18(personCode)) {
            showToast("请输入正确身份证号");
            return;
        }

        if (TextUtils.isEmpty(sendCode)) {
            showToast("验证码还未发送");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            showToast("请输入验证码");
            return;
        }

        if (!sendCode.equals(code)) {
            showToast("验证码输入不正确");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }

        if (password.length() != 6) {
            showToast("密码长度不符合标准");
            return;
        }

        if (TextUtils.isEmpty(IMGHANDPATH) || TextUtils.isEmpty(IMGPERSONPATH)) {
            showToast("请上传完整的图片信息");
            return;
        }

        BaseUserEntity userEntity = new BaseUserEntity();
        //都不为空时进行数据的添加
        if (!TextUtils.isEmpty(Referral) && !TextUtils.isEmpty(ReferralTel)) {
            userEntity.setReferral(Referral);
            userEntity.setReferralTel(ReferralTel);
        }

        userEntity.setRealName(name);
        userEntity.setUserName(phone);
        userEntity.setPassWord(password);
        userEntity.setIdCardNumber(personCode);
        userEntity.setIdCard1(IMGHANDPATH);
        userEntity.setIdCard2(IMGPERSONPATH);

        Intent intent;
        if (role == API.DRIVER) {
            intent = new Intent(mActivity, RegisterActivity.class);
        } else {
            intent = new Intent(mActivity, GoodsRegisterActivity.class);
        }
        intent.putExtra("userEntity", userEntity);
        startActivity(intent);
    }

    /**
     * 进入输入推介人信息界面
     */
    private void startPersonFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("no")
                .replace(R.id.uregister_rl, pushPersonFragment)
                .commit();
    }
}
