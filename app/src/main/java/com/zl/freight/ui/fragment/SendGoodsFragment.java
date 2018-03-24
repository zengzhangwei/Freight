package com.zl.freight.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.zl.freight.R;
import com.zl.freight.mode.CarSendEntity;
import com.zl.freight.mode.KeyValueBean;
import com.zl.freight.ui.activity.AgreementActivity;
import com.zl.freight.ui.activity.MyMoneyActivity;
import com.zl.freight.ui.dialog.ChooseTimeDialog;
import com.zl.freight.ui.dialog.GoodsTypeDialog;
import com.zl.freight.ui.dialog.RemarkDialog;
import com.zl.freight.ui.dialog.SGCarLengthDialog;
import com.zl.freight.ui.window.AddressDialog;
import com.zl.freight.utils.API;
import com.zl.freight.utils.AddressUtils;
import com.zl.freight.utils.DoubleUtils;
import com.zl.freight.utils.LocationUtils;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.utils.GsonUtils;

import java.util.HashMap;
import java.util.Map;

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
    @BindView(R.id.tv_chong_fa)
    TextView tvChongFa;
    @BindView(R.id.tv_chang_fa)
    TextView tvChangFa;
    @BindView(R.id.tv_tong_cheng)
    TextView tvTongCheng;
    @BindView(R.id.et_info_money)
    EditText etInfoMoney;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    private SGCarLengthDialog dialog;
    private GoodsTypeDialog goodsTypeDialog;
    private RemarkDialog remarkDialog;
    private ChooseTimeDialog timeDialog;
    private final int CHOOSESTART = 0x112;
    private final int CHOOSEEND = 0x113;
    private double startLatitude = 0;
    private double startLongitude = 0;
    private String startAddress;
    private String startCity;
    private double endLatitude = 0;
    private double endLongitude = 0;
    private String endAddress;
    private String endCity;
    private String goDate = "";
    private String goTime = "";
    private String c;
    private String goodsName = "";
    private KeyValueBean l, t, u, g, z, p;
    private AlertDialog alertDialog;
    private AddressDialog addressDialog;
    private AddressUtils addressUtils;
    private int tag;
    private LocationUtils locationUtils;

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
        initListener();
        return view;
    }

    private void initListener() {
        timeDialog.setOnChooseTimeListener(new ChooseTimeDialog.OnChooseTimeListener() {
            @Override
            public void onChooseTime(String date, String time) {
                goDate = date;
                goTime = time;
                tvChooseTime.setText(date + time);
            }
        });
        goodsTypeDialog.setOnReturnDataListener(new GoodsTypeDialog.OnReturnDataListener() {
            @Override
            public void returnData(KeyValueBean data, String content) {
                g = data;
                goodsName = content;
                tvChooseType.setText(data.getCodeName() + "  " + content);
            }
        });
        dialog.setOnGetCarLengthDataListener(new SGCarLengthDialog.OnGetSGCarLengthDataListener() {
            @Override
            public void carLengthData(KeyValueBean length, KeyValueBean type, KeyValueBean yong) {
                l = length;
                t = type;
                u = yong;
                tvChooseLength.setText(length.getCodeName() + " " + type.getCodeName() + " " + yong.getCodeName());
            }
        });
        remarkDialog.setOnGetRemarkListener(new RemarkDialog.OnGetRemarkListener() {
            @Override
            public void onRemark(KeyValueBean zhuang, KeyValueBean payType, String content) {
                z = zhuang;
                p = payType;
                c = content;
                StringBuilder builder = new StringBuilder();
                if (z != null) {
                    builder.append(z.getCodeName()).append(" ");
                }

                if (p != null) {
                    builder.append(p.getCodeName()).append(" ");
                }

                if (!TextUtils.isEmpty(c)) {
                    builder.append(c);
                }

                String data = builder.toString();
                tvChooseContent.setText(data);
            }
        });
        addressDialog.setOnReturnAddressListener(new AddressDialog.OnReturnAddressListener() {
            @Override
            public void onAddress(String data, String city, String county) {
                if (data.equals("全国")) {
                    showToast("发货时请不要选择全国");
                    return;
                }
                addressUtils.search(city,county);
                switch (tag) {
                    case CHOOSESTART:
                        startCity = data;
                        tvChooseStart.setText(data);
                        break;
                    case CHOOSEEND:
                        endCity = data;
                        tvChooseEnd.setText(data);
                        break;
                }
            }

            @Override
            public void onAddressDetail(String data) {
                if (data.equals("全国")) {
                    return;
                }
                switch (tag) {
                    case CHOOSESTART:
                        startAddress = data;
                        break;
                    case CHOOSEEND:
                        endAddress = data;
                        break;
                }
            }
        });
        addressUtils.setOnAddressSearchListener(new AddressUtils.OnAddressSearchListener() {
            @Override
            public void onSearch(double x, double y) {
                switch (tag) {
                    case CHOOSESTART:
                        startLatitude = x;
                        startLongitude = y;
                        break;
                    case CHOOSEEND:
                        endLatitude = x;
                        endLongitude = y;
                        break;
                }
            }
        });
        locationUtils.setOnLocationListener(new LocationUtils.OnLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                startCity = location.getCity() + location.getDistrict();
                startAddress = location.getProvince() + location.getCity() + location.getDistrict();
                tvChooseStart.setText(location.getCity() + location.getDistrict());
                startLatitude = location.getLatitude();
                startLongitude = location.getLongitude();
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
    }

    private void initView() {
        tvTonne.setSelected(true);
        dialog = new SGCarLengthDialog(mActivity);
        goodsTypeDialog = new GoodsTypeDialog(mActivity);
        remarkDialog = new RemarkDialog(mActivity);
        timeDialog = new ChooseTimeDialog(mActivity);

        tvChongFa.setSelected(true);

        alertDialog = new AlertDialog.Builder(mActivity).setMessage("账户余额不足请充值，建议充值100元以上")
                .setPositiveButton("充值", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(mActivity, MyMoneyActivity.class));
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        addressDialog = new AddressDialog(mActivity);
        addressUtils = new AddressUtils();
        locationUtils = new LocationUtils(mActivity);
        locationUtils.startLocation();

        Spanned spanned = Html.fromHtml(" <font color=\"#1e90ff\">《货物运输协议》</font>点击查看");
        tvAgreement.setText(spanned);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_tonne, R.id.tv_square, R.id.tv_choose_start, R.id.tv_choose_end, R.id.tv_choose_length,
            R.id.tv_choose_type, R.id.tv_choose_time, R.id.tv_choose_content, R.id.tv_ok_push, R.id.tv_chong_fa,
            R.id.tv_chang_fa, R.id.tv_tong_cheng, R.id.tv_agreement})
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
                tag = CHOOSESTART;
                addressDialog.show(view);
