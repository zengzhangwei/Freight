package com.zl.freight.ui.activity.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseCarEntity;
import com.zl.freight.mode.KeyValueBean;
import com.zl.freight.mode.UserBean;
import com.zl.freight.ui.activity.URegisterActivity;
import com.zl.freight.ui.dialog.CarLengthDialog;
import com.zl.freight.ui.fragment.PushPersonFragment;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
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
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CarDataActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.iv_driving_photo)
    ImageView ivDrivingPhoto;
    @BindView(R.id.iv_run_photo)
    ImageView ivRunPhoto;
    @BindView(R.id.iv_car_front_photo)
    ImageView ivCarFrontPhoto;
    @BindView(R.id.iv_car_back_photo)
    ImageView ivCarBackPhoto;

    @BindView(R.id.et_car_content)
    EditText etCarContent;
    @BindView(R.id.et_input_car_code)
    EditText etInputCarCode;
    @BindView(R.id.tv_choose_car_length_type)
    TextView tvChooseCarLengthType;
    private int type;
    private PhotoDialog photoDialog;
    private String imagePath = "";
    private String IMGDRIVINGPATH = "";
    private String IMGRUNPATH = "";
    private String IMGFRONTPATH = "";
    private String IMGBACKPATH = "";
    private final int DRIVINGTYPE = 0x3;
    private final int RUNTYPE = 0x4;
    private final int FRONTTYPE = 0x5;
    private final int BACKTYPE = 0x6;
    private PushPersonFragment pushPersonFragment;
    private CarLengthDialog carLengthDialog;
    private KeyValueBean l, t;
    private UserBean userEntity;
    private String idCard1;
    private String idCard2, length, carType;
    private BaseCarEntity carEntity;
    private String vehicleLicense;
    private String drivingLlicence;
    private String carPic1;
    private String carPic2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    /**
     * 初始化数据
     */
    private void initData() {

    }

    private void initListener() {
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
        userEntity = (UserBean) getIntent().getSerializableExtra("userEntity");
        carEntity = (BaseCarEntity) getIntent().getSerializableExtra("carEntity");
        idCard1 = getIntent().getStringExtra("idCard1");
        idCard2 = getIntent().getStringExtra("idCard2");
        length = getIntent().getStringExtra("length");
        carType = getIntent().getStringExtra("type");

        etInputCarCode.setText(carEntity.getCarNo());
        tvChooseCarLengthType.setText(length + "米/" + carType);
        vehicleLicense = carEntity.getVehicleLicense();
        drivingLlicence = carEntity.getDrivingLlicence();
        carPic1 = carEntity.getCarPic1();
        carPic2 = carEntity.getCarPic2();
        ImageLoader.loadImageUrl(mActivity, vehicleLicense, ivRunPhoto);
        ImageLoader.loadImageUrl(mActivity, drivingLlicence, ivDrivingPhoto);
        ImageLoader.loadImageUrl(mActivity, carPic1, ivCarFrontPhoto);
        ImageLoader.loadImageUrl(mActivity, carPic2, ivCarBackPhoto);
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

    private void setImage(final ImageView image) {
        ImageLoader.loadImageFile(imagePath, image);
        Observable.just(imagePath)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull String s) throws Exception {
                        byte[] getimage = ImageFactory.getimage(imagePath);
                        return BitmapFactory.decodeByteArray(getimage, 0, getimage.length);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {
                        image.setImageBitmap(bitmap);
                    }
                });
    }

    @OnClick({R.id.iv_back, R.id.tv_title_right, R.id.iv_driving_photo, R.id.iv_run_photo,
            R.id.iv_car_front_photo, R.id.iv_car_back_photo, R.id.tv_choose_car_length_type})
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
            //选择车长车型
            case R.id.tv_choose_car_length_type:
                carLengthDialog.show(view);
                break;
        }
    }


    /**
     * 提交注册
     */
    private void commit() {
        final String content = etCarContent.getText().toString().trim();
        final String carCode = etInputCarCode.getText().toString().trim();
        if (TextUtils.isEmpty(carCode)) {
            showToast("请输入车牌照");
            return;
        }

        final Map<String, String> params = new HashMap<>();
        //用户角色
//        params.put("UserRole", "1");
        //此项为空
        params.put("CompanyEntityJson", "");
        showNotTouchDialog("注册中，请勿退出");
        //开启线程处理图片（将图片进行base64编码）
        new Thread() {
            @Override
            public void run() {
                userEntity.setIdCard1("");
                userEntity.setIdCard2("");
                carEntity.setVehicleLicense("");
                carEntity.setDrivingLlicence("");
                carEntity.setCarPic1("");
                carEntity.setCarPic2("");
                if (!TextUtils.isEmpty(idCard1)) {
                    String idCard1Data = ImageFactory.base64Encode(ImageFactory.getimage(idCard1));
                    userEntity.setIdCard1(idCard1Data);
                }
                if (!TextUtils.isEmpty(idCard2)) {
                    String idCard2Data = ImageFactory.base64Encode(ImageFactory.getimage(idCard2));
                    userEntity.setIdCard2(idCard2Data);
                }

                //填充司机数据
                if (t != null && l != null) {
                    carEntity.setCarLong(Integer.parseInt(l.getId()));
                    carEntity.setCarType(Integer.parseInt(t.getId()));
                }
                carEntity.setCarNo(carCode);

                if (!TextUtils.isEmpty(IMGBACKPATH)) {
                    String backData = ImageFactory.base64Encode(ImageFactory.getimage(IMGBACKPATH));
                    carEntity.setCarPic2(backData);
                }
                if (!TextUtils.isEmpty(IMGFRONTPATH)) {
                    String frontData = ImageFactory.base64Encode(ImageFactory.getimage(IMGFRONTPATH));
                    carEntity.setCarPic1(frontData);
                }
                if (!TextUtils.isEmpty(IMGRUNPATH)) {
                    String runData = ImageFactory.base64Encode(ImageFactory.getimage(IMGRUNPATH));
                    carEntity.setVehicleLicense(runData);
                }
                if (!TextUtils.isEmpty(IMGDRIVINGPATH)) {
                    String drivingData = ImageFactory.base64Encode(ImageFactory.getimage(IMGDRIVINGPATH));
                    carEntity.setDrivingLlicence(drivingData);
                }
                carEntity.setCarPic3("");

                params.put("UserEntityJson", GsonUtils.toJson(userEntity));
                params.put("CarEntityJson", GsonUtils.toJson(carEntity));

                if (!TextUtils.isEmpty(content)) {

                }

                SoapUtils.Post(mActivity, API.UpdateUserInfo, params, new SoapCallback() {
                    @Override
                    public void onError(String error) {
                        hideDialog();
                        showToast(error);
                    }

                    @Override
                    public void onSuccess(String data) {
                        showToast("注册成功");
                        URegisterActivity.uRegisterActivity.finish();
                        finish();
                    }
                });
            }
        }.start();

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
