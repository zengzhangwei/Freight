package com.zl.freight.ui.activity;

import android.content.Intent;
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
import com.zl.freight.mode.AddressListBean;
import com.zl.freight.mode.BaseCompanyEntity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.ui.window.AddressDialog;
import com.zl.freight.ui.window.ChooseAddressWindow;
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
    private String address;
    private PhotoDialog photoDialog;
    private String imagePath;
    private BaseUserEntity userEntity;
    private String idCard1;
    private String idCard2;
    private String companyPic;
    private AddressDialog addressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_register);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initListener() {
        addressDialog.setOnReturnAddressListener(new AddressDialog.OnReturnAddressListener() {
            @Override
            public void onAddress(String data, String city, String county) {
                if (data.equals("全国")) {
                    showToast("请不要选择全国");
                    return;
                }
            }

            @Override
            public void onAddressDetail(String data) {
                if (TextUtils.isEmpty(data)) return;
                address = data;
                tvChooseAddress.setText(data);
            }
        });
    }

    private void initView() {
        tvTitle.setText("货主注册");
        mAdapter = new UniversalAdapter<String>(mActivity, photoList, R.layout.image_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                final ImageView view = holder.getView(R.id.iv_item);
                ImageLoader.loadImageFile(s, view);
            }
        };
        grImgGrid.setAdapter(mAdapter);
        photoDialog = new PhotoDialog(mActivity);
        userEntity = (BaseUserEntity) getIntent().getSerializableExtra("userEntity");
        idCard1 = userEntity.getIdCard1();
        idCard2 = userEntity.getIdCard2();
        addressDialog = new AddressDialog(mActivity, 1);
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
                addressDialog.show(view);
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

        if (TextUtils.isEmpty(address)) {
            showToast("请选择公司所在位置");
            return;
        }

        if (photoList.size() <= 0) {
            showToast("请添加照片");
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
                String idCard1Data = ImageFactory.base64Encode(ImageFactory.getimage(idCard1));
                String idCard2Data = ImageFactory.base64Encode(ImageFactory.getimage(idCard2));
                userEntity.setIdCard1(idCard1Data);
                userEntity.setIdCard2(idCard2Data);

                //填充公司数据
                BaseCompanyEntity companyEntity = new BaseCompanyEntity();
                companyEntity.setCompanyName(companyName);
                companyEntity.setCompanyAddress(address);
                //机构代码不为空上传机构代码
                if (!TextUtils.isEmpty(companyCode)) {
                    companyEntity.setCompanyCode(companyCode);
                }
                //营业执照不为空上传营业执照
                if (!TextUtils.isEmpty(imagePath)) {
                    companyPic = ImageFactory.base64Encode(ImageFactory.getimage(imagePath));
                    companyEntity.setCompanyPic(companyPic);
                }

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

                if (!TextUtils.isEmpty(content)) {

                }
                params.put("UserEntityJson", GsonUtils.toJson(userEntity));
                params.put("CompanyEntityJson", GsonUtils.toJson(companyEntity));

                SoapUtils.Post(mActivity, API.Register, params, new SoapCallback() {
                    @Override
                    public void onError(String error) {
                        tvRegisterCommit.setEnabled(true);
                        hideDialog();
                        showToast(error);
                    }

                    @Override
                    public void onSuccess(String data) {
                        tvRegisterCommit.setEnabled(true);
                        showToast("注册成功");
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
