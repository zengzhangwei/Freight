package com.zl.freight.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zl.freight.R;
import com.zl.freight.alipay.AuthResult;
import com.zl.freight.alipay.PayResult;
import com.zl.freight.alipay.util.OrderInfoUtil2_0;
import com.zl.freight.mode.PayTypeMode;
import com.zl.freight.utils.API;
import com.zl.freight.utils.Constants;
import com.zl.freight.utils.WxPayUtils;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/22.
 */

public class PayTypeDialog extends BaseDialog {

    private double payMoney;
    private List<PayTypeMode> mList = new ArrayList<>();
    private UniversalAdapter<PayTypeMode> mAdapter;
    private ListView listView;

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2018011301829574";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088921605246931";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = System.currentTimeMillis() + "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCJF0Y2IWQj2EXHmhzuXAkj5nBNhKj9KMc7d7CXwlvGrWawjYLQe6SXW7o8YoCuUh/fk7R00LnOAIWvuHfijw9bDEIa6AwECDBDGtfyK3uvsjdESGt4XN5sqMrMD/1yAT4iT2GaFj2DFTYqErb3NMMi++6rTb5iXpF8ahUG655OjgUHTWmV8PYjOWHW317goiegCypQFixrqRiXmCldN1f5omChRUpojaYAnI7jKNtVeXJVTpXB4y6y6bw845sIGpkMpT85OSBZHcaxrZp0lWLkeVB50kpVAeoidXH+ws1zFBf5vpl+yFYmUrEH9KVid/4W8H9k5N1dIGlp6J577qbhAgMBAAECggEAWEUqKjR53DKnS4xJ0JhmMj1wVhlzDf6xufKpPC4jFdwU4N1dO4xBQEwJsFPkc1nht/3NFeJHoQLpEWn/l1tyEbfk4JpFEluLZ8ni/cVN0KU5vLe8qzVw6hoM6jPphvyR21mtiJ+FX9/KjnCaarYIZ4A7/wUB7yjhGHDdtSUFO6J4ZPDFDrUZQ17/MKqV0cQHS3bhDcyvk1mmYYPVJG+5UFFUodPKHsQg0a7PNmFXEbWKlW2sIDnpuOchfcwvFkk3q+c6HBCvoqjuBG4H15N0Rb3VwVond9BK0Q36szHpfT0lJiIFQDy97utNYblnQVZ6ZfcAEoIcATCPR0ukLjaNuQKBgQDJvu3i6S63AAQuwWktsz5Ioeabzh63sY8iV5Cnd79wxIhOi39nhKgHOmZiqgzGtrVFIBcAufhthjpBqNg8+BFPOQSraqU20C9UyRpmjccB8xkBB6vhYftgLc/S5xRWEyLGMb2i1Oa92GBhUtIRMJF8vMn6jz+03uS//sYx3mfHIwKBgQCt9Td8fG5VRAn0AUX1mc1HTjvG0GjrA5jtGDTk3alS0TRk4zw/KRE3tAate9n7Vxdig/82sgBEZzL2+aNndoxpt999A0eoCx44wKTkM1KoFcy2SuALSvHZC1fu/sGMPK30Ae7dSpYmMW0Da19DetjnzYeJTXXMg9XP7i4vQhw8KwKBgAZPhJ1ucdmjlH3p6uJVEwOunMoO3oMceL+4QXwbkM90NldOocOZMLBZMU6wxwmIOY9Z+544Mv8qDbR0FZ+CmvgBbpX2Fgjq5jAewkam9Kyo8AVKOiQornZJuJB+TfKLUrEkaSwowLb7h7hDoPKMtGBUf3To+jMsLbebvpNcw55FAoGAIjpQm5bkIrTLfI5PlJ6ZjIExM9JIAo3npicQKUQQD/KH653mecr7z0kF01a+Mj0VpdWw/rMRaGQh3ASMiUwDHr4GJFPj4oWlgacuCpSqm9YGPIwI/0/UvE8GE0lfDdfWpl9G/5Wwa31oJLcYvG9MhHelRnfOSNpb1JjZz4Sjh5cCgYEAw8l86U46ray+brsm/kSoI3tzr3F4u5HLuyule5I3fvcB2/BAWbKaZwwqC2S1Dq79hA4bIMMZx7fBsqJRQaVp7URWz+XTBypwfXdNfOQaM1kBRDjR4yHwtUQxfXeH85rN1O6tWzfFnxALFB8OvybLiOUcaSBFqhOVIStxHljabNI=";
    public static final String RSA_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCJF0Y2IWQj2EXHmhzuXAkj5nBNhKj9KMc7d7CXwlvGrWawjYLQe6SXW7o8YoCuUh/fk7R00LnOAIWvuHfijw9bDEIa6AwECDBDGtfyK3uvsjdESGt4XN5sqMrMD/1yAT4iT2GaFj2DFTYqErb3NMMi++6rTb5iXpF8ahUG655OjgUHTWmV8PYjOWHW317goiegCypQFixrqRiXmCldN1f5omChRUpojaYAnI7jKNtVeXJVTpXB4y6y6bw845sIGpkMpT85OSBZHcaxrZp0lWLkeVB50kpVAeoidXH+ws1zFBf5vpl+yFYmUrEH9KVid/4W8H9k5N1dIGlp6J577qbhAgMBAAECggEAWEUqKjR53DKnS4xJ0JhmMj1wVhlzDf6xufKpPC4jFdwU4N1dO4xBQEwJsFPkc1nht/3NFeJHoQLpEWn/l1tyEbfk4JpFEluLZ8ni/cVN0KU5vLe8qzVw6hoM6jPphvyR21mtiJ+FX9/KjnCaarYIZ4A7/wUB7yjhGHDdtSUFO6J4ZPDFDrUZQ17/MKqV0cQHS3bhDcyvk1mmYYPVJG+5UFFUodPKHsQg0a7PNmFXEbWKlW2sIDnpuOchfcwvFkk3q+c6HBCvoqjuBG4H15N0Rb3VwVond9BK0Q36szHpfT0lJiIFQDy97utNYblnQVZ6ZfcAEoIcATCPR0ukLjaNuQKBgQDJvu3i6S63AAQuwWktsz5Ioeabzh63sY8iV5Cnd79wxIhOi39nhKgHOmZiqgzGtrVFIBcAufhthjpBqNg8+BFPOQSraqU20C9UyRpmjccB8xkBB6vhYftgLc/S5xRWEyLGMb2i1Oa92GBhUtIRMJF8vMn6jz+03uS//sYx3mfHIwKBgQCt9Td8fG5VRAn0AUX1mc1HTjvG0GjrA5jtGDTk3alS0TRk4zw/KRE3tAate9n7Vxdig/82sgBEZzL2+aNndoxpt999A0eoCx44wKTkM1KoFcy2SuALSvHZC1fu/sGMPK30Ae7dSpYmMW0Da19DetjnzYeJTXXMg9XP7i4vQhw8KwKBgAZPhJ1ucdmjlH3p6uJVEwOunMoO3oMceL+4QXwbkM90NldOocOZMLBZMU6wxwmIOY9Z+544Mv8qDbR0FZ+CmvgBbpX2Fgjq5jAewkam9Kyo8AVKOiQornZJuJB+TfKLUrEkaSwowLb7h7hDoPKMtGBUf3To+jMsLbebvpNcw55FAoGAIjpQm5bkIrTLfI5PlJ6ZjIExM9JIAo3npicQKUQQD/KH653mecr7z0kF01a+Mj0VpdWw/rMRaGQh3ASMiUwDHr4GJFPj4oWlgacuCpSqm9YGPIwI/0/UvE8GE0lfDdfWpl9G/5Wwa31oJLcYvG9MhHelRnfOSNpb1JjZz4Sjh5cCgYEAw8l86U46ray+brsm/kSoI3tzr3F4u5HLuyule5I3fvcB2/BAWbKaZwwqC2S1Dq79hA4bIMMZx7fBsqJRQaVp7URWz+XTBypwfXdNfOQaM1kBRDjR4yHwtUQxfXeH85rN1O6tWzfFnxALFB8OvybLiOUcaSBFqhOVIStxHljabNI=";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        if (onReturnPayListener != null) {
                            onReturnPayListener.onSuccess();
                        }
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if (onReturnPayListener != null) {
                            onReturnPayListener.onError("支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    public PayTypeDialog(Activity mActivity, double payMoney) {
        super(mActivity);
        this.payMoney = payMoney;
        initView();
        initData();
        initListener();
    }

    private void initData() {
        PayTypeMode typeMode = new PayTypeMode();
        typeMode.setIcon(R.mipmap.icon_alipay);
        typeMode.setPayType("aliPay");
        typeMode.setPayTypeName("支付保");
        PayTypeMode wxPay = new PayTypeMode();
        wxPay.setIcon(R.mipmap.icon_wx);
        wxPay.setPayType("wxPay");
        wxPay.setPayTypeName("微信支付");
        mList.add(typeMode);
        mList.add(wxPay);
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.listview_layout, null);
        listView = inflate.findViewById(R.id.listView);
        mAdapter = new UniversalAdapter<PayTypeMode>(mActivity, mList, R.layout.pay_type_item) {
            @Override
            public void convert(UniversalViewHolder holder, int position, PayTypeMode payTypeMode) {
                ImageView icon = holder.getView(R.id.iv_pay_icon);
                icon.setImageResource(payTypeMode.getIcon());
                holder.setText(R.id.tv_pay_name, payTypeMode.getPayTypeName());
            }
        };
        listView.setAdapter(mAdapter);
        initPopupWindow(inflate);
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dismissDialog();
                switch (mList.get(i).getPayType()) {
                    case "aliPay":
                        payV2();
                        break;
                    case "wxPay":
                        weiXinPay();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 吊起微信支付
     */
    private void weiXinPay() {
        API.money = (int) payMoney;
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(mActivity, Constants.APP_ID);
        // 将该app注册到微信
        msgApi.registerApp(Constants.APP_ID);
        new WxPayUtils(mActivity).getPayData(new WxPayUtils.OnWxPayListener() {
            @Override
            public void wxPay(PayReq req) {
                //发起微信支付
                msgApi.sendReq(req);
            }
        });
    }

    /**
     * 支付宝支付业务
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(mActivity).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            mActivity.finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2, payMoney);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private OnReturnPayListener onReturnPayListener;

    public void setOnReturnPayListener(OnReturnPayListener onReturnPayListener) {
        this.onReturnPayListener = onReturnPayListener;
    }

    public interface OnReturnPayListener {
        void onError(String error);

        void onSuccess();
    }

}
