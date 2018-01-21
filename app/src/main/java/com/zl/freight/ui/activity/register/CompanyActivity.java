package com.zl.freight.ui.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseCompanyEntity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.UserBean;
import com.zl.freight.ui.activity.AddressChooseActivity;
import com.zl.freight.ui.activity.URegisterActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.dialog.PhotoDialog;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.ImageFactory;
import com.zl.zlibrary.utils.MiPictureHelper;
import com.zl.zlibrary.view.MyGridView;

import java.util.ArrayList;
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

public class CompanyActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.gr_img_grid)
    MyGridView grImgGrid;
    @BindView(R.id.tv_register_add_icon)
    TextView tvRegisterAddIcon;
    @BindView(R.id.tv_register_commit)
    TextView tvRegisterCommit;
    @BindView(R.id.tv_choose_address)
    TextView tvChooseAddress;
    @BindView(R.id.et_company_content)
    EditText etCompanyContent;
    @BindView(R.id.et_company_name)
    EditText etCompanyName;
    @BindView(R.id.et_company_code)
    EditText etCompanyCode;
    @BindView(R.id.iv_person_photo)
    ImageView ivPersonPhoto;

    private ArrayList<String> photoList = new ArrayList<>();
    private final int REQUEST_CAMERA_CODE = 0x427;
    private UniversalAdapter<String> mAdapter;
    private double latitude;
    private double longitude;
    private String address;
    private PhotoDialog photoDialog;
    private String imagePath;
    private UserBean userEntity;
    private String idCard1;
    private String idCard2;
    private String companyPic;
    private BaseCompanyEntity companyEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_register);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initListener() {

    }

    private void initView() {
        tvTitle.setText("货主注册");
        mAdapter = new UniversalAdapter<String>(mActivity, photoList, R.layout.image_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                ImageView view = holder.getView(R.id.iv_item);
                view.setImageBitmap(ImageFactory.getSimpeImage(s));
            }
        };
        grImgGrid.setAdapter(mAdapter);
        photoDialog = new PhotoDialog(mActivity);
        userEntity = (UserBean) getIntent().getSerializableExtra("userEntity");
        companyEntity = (BaseCompanyEntity) getIntent().getSerializableExtra("companyEntity");
        idCard1 = getIntent().getStringExtra("idCard1");
        idCard2 = getIntent().getStringExtra("idCard2");

        etCompanyName.setText(companyEntity.getCompanyName());
        etCompanyCode.setText(companyEntity.getCompanyCode());
        tvChooseAddress.setText(companyEntity.getCompanyAddress());
        ImageLoader.loadImageUrl(mActivity, companyEntity.getCompanyPic(), ivPersonPhoto);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA_CODE:
                    photoList.clear();
                    photoList.addAll(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    mAdapter.notifyDataSetChanged();
                    break;
                case 666:
                    latitude = data.getDoubleExtra("latitude", 0);
                    longitude = data.getDoubleExtra("longitude", 0);
                    address = data.getStringExtra("address");
                    tvChooseAddress.setText(address);
                    break;
                case PhotoDialog.PICK_FROM_CAMERA:
                    imagePath = photoDialog.imagePath;
                    setImage();
                    break;
                case PhotoDialog.SELECT_PHOTO:
                    imagePath = MiPictureHelper.getPath(mActivity, data.getData());
                    setImage();
                    break;
            }
        }
    }

    private void setImage() {
        ImageLoader.loadImageFile(imagePath, ivPersonPhoto);
    }

    @OnClick({R.id.iv_back, R.id.tab_add_icon, R.id.tv_register_commit, R.id.tv_register_add_icon,
            R.id.tv_choose_address, R.id.iv_person_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                finish();
                break;
            //添加图片
            case R.id.tv_register_add_icon:
                openAlbum();
                break;
            //提交注册
            case R.id.tv_register_commit:
                commit();
                break;
            //选择地址
            case R.id.tv_choose_address:
                startActivityForResult(new Intent(mActivity, AddressChooseActivity.class), 666);
                break;
            //上传营业执照
            case R.id.iv_person_photo:
                photoDialog.show(view);
                break;
        }
    }

    private void commit() {
        final String content = etCompanyContent.getText().toString().trim();
        final String companyName = etCompanyName.getText().toString().trim();
        final String companyCode = etCompanyCode.getText().toString().trim();
        if (TextUtils.isEmpty(companyName)) {
            showToast("请输入公司名称");
            return;
        }

        if (!TextUtils.isEmpty(imagePath)) {
            companyPic = ImageFactory.base64Encode(ImageFactory.getimage(imagePath));
            return;
        }

        final Map<String, String> params = new HashMap<>();
        //用户角色
        params.put("UserRole", "2");
        //此项为空
        params.put("CarEntityJson", "");
        tvRegisterCommit.setEnabled(false);
        showNotTouchDialog("注册中，请勿退出");
        new Thread() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(idCard1)) {
                    String idCard1Data = ImageFactory.base64Encode(ImageFactory.getimage(idCard1));
                    userEntity.setIdCard1(idCard1Data);
                }
                if (!TextUtils.isEmpty(idCard2)) {
                    String idCard2Data = ImageFactory.base64Encode(ImageFactory.getimage(idCard2));
                    userEntity.setIdCard2(idCard2Data);
                }

                companyEntity.setCompanyName(companyName);
                if (!TextUtils.isEmpty(address)) {
                    companyEntity.setCompanyAddress(address);
                    return;
                }

                //机构代码不为空时必须添加营业执照
                if (!TextUtils.isEmpty(companyCode)) {
                    companyEntity.setCompanyCode(companyCode);
                }

                if (!TextUtils.isEmpty(companyPic)) {
                    companyEntity.setCompanyPic(companyPic);

                }

                if (photoList.size() > 0) {
                    for (int i = 0; i < photoList.size(); i++) {
                        String s = ImageFactory.base64Encode(ImageFactory.getimage(photoList.get(i)));
                        switch (i) {
                            case 0:
                                companyEntity.setStorePic(s);
                                break;
                            case 1:
                                companyEntity.setStorePic1(s);
                                break;
                            case 2:
                                companyEntity.setStorePic2(s);
                                break;
                        }
                    }
                }

                if (!TextUtils.isEmpty(content)) {

                }
                params.put("UserEntityJson", GsonUtils.toJson(userEntity));
                params.put("CompanyEntityJson", GsonUtils.toJson(companyEntity));

                SoapUtils.Post(mActivity, API.UpdateUserInfo, params, new SoapCallback() {
                    @Override
                    public void onError(String error) {
                        tvRegisterCommit.setEnabled(true);
                        hideDialog();
                        showToast(error);
                    }

                    @Override
                    public void onSuccess(String data) {
                        tvRegisterCommit.setEnabled(true);
                        showToast("信息修改成功");
                        URegisterActivity.uRegisterActivity.finish();
                        finish();
                    }
                });
            }
        }.start();
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