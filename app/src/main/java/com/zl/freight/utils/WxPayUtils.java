package com.zl.freight.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zl.zlibrary.utils.HttpUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import okhttp3.Request;

/**
 * Created by zhanglei on 2018/1/26.
 * 微信支付工具类
 */

public class WxPayUtils {
    //微信统一下单接口
    private String urlString = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private StringBuilder sb = new StringBuilder();
    private Map<String, String> resultunifiedorder;
    private PayReq req = new PayReq();
    private Activity context;

    public WxPayUtils(Activity context) {
        this.context = context;
    }

    /**
     * 支付
     */
    public void getPayData(final OnWxPayListener onWxPayListener) {
        if (API.money <= 0) {
            Toast.makeText(context, "充值金额不能为0", Toast.LENGTH_SHORT).show();
            return;
        }

        String productArgs = getProductArgs();
        HttpUtils.getInstance().POST(context, urlString, productArgs, new HttpUtils.OnOkHttpCallback() {
            @Override
            public void onSuccess(String body) {
                resultunifiedorder = decodeXml(body);
                if (resultunifiedorder.get("return_code").equals("SUCCESS")) {
                    genPayReq(onWxPayListener);
                } else {
                    Toast.makeText(context, resultunifiedorder.get("return_msg"), Toast.LENGTH_SHORT).show();
                }
                Log.e("body", body);

            }

            @Override
            public void onError(Request error, Exception e) {
                Log.e("body", "body");
            }
        });
    }

    private String genTimeStamp() {
        long timeStampSec = System.currentTimeMillis() / 1000;
        String timestamp = String.format("%010d", timeStampSec);
        return timestamp;
    }

    private void genPayReq(final OnWxPayListener onWxPayListener) {

        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        if (resultunifiedorder != null) {
            req.prepayId = resultunifiedorder.get("prepay_id");
            req.packageValue = "prepay_id=" + resultunifiedorder.get("prepay_id");
        } else {
            Toast.makeText(context, "prepayid为空", Toast.LENGTH_SHORT).show();
        }
        req.nonceStr = getNonceStr();
        req.timeStamp = genTimeStamp();

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");

        if (onWxPayListener != null) {
            onWxPayListener.wxPay(req);
        }

    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        Log.e("Simon", "----" + appSign);
        return appSign;
    }


    public Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("Simon", "----" + e.toString());
        }
        return null;

    }

    private String getProductArgs() {
        StringBuffer xml = new StringBuffer();
        try {
            String nonceStr = getNonceStr();
            xml.append("<xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", "货车多充值"));
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://www.weixin.qq.com/wxpay/pay.php"));//写你们的回调地址
            packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", getHostIP()));
//            packageParams.add(new BasicNameValuePair("total_fee", API.money + ""));
            packageParams.add(new BasicNameValuePair("total_fee", API.money * 100 + ""));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = getPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));
            String xmlString = toXml(packageParams);
            return xmlString;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    /**
     * 获取ip地址
     *
     * @return
     */
    private String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;
    }

    //生成订单号,测试用，在客户端生成
    private String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
//        Date date = new Date();
//        String key = format.format(date);
//        Random r = new Random();
//        key = key + r.nextInt(100000);
//        return key;
    }

    //生成随机号，防重发
    private String getNonceStr() {
        // TODO Auto-generated method stub
        Random random = new Random();

        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 生成签名
     */
    private String getPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);


        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("Simon", ">>>>" + packageSign);
        return packageSign;
    }

    /**
     * 转换成xml
     */
    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");


            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("Simon", ">>>>" + sb.toString());
        return sb.toString();
    }

    public interface OnWxPayListener {
        void wxPay(PayReq req);
    }

}
