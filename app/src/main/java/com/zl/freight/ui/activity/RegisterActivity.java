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

import java.io.Serializable;
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
    private BaseUserEntity userEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        initListener();
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
        userEntity = (BaseUserEntity) getIntent().getSerializableExtra("userEntity");
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

    private void setImage(ImageView image) {
        byte[] getimage = ImageFactory.getimage(imagePath);
        image.setImageBitmap(BitmapFactory.decodeByteArray(getimage, 0, getimage.length));
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

        if (t == null || l == null) {
            showToast("请选择车长和车型");
            return;
        }

        if (TextUtils.isEmpty(IMGBACKPATH) || TextUtils.isEmpty(IMGFRONTPATH)
                || TextUtils.isEmpty(IMGRUNPATH) || TextUtils.isEmpty(IMGDRIVINGPATH)) {
            showToast("请上传完整的图片信息");
            return;
        }

        final Map<String, String> params = new HashMap<>();
        //用户角色
        params.put("UserRole", "2");
        //此项为空
        params.put("CompanyEntityJson", "");

        //开启线程处理图片（将图片进行base64编码）
        new Thread() {
            @Override
            public void run() {
                String idCard1 = userEntity.getIdCard1();
                String idCard2 = userEntity.getIdCard2();
                String idCard1Data = ImageFactory.bitmaptoString(ImageFactory.getimage(idCard1));
                String idCard2Data = ImageFactory.bitmaptoString(ImageFactory.getimage(idCard2));
                userEntity.setIdCard1(idCard1Data);
                userEntity.setIdCard2(idCard2Data);

                //填充司机数据
                BaseCarEntity carEntity = new BaseCarEntity();
                carEntity.setCarLong(Integer.parseInt(l.getId()));
                carEntity.setCarType(Integer.parseInt(t.getId()));
                carEntity.setCarNo(carCode);
                String drivingData = ImageFactory.bitmaptoString(ImageFactory.getimage(IMGDRIVINGPATH));
                String runData = ImageFactory.bitmaptoString(ImageFactory.getimage(IMGRUNPATH));
                String frontData = ImageFactory.bitmaptoString(ImageFactory.getimage(IMGFRONTPATH));
                String backData = ImageFactory.bitmaptoString(ImageFactory.getimage(IMGBACKPATH));
                carEntity.setDrivingLlicence(drivingData);
                carEntity.setVehicleLicense(runData);
                carEntity.setCarPic1(frontData);
                carEntity.setCarPic2(backData);

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
