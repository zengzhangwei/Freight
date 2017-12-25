package com.zl.freight.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.ui.fragment.AddPhoneFragment;
import com.zl.freight.utils.SpUtils;
import com.zl.freight.utils.StringUtils;
import com.zl.zlibrary.dialog.PhotoDialog;
import com.zl.zlibrary.utils.MiPictureHelper;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private PhotoDialog dialog;
    private String imagePath = "";
    private String idCarNumber = "130526199311146498";
    private AddPhoneFragment addPhoneFragment;

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
            }
        });
    }

    private void initData() {
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        String otherTel = userData.getOtherTel();
        String otherTel1 = userData.getOtherTel1();
        if (!TextUtils.isEmpty(otherTel)) {
            tvUserStandbyPhone.setText(otherTel);
        }
        if (!TextUtils.isEmpty(otherTel1)) {
            tvUserStandbyPhone.setText(tvUserStandbyPhone.getText().toString() + "," + otherTel1);
        }
    }

    private void initView() {
        tvTitle.setText("个人信息");
        dialog = new PhotoDialog(mActivity);
        tvUserIdCardNumber.setText(StringUtils.handleIdCardNumber(idCarNumber));
        tvSex.setText(StringUtils.countSex(idCarNumber));
        tvName.setText("张磊");
        addPhoneFragment = AddPhoneFragment.newInstance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK) {
            switch (requestCode) {
                case PhotoDialog.PICK_FROM_CAMERA:
                    imagePath = dialog.imagePath;
                    civUserIcon.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    break;
                case PhotoDialog.SELECT_PHOTO:
                    imagePath = MiPictureHelper.getPath(mActivity, data.getData());
                    civUserIcon.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    break;
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.civ_user_icon, R.id.rl_sex, R.id.tv_user_standby_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                finish();
                break;
            //点击用户头像
            case R.id.civ_user_icon:
                dialog.show(view);
                break;
            //添加备用手机号
            case R.id.tv_user_standby_phone:
                getSupportFragmentManager().beginTransaction().addToBackStack("phone").replace(R.id.edit_user_rl, addPhoneFragment).commit();
                break;
        }
    }
}
