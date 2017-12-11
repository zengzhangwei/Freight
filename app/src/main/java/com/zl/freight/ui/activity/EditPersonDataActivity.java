package com.zl.freight.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.zlibrary.dialog.PhotoDialog;
import com.zl.zlibrary.utils.MiPictureHelper;

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
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;
    private String[] sexs = {"男", "女"};
    private AlertDialog sexDialog;
    private PhotoDialog dialog;
    private String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person_data);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        tvTitle.setText("个人信息");
        sexDialog = new AlertDialog.Builder(mActivity).setTitle("选择性别").setSingleChoiceItems(sexs, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvSex.setText(i == 0 ? "男" : "女");
                sexDialog.dismiss();
            }
        }).create();
        dialog = new PhotoDialog(mActivity);
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

    @OnClick({R.id.iv_back, R.id.civ_user_icon, R.id.rl_sex})
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
            //修改性别
            case R.id.rl_sex:
                sexDialog.show();
                break;
        }
    }
}
