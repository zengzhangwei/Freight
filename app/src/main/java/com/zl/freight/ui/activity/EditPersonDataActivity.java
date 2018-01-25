package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.CarUserBean;
import com.zl.freight.mode.UserBean;
import com.zl.freight.ui.fragment.AddPhoneFragment;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.freight.utils.StringUtils;
import com.zl.zlibrary.dialog.PhotoDialog;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.ImageFactory;
import com.zl.zlibrary.utils.MiPictureHelper;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhanglei
 * @date 17/12/11
 * 编辑个人信息
 */
public class EditPersonDataActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.civ_user_icon)
    CircleImageView civUserIcon;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.rl_sex)
    AutoRelativeLayout rlSex;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_user_id_card_number)
    TextView tvUserIdCardNumber;
    @BindView(R.id.tv_user_standby_phone)
    TextView tvUserStandbyPhone;
    @BindView(R.id.tv_car_code)
    TextView tvCarCode;
    @BindView(R.id.tv_car_length_type)
    TextView tvCarLengthType;
    @BindView(R.id.linear_driver)
    AutoLinearLayout linearDriver;
    @BindView(R.id.edit_user_rl)
    RelativeLayout editUserRl;
    @BindView(R.id.tv_ali_account)
    TextView tvAliAccount;
    private PhotoDialog dialog;
    private String imagePath = "";
    private String idCarNumber = "130526199311146498";
    private AddPhoneFragment addPhoneFragment;
    private BaseUserEntity userData;
    private UserBean userEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person_data);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        addPhoneFragment.setOnReturnPhoneListener(new AddPhoneFragment.OnReturnPhoneListener() {
            @Override
            public void onPhone(String phone1, String phone2) {
                tvUserStandbyPhone.setText(phone1 + "," + phone2);
                updatePhone(phone1, phone2);
            }
        });
    }

    /**
     * @param phone1
     * @param phone2
     */
    private void updatePhone(String phone1, String phone2) {
        Map<String, String> params = new HashMap<>();
        userEntity.setOtherTel(phone1);
        userEntity.setOtherTel1(phone2);
        userEntity.setUserIcon("");
        params.put("UserEntityJson", GsonUtils.toJson(userEntity));
        SoapUtils.Post(mActivity, API.UpdateBaseUser, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
                Log.e("error", "");
            }

            @Override
            public void onSuccess(String data) {
                showToast("更新成功");
                Log.e("error", "");
            }
        });
    }

    private void initData() {
        getData();
    }

    /**
     * 数据获取成功，更新UI
     */
    private void upDateUi(CarUserBean userData) {
        tvUserIdCardNumber.setText(StringUtils.handleIdCardNumber(userData.getIdCardNumber()));
        tvSex.setText(StringUtils.countSex(userData.getIdCardNumber()));
        tvName.setText(userData.getRealName());
        ImageLoader.loadUserIcon(mActivity, userData.getUserIcon(), civUserIcon);
        String otherTel = userData.getOtherTel();
        String otherTel1 = userData.getOtherTel1();
        if (!TextUtils.isEmpty(otherTel)) {
            tvUserStandbyPhone.setText(otherTel);
        }
        if (!TextUtils.isEmpty(otherTel1)) {
            tvUserStandbyPhone.setText(tvUserStandbyPhone.getText().toString() + "," + otherTel1);
        }

        //如果是司机，则显示车长车型车牌号
        if (userData.getUserRole().equals("" + API.DRIVER)) {
            linearDriver.setVisibility(View.VISIBLE);
            tvCarLengthType.setText(userData.getCodeName1() + "米/" + userData.getCodeName());
            tvCarCode.setText(userData.getCarNo());
        }

        userEntity = new UserBean();
        userEntity.setId(userData.getId());
        userEntity.setReferral(userData.getReferral());
        userEntity.setReferralTel(userData.getReferralTel());
        userEntity.setRealName(userData.getRealName());
        userEntity.setUserName(userData.getUserName());
        userEntity.setPassWord(userData.getPassWord());
        userEntity.setIdCardNumber(userData.getIdCardNumber());
        userEntity.setUserRole(userData.getUserRole());
        userEntity.setIdCard1("");
        userEntity.setIdCard2("");
        userEntity.setOtherTel(userData.getOtherTel());
        userEntity.setOtherTel1(userData.getOtherTel1());
    }

    /**
     * 获取用户信息
     */
    private void getData() {
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
                    CarUserBean carUserBean = GsonUtils.fromJson(array.optString(0), CarUserBean.class);
                    upDateUi(carUserBean);
                } catch (Exception e) {

                }
            }
        });
    }

    private void initView() {
        tvTitle.setText("个人信息");
        dialog = new PhotoDialog(mActivity);
        userData = SpUtils.getUserData(mActivity);
        addPhoneFragment = AddPhoneFragment.newInstance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK) {


            switch (requestCode) {
                //从相机返回照片
                case PhotoDialog.PICK_FROM_CAMERA:
                    imagePath = dialog.imagePath;
                    break;
                //从相册返回照片
                case PhotoDialog.SELECT_PHOTO:
                    imagePath = MiPictureHelper.getPath(mActivity, data.getData());
                    break;
                case 666:
                    String ali = data.getStringExtra("ali");
                    tvAliAccount.setText(ali);
                    break;
            }
            //上传头像
            showDialog("头像上传中...");
            Observable.just(imagePath)
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(@NonNull String s) throws Exception {
                            return ImageFactory.base64Encode(ImageFactory.getimage(imagePath));
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(@NonNull String s) throws Exception {
                            uploadTouXiang(s);
                        }
                    });

        }
    }

    /**
     * 调用接口上传头像
     *
     * @param s
     */
    private void uploadTouXiang(String s) {
        ImageLoader.loadImageFile(imagePath, civUserIcon);
        Map<String, String> params = new HashMap<>();
        userEntity.setUserIcon(s);
        params.put("UserEntityJson", GsonUtils.toJson(userEntity));
        SoapUtils.Post(mActivity, API.UpdateBaseUser, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                hideDialog();
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                hideDialog();
                showToast("更新成功");
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.civ_user_icon, R.id.rl_sex, R.id.tv_user_standby_phone, R.id.tv_ali_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                finish();
                break;
            //点击用户头像
            case R.id.civ_user_icon:
                if (userEntity == null) {
                    showToast("数据未加载完成");
                    return;
                }
                dialog.show(view);
                break;
            //添加备用手机号
            case R.id.tv_user_standby_phone:
                getSupportFragmentManager().beginTransaction().addToBackStack("phone").replace(R.id.edit_user_rl, addPhoneFragment).commit();
                break;
            //添加备用手机号
            case R.id.tv_ali_account:
                startActivityForResult(new Intent(mActivity, BindAliActivity.class), 666);
                break;
        }
    }
}
