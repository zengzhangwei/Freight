package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.ui.activity.AddressChooseActivity;
import com.zl.freight.ui.dialog.ChooseTimeDialog;
import com.zl.freight.ui.dialog.GoodsTypeDialog;
import com.zl.freight.ui.dialog.RemarkDialog;
import com.zl.freight.ui.dialog.SGCarLengthDialog;
import com.zl.zlibrary.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 17/12/7
 * 发货界面
 */
public class SendGoodsFragment extends BaseFragment {

    @BindView(R.id.tv_tonne)
    TextView tvTonne;
    @BindView(R.id.tv_square)
    TextView tvSquare;
    Unbinder unbinder;
    @BindView(R.id.tv_choose_start)
    TextView tvChooseStart;
    @BindView(R.id.tv_choose_end)
    TextView tvChooseEnd;
    @BindView(R.id.tv_choose_length)
    TextView tvChooseLength;
    @BindView(R.id.tv_choose_type)
    TextView tvChooseType;
    @BindView(R.id.et_weight)
    EditText etWeight;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.tv_choose_time)
    TextView tvChooseTime;
    @BindView(R.id.tv_choose_content)
    TextView tvChooseContent;
    @BindView(R.id.tv_ok_push)
    TextView tvOkPush;
    private SGCarLengthDialog dialog;
    private GoodsTypeDialog goodsTypeDialog;
    private RemarkDialog remarkDialog;
    private ChooseTimeDialog timeDialog;
    private final int CHOOSESTART = 0x112;
    private final int CHOOSEEND = 0x113;
    private double startLatitude = 0;
    private double startLongitude = 0;
    private String startAddress;
    private double endLatitude = 0;
    private double endLongitude = 0;
    private String endAddress;

    public SendGoodsFragment() {
        // Required empty public constructor
    }

    public static SendGoodsFragment newInstance() {

        Bundle args = new Bundle();

        SendGoodsFragment fragment = new SendGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_goods, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvTonne.setSelected(true);
        dialog = new SGCarLengthDialog(mActivity);
        goodsTypeDialog = new GoodsTypeDialog(mActivity);
        remarkDialog = new RemarkDialog(mActivity);
        timeDialog = new ChooseTimeDialog(mActivity);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_tonne, R.id.tv_square, R.id.tv_choose_start, R.id.tv_choose_end, R.id.tv_choose_length, R.id.tv_choose_type, R.id.tv_choose_time, R.id.tv_choose_content, R.id.tv_ok_push})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //选择吨
            case R.id.tv_tonne:
                tvTonne.setSelected(true);
                tvSquare.setSelected(false);
                break;
            //选择方
            case R.id.tv_square:
                tvTonne.setSelected(false);
                tvSquare.setSelected(true);
                break;
            //选择始发地
            case R.id.tv_choose_start:
                startActivityForResult(new Intent(mActivity, AddressChooseActivity.class), CHOOSESTART);
                break;
            //选择目的地
            case R.id.tv_choose_end:
                startActivityForResult(new Intent(mActivity, AddressChooseActivity.class), CHOOSEEND);
                break;
            //选择货车长度
            case R.id.tv_choose_length:
                dialog.show(view);
                break;
            //选择货物类型
            case R.id.tv_choose_type:
                goodsTypeDialog.show(view);
                break;
            //选择装货时间
            case R.id.tv_choose_time:
                timeDialog.showDialog(view);
                break;
            //选择备注
            case R.id.tv_choose_content:
                remarkDialog.show(view);
                break;
            //确认发布
            case R.id.tv_ok_push:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            String addresses = data.getStringExtra("address");
            switch (requestCode) {
                case CHOOSESTART:
                    startLatitude = latitude;
                    startLongitude = longitude;
                    startAddress = addresses;
                    tvChooseStart.setText(addresses);
                    break;
                case CHOOSEEND:
                    endAddress = addresses;
                    endLatitude = latitude;
                    endLongitude = longitude;
                    tvChooseEnd.setText(addresses);
                    break;
            }
        }
    }
}
