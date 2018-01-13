package com.zl.freight.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zl.freight.base.VersionBean;
import com.zl.zlibrary.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Request;

/**
 * Created by zhanglei on 2017/4/19.
 * 软件更新的管理类
 */

public class UpdateManager {

    private Activity context;
    private final ProgressDialog dialog;
    private final AlertDialog.Builder builder;
    private VersionBean versionBean;

    public UpdateManager(final Activity context) {
        this.context = context;

        builder = new AlertDialog.Builder(context);
        builder.setMessage("软件已升级，是否更新").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startUpdate();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.finish();

            }
        });

        dialog = new ProgressDialog(context);
        dialog.setTitle("程序更新中...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    /**
     * 判断APP是否需要更新
     *
     * @return
     */
    public void isUpdate() {

        try {
            //获取当前APP的版本号
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            HttpUtils.getInstance().GET(context, API.BaseUrl, new HttpUtils.OnOkHttpCallback() {

                @Override
                public void onSuccess(String body) {
                    try {
                        JSONObject object = new JSONObject(body);
                        Log.e("body", body);
                        versionBean = new Gson().fromJson(object.toString(), VersionBean.class);

                        if (packageInfo.versionCode < versionBean.getVisionCode()) {
                            builder.setMessage("有新的版本可更新，请更新");
                            builder.show().setCanceledOnTouchOutside(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Request error, Exception e) {
                    Log.e("error", "eee");
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断APP是否需要更新(用户主动点击时触发的)
     *
     * @return
     */
    public void isVUpdate() {

        try {
            //获取当前APP的版本号
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            HttpUtils.getInstance().GET(context, API.BaseUrl, new HttpUtils.OnOkHttpCallback() {

                @Override
                public void onSuccess(String body) {
                    try {
                        JSONObject object = new JSONObject(body);
                        Log.e("body", body);
                        versionBean = new Gson().fromJson(object.toString(), VersionBean.class);

                        if (packageInfo.versionCode < versionBean.getVisionCode()) {
                            builder.setMessage("有新的版本可更新，请更新");
                            builder.show().setCanceledOnTouchOutside(false);
                        } else {
                            Toast.makeText(context, "已是最新版本", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Request error, Exception e) {
                    Log.e("error", "eee");
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 开始更新
     */
    private void startUpdate() {
        dialog.show();
        HttpUtils.getInstance().downLoadFile(versionBean.getUrl(), "WitMiDownLoad", new HttpUtils.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(final String filePath) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        install(filePath);
                    }
                });
            }

            @Override
            public void onDownloading(final int progress) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setProgress(progress);
                    }
                });
            }

            @Override
            public void onDownloadFailed() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    /**
     * 安装 APK
     */
    public void install(String mUrl) {
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(context, "com.zl.makecard.fileprovider", new File(mUrl));//在AndroidManifest中的android:authorities值
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(new File(mUrl)), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
    }

}
