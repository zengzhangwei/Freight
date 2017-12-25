package com.zl.freight.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseCarEntity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.KeyValueBean;
import com.zl.freight.ui.dialog.CarLengthDialog;
import com.zl.freight.ui.fragment.PushPersonFragment;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.zlibrary.dialog.PhotoDialog;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.ImageFactory;
import com.zl.zlibrary.utils.MiPictureHelper;

import java.util.HashMap;
import java.util.Map;

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
    @BindView(R.id.et_person_code)
    EditText etPersonCode;
    @BindView(R.id.et_input_car_code)
    EditText etInputCarCode;
    @BindView(R.id.tv_choose_push_p)
    TextView tvChoosePushP;
    @BindView(R.id.register_rl)
    RelativeLayout registerRl;
    @BindView(R.id.tv_choose_car_length_type)
    TextView tvChooseCarLengthType;
    private int type = PERSONTYPE;
    private PhotoDialog photoDialog;
    private String imagePath = "";
    private String IMGPERSONPATH = "";
    private String IMGHANDPATH = "";
    private String IMGDRIVINGPATH = "";
    private String IMGRUNPATH = "";
    private String IMGFRONTPATH = "";
    private String IMGBACKPATH = "";
    private String pushName;
    private String pushPhone;
    private String[] sexs = {"男", "女"};
    private AlertDialog sexDialog;
    private PushPersonFragment pushPersonFragment;
    private CarLengthDialog carLengthDialog;
    private KeyValueBean l, t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initListener() {
        //获取推介人信息的回调
        pushPersonFragment.setOnRetrunDataListener(new PushPersonFragment.OnReturnDataListener() {
            @Override
            public void onReturnData(String name, String phone) {
                pushName = name;
                pushPhone = phone;
                tvChoosePushP.setText(name + "  " + phone);
            }
        });
        carLengthDialog.setOnGetCarLengthDataListener(new CarLengthDialog.OnGetCarLengthDataListener() {
            @Override
            public void carLengthData(KeyValueBean length, KeyValueBean type, KeyValueBean goodsType) {
                l = length;
                t = type;
                tvChooseCarLengthType.setText(length.getCodeName() + "  " + type.getCodeName());
            }
        });
    }

    private void initView() {
        tvTitle.setText("司机注册");
        tvTitleRight.setText("提交");
        photoDialog = new PhotoDialog(mActivity);
        pushPersonFragment = PushPersonFragment.newInstance();
        carLengthDialog = new CarLengthDialog(mActivity, 0);
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

    @OnClick({R.id.iv_back, R.id.tv_title_right, R.id.iv_person_photo, R.id.iv_hand_photo,
            R.id.iv_driving_photo, R.id.iv_run_photo, R.id.iv_car_front_photo, R.id.iv_car_back_photo,
            R.id.tv_send_code, R.id.tv_choose_push_p, R.id.tv_choose_car_length_type})
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
            //输入推介人信息
            case R.id.tv_choose_push_p:
                startPersonFragment();
                break;
            //选择车长车型
            case R.id.tv_choose_car_length_type:
                carLengthDialog.show(view);
                break;
        }
    }

    /**
     * 进入输入推介人信息界面
     */
    private void startPersonFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("no")
                .replace(R.id.register_rl, pushPersonFragment)
                .commit();
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
        String personCode = etPersonCode.getText().toString().trim();
        String carCode = etInputCarCode.getText().toString().trim();

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

        if (TextUtils.isEmpty(code)) {
            showToast("请输入验证码");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }

        if (TextUtils.isEmpty(carCode)) {
            showToast("请输入车牌照");
            return;
        }

        if (password.length() < 6 || password.length() > 12) {
            showToast("密码长度不符合标准");
            return;
        }

        if (TextUtils.isEmpty(IMGBACKPATH) || TextUtils.isEmpty(IMGFRONTPATH) || TextUtils.isEmpty(IMGRUNPATH)
                || TextUtils.isEmpty(IMGDRIVINGPATH) || TextUtils.isEmpty(IMGHANDPATH) || TextUtils.isEmpty(IMGPERSONPATH)) {
            showToast("请上传完整的图片信息");
            return;
        }

        if (t == null || l == null) {
            showToast("请选择车长和车型");
            return;
        }

        //都不为空时进行数据的添加
        if (!TextUtils.isEmpty(pushName) && !TextUtils.isEmpty(pushPhone)) {

        }

        Map<String, String> params = new HashMap<>();
        //用户角色
        params.put("UserRole", "2");
        //此项为空
        params.put("CompanyEntityJson", "");

        BaseUserEntity userEntity = new BaseUserEntity();
        userEntity.setRealName(name);
        userEntity.setUserName(phone);
        userEntity.setIdCardNumber(personCode);
        userEntity.setPassWord(password);

        //推介人不为空添加推介人
        if (!TextUtils.isEmpty(pushName) && !TextUtils.isEmpty(pushPhone)) {
            userEntity.setReferral(pushName);
            userEntity.setReferralTel(pushPhone);
        }

        BaseCarEntity carEntity = new BaseCarEntity();
        carEntity.setCarLong(Integer.parseInt(l.getId()));
        carEntity.setCarType(Integer.parseInt(t.getId()));
        params.put("UserEntityJson", GsonUtils.toJson(userEntity));
        params.put("CarEntityJson", GsonUtils.toJson(carEntity));

        if (!TextUtils.isEmpty(content)) {

        }

        SoapUtils.Post(mActivity, API.Register, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", error);
            }

            @Override
            public void onSuccess(String data) {
                Log.e("data", data);
            }
        });

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
