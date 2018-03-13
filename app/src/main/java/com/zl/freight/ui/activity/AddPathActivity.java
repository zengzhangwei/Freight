package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.AddressListBean;
import com.zl.freight.mode.KeyValueBean;
import com.zl.freight.ui.dialog.CarLengthDialog;
import com.zl.freight.ui.window.AddressDialog;
import com.zl.freight.ui.window.ChooseAddressWindow;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;

import java.util.HashMap;
import java.util.Map;

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
    private boolean isEnd;
    private AddressDialog addressDialog;
    private String from;
    private String to;

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
        addressDialog.setOnReturnAddressListener(new AddressDialog.OnReturnAddressListener() {
            @Override
            public void onAddress(String data) {
                //是否是目的地
                if (!isEnd) {
                    tvStartName.setText(data);
                    from = data;
                } else {
                    tvEndName.setText(data);
                    to = data;
                }
            }

            @Override
            public void onAddressDetail(String data) {

            }
        });
    }

    private void initData() {

    }

    private void initView() {
        tvTitle.setText("添加路线");
        carLengthDialog = new CarLengthDialog(mActivity, 0);
        addressDialog = new AddressDialog(mActivity, 1);
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
                addressDialog.show(view);
                isEnd = false;
                break;
            //选择目的地
            case R.id.linear_end_name:
                addressDialog.show(view);
                isEnd = true;
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

        if (TextUtils.isEmpty(from) || TextUtils.isEmpty(to)) {
            showToast("请选择完整地址");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("From", from);
        params.put("to", to);
        params.put("UserId", SpUtils.getUserData(mActivity).getId());

        SoapUtils.Post(mActivity, API.AddLine, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast("添加路线失败，请重试");
            }

            @Override
            public void onSuccess(String data) {
                showToast("添加路线成功");
                finish();
            }
        });
    }
}