//                startActivityForResult(new Intent(mActivity, AddressChooseActivity.class), CHOOSESTART);
                break;
            //选择目的地
            case R.id.tv_choose_end:
                tag = CHOOSEEND;
                addressDialog.show(view);
//                startActivityForResult(new Intent(mActivity, AddressChooseActivity.class), CHOOSEEND);
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
                commit();
                break;
            //智能重发
            case R.id.tv_chong_fa:
                tvChongFa.setSelected(!tvChongFa.isSelected());
                break;
            //常发货源
            case R.id.tv_chang_fa:
                tvChangFa.setSelected(!tvChangFa.isSelected());
                break;
            //同城不可见
            case R.id.tv_tong_cheng:
                tvTongCheng.setSelected(!tvTongCheng.isSelected());
                break;
            //货物运输协议
            case R.id.tv_agreement:
                startActivity(new Intent(mActivity, AgreementActivity.class));
                break;
        }
    }

    /**
     * 发布货物
     */
    private void commit() {

        if (!SpUtils.getUserData(mActivity).getIsCheck().equals("1")) {
            new AlertDialog.Builder(mActivity)
                    .setMessage("您的实名认证还未通过无法发货")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
            return;
        }

        String weight = etWeight.getText().toString().trim();
        String money = etMoney.getText().toString().trim();
        String infoMoney = etInfoMoney.getText().toString().trim();
        if (TextUtils.isEmpty(startCity) || TextUtils.isEmpty(endCity) || TextUtils.isEmpty(startAddress) || TextUtils.isEmpty(endAddress)) {
            showToast("请选择起点和终点");
            return;
        }

        if (l == null || t == null || u == null) {
            showToast("请选择车长车型");
            return;
        }

        if (TextUtils.isEmpty(weight)) {
            showToast("请输入货物重量");
            return;
        }

//        if (TextUtils.isEmpty(goDate)) {
//            showToast("请选择装车时间");
//            return;
//        }

        CarSendEntity sendEntity = new CarSendEntity();
        sendEntity.setUserId(SpUtils.getUserData(mActivity).getId());
        sendEntity.setStateFrom(startAddress);
        sendEntity.setStateTo(endAddress);
        sendEntity.setStartPlace(startCity);
        sendEntity.setEndPlace(endCity);
        sendEntity.setStartX(startLatitude + "");
        sendEntity.setStartY(startLongitude + "");
        sendEntity.setEndX(endLatitude + "");
        sendEntity.setEndY(endLongitude + "");
        sendEntity.setUseCarLong(Integer.parseInt(l.getId()));
        sendEntity.setUseCarType(Integer.parseInt(t.getId()));
        sendEntity.setUseCarClass(Integer.parseInt(u.getId()));

        if (g != null) {
            sendEntity.setGoodsType(Integer.parseInt(g.getId()));
        } else {
            sendEntity.setGoodsType(0);
        }

        sendEntity.setGoodsWeight(Double.parseDouble(weight));
        if (!TextUtils.isEmpty(money)) {
            sendEntity.setFreight(Double.parseDouble(money));
        } else {
            sendEntity.setFreight(0);
        }

        sendEntity.setGoodName(goodsName);
        sendEntity.setGoTime(goTime);
        sendEntity.setGoDate(goDate);

        if (!TextUtils.isEmpty(infoMoney)) {
            double v = Double.parseDouble(infoMoney);
            String s = DoubleUtils.format0_00(v);
            sendEntity.setInfoMoney(Double.parseDouble(s));
            sendEntity.setIsInfoPay(0);
        } else {
            sendEntity.setInfoMoney(0);
            sendEntity.setIsInfoPay(1);
        }

        if (goDate.equals("随时装货")) {
            sendEntity.setIsAnyTime(0);
        }

        //是否标记为常发货源
        if (tvChangFa.isSelected()) {
            sendEntity.setIsOften(0);
        } else {
            sendEntity.setIsOften(1);
        }

        //是否智能重发
        if (tvChongFa.isSelected()) {
            sendEntity.setIsRetry(0);
        } else {
            sendEntity.setIsRetry(1);
        }

        //是否同城不可见
        if (tvTongCheng.isSelected()) {
            sendEntity.setIsvisible(0);
        } else {
            sendEntity.setIsvisible(1);
        }

        if (tvTonne.isSelected()) {
            sendEntity.setWeightUnit("吨");
        } else {
            sendEntity.setWeightUnit("方");
        }

        //计算距离（直线距离）
        if (startLatitude == 0 || startLongitude == 0 || endLatitude == 0 || endLongitude == 0) {
            sendEntity.setRange(0.00);
        } else {
            LatLng p1 = new LatLng(startLatitude, startLongitude);
            LatLng p2 = new LatLng(endLatitude, endLongitude);
            double distance = DistanceUtil.getDistance(p1, p2);
            sendEntity.setRange(distance);
        }


        //装卸方式
        if (z != null) {
            sendEntity.setHandlingType(Integer.parseInt(z.getId()));
        }
        //支付方式
        if (p != null) {
            sendEntity.setPayType(Integer.parseInt(p.getId()));
        }

        //备注信息
        if (!TextUtils.isEmpty(c)) {
            sendEntity.setRemark(c);
        }
        Map<String, String> params = new HashMap<>();
        params.put("sendJson", GsonUtils.toJson(sendEntity));
        params.put("UserRole", SpUtils.getUserData(mActivity).getUserRole());

        showDialog("货物发布中...");
        SoapUtils.Post(mActivity, API.AddSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                hideDialog();
                if (error.equals("账户积分不足")) {
                    if (p != null && p.getCodeName().equals("在线代收")) {
                        alertDialog.setMessage("对不起您的账户余额不足，由于您选择了在线代收，需要预先将运费支付到货车多财务系统进行保管，" +
                                "当订单完成时会将运费发送至司机的账户上，请前去充值");
                    } else {
                        alertDialog.setMessage("账户余额不足请充值，建议充值100元以上");
                    }

                    alertDialog.show();
                    return;
                }
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                hideDialog();
                showToast("货物发布成功");
                resetData();
            }
        });
    }

    /**
     * 清空数据
     */
    private void resetData() {
        startLatitude = 0;
        startLongitude = 0;
        endLatitude = 0;
        endLongitude = 0;
        goDate = "";
        goTime = "";
        c = "";
        goodsName = "";
        l = null;
        t = null;
        u = null;
        g = null;
        z = null;
        p = null;
        startCity = "";
        endCity = "";
        startAddress = "";
        endAddress = "";
        tvChooseEnd.setText("");
        tvChooseStart.setText("");
        tvChooseLength.setText("");
        tvChooseType.setText("");
        etWeight.setText("");
        etMoney.setText("");
        etInfoMoney.setText("");
        tvChooseTime.setText("");
        tvChooseContent.setText("");
        tvChongFa.setSelected(true);
        tvChangFa.setSelected(false);
        tvTongCheng.setSelected(false);
    }

    /**
     * 支付发货的费用
     *
     * @param params
     */
    private void payMoney(Map<String, String> params) {

    }

    //TODO 暂时用不到了（改了业务逻辑，估计以后也用不到）
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == mActivity.RESULT_OK) {
//            double latitude = data.getDoubleExtra("latitude", 0);
//            double longitude = data.getDoubleExtra("longitude", 0);
//            String addresses = data.getStringExtra("address");
//            String city = data.getStringExtra("city");
//            switch (requestCode) {
//                case CHOOSESTART:
//                    startLatitude = latitude;
//                    startLongitude = longitude;
//                    startAddress = addresses;
//                    startCity = city;
//                    tvChooseStart.setText(city);
//                    break;
//                case CHOOSEEND:
//                    endAddress = addresses;
//                    endLatitude = latitude;
//                    endLongitude = longitude;
//                    endCity = city;
//                    tvChooseEnd.setText(city);
//                    break;
//            }
//        }
//    }

}
