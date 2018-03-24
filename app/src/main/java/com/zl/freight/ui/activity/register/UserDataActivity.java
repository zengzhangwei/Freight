package com.zl.freight.ui.activity.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseCarEntity;
import com.zl.freight.mode.BaseCompanyEntity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.CarUserBean;
import com.zl.freight.mode.UserBean;
import com.zl.freight.ui.activity.AgreementActivity;
import com.zl.freight.ui.activity.CameraActivity;
import com.zl.freight.ui.activity.FrontCameraActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.SendCodeUtils;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.dialog.PhotoDialog;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.MiPictureHelper;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserDataActivity extends BaseActivity {

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
    @BindView(R.id.et_input_push_name)
    EditText etInputPushName;
    @BindView(R.id.et_input_push_phone)
    EditText etInputPushPhone;
    @BindView(R.id.cb_agreement)
    CheckBox cbAgreement;
    @BindView(R.id.uregister_rl)
    RelativeLayout uregisterRl;
    private PhotoDialog photoDialog;
    private String imagePath;
    private String IMGPERSONPATH = "";
    private String IMGHANDPATH = "";
    private int type;
    private String Referral;
    private String ReferralTel;
    private final int PERSONTYPE = 0x1;
    private final int HANDTYPE = 0x2;
    public static UserDataActivity userDataActivity;
    private UserBean userEntity;
    BaseCompanyEntity companyEntity;
    //填充司机数据
    BaseCarEntity carEntity;
    private CarUserBean carUserBean;
    private String sendCode;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uregister);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        Map<String, String> params = new HashMap<>();
        params.put("UserId", userData.getId());
        params.put("UserRole", userData.getUserRole());
        SoapUtils.Post(mActivity, API.ShowUserInfo, params, new SoapCallback() {

            @Override
            public void onError(String error) {
                Log.e("error", "获取用户信息失败");
            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONArray array = new JSONArray(data);
                    carUserBean = GsonUtils.fromJson(array.optString(0), CarUserBean.class);
                    upDateUi(carUserBean);
                } catch (Exception e) {
                    showToast("用户信息出错，请联系管理员");
                }
            }
        });
    }

    /**
     * 更新界面数据
     *
     * @param carUserBean
     */
    private void upDateUi(CarUserBean carUserBean) {
        //初始化一下数据
        userEntity = new UserBean();
        userEntity.setId(carUserBean.getId());
        userEntity.setReferral(carUserBean.getReferral());
        userEntity.setReferralTel(carUserBean.getReferralTel());
        Referral = carUserBean.getReferral();
        ReferralTel = carUserBean.getReferralTel();
        userEntity.setRealName(carUserBean.getRealName());
        userEntity.setUserName(carUserBean.getUserName());
        userEntity.setPassWord(carUserBean.getPassWord());
        userEntity.setUserRole(SpUtils.getUserData(mActivity).getUserRole());
        userEntity.setIdCardNumber(carUserBean.getIdCardNumber());
        userEntity.setIdCard1(carUserBean.getIdCard1());
        userEntity.setIdCard2(carUserBean.getIdCard2());

        if (carUserBean.getUserRole().equals("" + API.DRIVER)) {
            carEntity = new BaseCarEntity();
            carEntity.setId(Integer.parseInt(carUserBean.getId1()));
            carEntity.setUserId(Integer.parseInt(carUserBean.getId()));
            carEntity.setCarLong(Integer.parseInt(carUserBean.getCarLong()));
            carEntity.setCarType(Integer.parseInt(carUserBean.getCarType()));
            carEntity.setCarNo(carUserBean.getCarNo());
            carEntity.setDrivingLlicence(carUserBean.getDrivingLlicence());
            carEntity.setVehicleLicense(carUserBean.getVehicleLicense());
            carEntity.setCarPic1(carUserBean.getCarPic1());
            carEntity.setCarPic2(carUserBean.getCarPic2());
            carEntity.setCarPic3("");
        } else {
            companyEntity = new BaseCompanyEntity();
            companyEntity.setId(Integer.parseInt(carUserBean.getId1()));
            companyEntity.setUserId(Integer.parseInt(carUserBean.getId()));
            companyEntity.setCompanyName(carUserBean.getCompanyName());
            companyEntity.setCompanyAddress(carUserBean.getCompanyAddress());
            companyEntity.setCompanyPic(carUserBean.getCompanyPic());
            companyEntity.setCompanyCode(carUserBean.getCompanyCode());
            companyEntity.setStorePic(carUserBean.getStorePic());
            companyEntity.setStorePic1(carUserBean.getStorePic1());
            companyEntity.setStorePic2(carUserBean.getStorePic2());

        }

        //为界面控件赋值
        etInputName.setText(carUserBean.getRealName());
        etPersonCode.setText(carUserBean.getIdCardNumber());
        etInputPhone.setText(carUserBean.getUserName());
        etInputPassword.setText(carUserBean.getPassWord());
        tvChoosePushP.setText(carUserBean.getReferral() + "  " + carUserBean.getReferralTel());
        etInputPushName.setText(carUserBean.getReferral());
        etInputPushPhone.setText(carUserBean.getReferralTel());
        ImageLoader.loadImageUrl(mActivity, carUserBean.getIdCard1(), ivPersonPhoto);
        ImageLoader.loadImageUrl(mActivity, carUserBean.getIdCard2(), ivHandPhoto);

        if (!TextUtils.isEmpty(carUserBean.getUnCheckInfo())) {
            alertDialog = new AlertDialog.Builder(mActivity)
                    .setTitle("错误原因")
                    .setMessage(carUserBean.getUnCheckInfo())
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create();
            alertDialog.show();
            tvTitleRight.setText("查看错误原因");
        }
    }


    private void initListener() {

    }

    private void initView() {
        userDataActivity = this;
        tvTitle.setText("实名认证");
        photoDialog = new PhotoDialog(mActivity);
        Spanned spanned = Html.fromHtml(" <font color=\"#1e90ff\">《货物运输协议》</font>点击查看");
        cbAgreement.setText(spanned);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK) {
//            switch (requestCode) {
//                case PhotoDialog.PICK_FROM_CAMERA:
//                    imagePath = photoDialog.imagePath;
//                    break;
//                case PhotoDialog.SELECT_PHOTO:
//                    imagePath = MiPictureHelper.getPath(mActivity, data.getData());
//                    break;
//            }
//
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
        }
    }

    private void setImage(ImageView image) {
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

    @OnClick({R.id.iv_back, R.id.tv_send_code, R.id.tv_choose_push_p, R.id.iv_person_photo,
            R.id.iv_hand_photo, R.id.tv_title_right, R.id.tv_next})
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
                next();
                break;
            //下一步
            case R.id.tv_title_right:
                if (alertDialog == null) return;
                alertDialog.show();
                break;
        }
    }

    /**
     * 下一步操作
     */
    private void next() {

        if (!cbAgreement.isChecked()) {
            new AlertDialog.Builder(mActivity).setMessage("您未同意《货物运输协议》无法进行下一步操作，请先阅读货物运输协议")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cbAgreement.setChecked(true);
                            startActivity(new Intent(mActivity, AgreementActivity.class));
                        }
                    }).show();
            return;
        }

        String name = etInputName.getText().toString().trim();
        String phone = etInputPhone.getText().toString().trim();
        String code = etInputCode.getText().toString().trim();
        String password = etInputPassword.getText().toString().trim();
        String personCode = etPersonCode.getText().toString().trim();
        Referral = etInputPushName.getText().toString().trim();
        ReferralTel = etInputPushPhone.getText().toString().trim();

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

        //都不为空时进行数据的添加
        if (!TextUtils.isEmpty(Referral) && !TextUtils.isEmpty(ReferralTel)) {
            if (!RegexUtils.isMobileExact(ReferralTel)) {
                showToast(getResources().getString(R.string.please_input_ok_phone));
                return;
            }
            userEntity.setReferral(Referral);
            userEntity.setReferralTel(ReferralTel);
        }

        userEntity.setRealName(name);
        userEntity.setUserName(phone);
        userEntity.setPassWord(password);
        userEntity.setIdCardNumber(personCode);

        Intent intent;
        if (Integer.parseInt(carUserBean.getUserRole()) == API.DRIVER) {
            intent = new Intent(mActivity, CarDataActivity.class);
            intent.putExtra("carEntity", carEntity);
            intent.putExtra("length", carUserBean.getCodeName1());
            intent.putExtra("type", carUserBean.getCodeName());
        } else {
            intent = new Intent(mActivity, CompanyActivity.class);
            intent.putExtra("companyEntity", companyEntity);
        }

        if (!TextUtils.isEmpty(IMGPERSONPATH)) {
            intent.putExtra("idCard1", IMGPERSONPATH);
        }
        if (!TextUtils.isEmpty(IMGHANDPATH)) {
            intent.putExtra("idCard2", IMGHANDPATH);
        }

        intent.putExtra("userEntity", userEntity);
        startActivity(intent);
    }

    /**
     * 进入输入推介人信息界面
     */
    private void startPersonFragment() {

    }
}
