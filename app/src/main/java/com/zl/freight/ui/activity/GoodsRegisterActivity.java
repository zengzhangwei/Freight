package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.zhy.autolayout.AutoLinearLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.utils.ImageFactory;
import com.zl.zlibrary.view.MyGridView;

import java.util.ArrayList;

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
    @BindView(R.id.et_input_phone)
    EditText etInputPhone;
    @BindView(R.id.et_input_code)
    EditText etInputCode;
    @BindView(R.id.gr_img_grid)
    MyGridView grImgGrid;
    @BindView(R.id.tab_add_icon)
    FloatingActionButton tabAddIcon;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.et_input_password)
    EditText etInputPassword;
    @BindView(R.id.tv_register_add_icon)
    TextView tvRegisterAddIcon;
    @BindView(R.id.tv_register_commit)
    TextView tvRegisterCommit;
    @BindView(R.id.user_goods_register_bottom)
    AutoLinearLayout userGoodsRegisterBottom;
    @BindView(R.id.et_person_code)
    EditText etPersonCode;
    @BindView(R.id.tv_choose_address)
    TextView tvChooseAddress;
    @BindView(R.id.et_company_content)
    EditText etCompanyContent;
    @BindView(R.id.et_company_name)
    EditText etCompanyName;

    private ArrayList<String> photoList = new ArrayList<>();
    private final int REQUEST_CAMERA_CODE = 0x427;
    private UniversalAdapter<String> mAdapter;
    private double latitude;
    private double longitude;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_register);
        ButterKnife.bind(this);
        initView();
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
            }
        }
    }


    @OnClick({R.id.iv_back, R.id.tab_add_icon, R.id.tv_register_commit, R.id.tv_register_add_icon, R.id.tv_choose_address})
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
        }
    }


    private void commit() {
        String name = etInputName.getText().toString().trim();
        String phone = etInputPhone.getText().toString().trim();
        String code = etInputCode.getText().toString().trim();
        String password = etInputPassword.getText().toString().trim();
        String content = etCompanyContent.getText().toString().trim();
        String companyName = etCompanyName.getText().toString().trim();

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

        if (password.length() != 6) {
            showToast("密码长度不符合标准");
            return;
        }

        if (TextUtils.isEmpty(companyName)) {
            showToast("请输入公司名称");
            return;
        }

        if (!TextUtils.isEmpty(content)) {

        }
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
