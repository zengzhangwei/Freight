package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.KeyValueBean;
import com.zl.freight.ui.dialog.CarLengthDialog;
import com.zl.freight.ui.window.ChooseAddressWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 18/2/7
 * 添加路线
 */
public class AddPathActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_start_name)
    TextView tvStartName;
    @BindView(R.id.linear_start_name)
    AutoRelativeLayout linearStartName;
    @BindView(R.id.tv_end_name)
    TextView tvEndName;
    @BindView(R.id.linear_end_name)
    AutoRelativeLayout linearEndName;
    @BindView(R.id.tv_car_length_type_name)
    TextView tvCarLengthTypeName;
    @BindView(R.id.linear_car_length_type)
    AutoRelativeLayout linearCarLengthType;
    @BindView(R.id.tv_add_path)
    TextView tvAddPath;
    private KeyValueBean mLength, mType;
    private CarLengthDialog carLengthDialog;
    private ChooseAddressWindow addressWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_path);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        carLengthDialog.setOnGetCarLengthDataListener(new CarLengthDialog.OnGetCarLengthDataListener() {
            @Override
            public void carLengthData(KeyValueBean length, KeyValueBean type, KeyValueBean goodsType) {
                mLength = length;
                mType = type;
                tvCarLengthTypeName.setText(length.getCodeName() + "米/" + type.getCodeName());
            }
        });
        addressWindow.setOnOkClickListener(new ChooseAddressWindow.OnOkClickListener() {
            @Override
            public void onClickOk(int[] indexs, String address) {

            }
        });
    }

    private void initData() {

    }

    private void initView() {
        tvTitle.setText("添加路线");
        carLengthDialog = new CarLengthDialog(mActivity, 0);
        addressWindow = new ChooseAddressWindow(mActivity);
    }

    @OnClick({R.id.iv_back, R.id.linear_start_name, R.id.linear_end_name, R.id.linear_car_length_type, R.id.tv_add_path})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                finish();
                break;
            //选择出发地
            case R.id.linear_start_name:
                addressWindow.showWindow(view);
                break;
            //选择目的地
            case R.id.linear_end_name:
                addressWindow.showWindow(view);
                break;
            //选择车长和车型
            case R.id.linear_car_length_type:
                carLengthDialog.show(view);
                break;
            //添加
            case R.id.tv_add_path:
                commit();
                break;
        }
    }

    /**
     * 添加
     */
    private void commit() {
        finish();
    }
}
