package com.zl.freight.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BasePayLogEntity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.Constants;
import com.zl.freight.utils.MoneyUtils;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.utils.GsonUtils;

import java.util.HashMap;
import java.util.Map;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = resp.errCode;
            String msg = "支付失败！";
            if (code != 0) {
                showToast(msg);
                finish();
            } else {
                showToast("支付成功！");
                updateMoney();
            }
        }
    }

    /**
     * 更新余额
     */
    private void updateMoney() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        params.put("Moreorless", "0");
        params.put("Value", "" + API.money);

        SoapUtils.Post(mActivity, API.IntegralChange, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                //更新本地存储的积分
                MoneyUtils.upDateMoney(mActivity, 0, API.money);
                insertPayLog();
            }
        });
    }

    /**
     * 插入充值记录
     */
    private void insertPayLog() {
        BasePayLogEntity entity = new BasePayLogEntity();
        entity.setPayMoney(API.money);
        entity.setUserId(Integer.parseInt(SpUtils.getUserData(mActivity).getId()));
        entity.setUserName(SpUtils.getUserData(mActivity).getUserName());
        Map<String, String> params = new HashMap<>();
        params.put("PayLogJson", GsonUtils.toJson(entity));
        SoapUtils.Post(mActivity, API.InsertPayLog, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                finish();
                Log.e("error", error);
            }

            @Override
            public void onSuccess(String data) {
                finish();
                Log.e("error", data);
            }
        });
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
}