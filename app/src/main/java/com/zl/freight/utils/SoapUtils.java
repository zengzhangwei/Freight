package com.zl.freight.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

/**
 * Created by zhanglei on 2017\12\21 0021.
 */

public class SoapUtils {

    // 命名空间
    public static final String nameSpace = "http://tempuri.org/";
    // EndPoint
    public static final String endPoint = "http://172.16.18.17/WebService1.asmx";

    /**
     * @param mActivity
     * @param method    调用的方法名称
     */
    public static void Post(final Activity mActivity, final String method, final Map<String, String> params, final SoapCallback callback) {
        new Thread() {
            @Override
            public void run() {

                // SOAP Action
                String soapAction = nameSpace + method;

                // 指定WebService的命名空间和调用的方法名
                SoapObject rpc = new SoapObject(nameSpace, method);

                // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
                Set<Map.Entry<String, String>> entries = params.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    rpc.addProperty(entry.getKey(), entry.getValue());
                }

                // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = rpc;
                // 设置是否调用的是dotNet开发的WebService
                envelope.dotNet = true;

                HttpTransportSE transport = new HttpTransportSE(endPoint, 1000 * 60);
                try {
                    // 调用WebService
                    transport.call(soapAction, envelope);
                    // 获取返回的数据
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    // 获取返回的结果
                    final String result = response.toString();
                    if (callback != null) {
                        mActivity.runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                callback.onSuccess(result);
                            }
                        });
                    }
                } catch (final SoapFault soapFault) {
                    soapFault.printStackTrace();
                    if (callback != null) {
                        mActivity.runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                String message = soapFault.getMessage();
                                callback.onError(TextUtils.isEmpty(message) ? "未知的错误" : message);
                            }
                        });
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        mActivity.runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                String message = e.getMessage();
                                callback.onError(TextUtils.isEmpty(message) ? "未知的错误" : message);
                            }
                        });
                    }
                }
            }
        }.start();
    }

    /**
     * @param method 调用的方法名称
     */
    public static void Post(final String method, final Map<String, String> params, final SoapCallback callback) {
        new Thread() {
            @Override
            public void run() {

                // SOAP Action
                String soapAction = nameSpace + method;

                // 指定WebService的命名空间和调用的方法名
                SoapObject rpc = new SoapObject(nameSpace, method);

                // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
                Set<Map.Entry<String, String>> entries = params.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    rpc.addProperty(entry.getKey(), entry.getValue());
                }

                // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = rpc;
                // 设置是否调用的是dotNet开发的WebService
                envelope.dotNet = true;

                HttpTransportSE transport = new HttpTransportSE(endPoint);
                try {
                    // 调用WebService
                    transport.call(soapAction, envelope);
                    // 获取返回的数据
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    // 获取返回的结果
                    final String result = response.toString();
                    if (callback != null) {
                        callback.onSuccess(result);
                    }
                } catch (final SoapFault soapFault) {
                    soapFault.printStackTrace();
                    if (callback != null) {
                        callback.onError(soapFault.getMessage());
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.onError(e.getMessage());
                    }
                }
            }
        }.start();
    }

}
