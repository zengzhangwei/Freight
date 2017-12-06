package com.zl.zlibrary.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttp网络请求工具类
 *
 * @author zhanglei
 */
public class HttpUtils {

    private static HttpUtils mHttpUtils;
    public static OkHttpClient mOkClient;

    private HttpUtils() {
        mOkClient = new OkHttpClient.Builder()
                .readTimeout(1000 * 60 * 10, TimeUnit.SECONDS)
                .writeTimeout(1000 * 60 * 10, TimeUnit.SECONDS)
                .connectTimeout(1000 * 60 * 10, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 获取一个本类的对象
     *
     * @return
     */
    public static HttpUtils getInstance() {
        if (mHttpUtils == null) {
            synchronized (HttpUtils.class) {
                if (mHttpUtils == null) {
                    mHttpUtils = new HttpUtils();
                }
            }
        }

        return mHttpUtils;
    }

    /**
     * get形式的请求方式
     *
     * @param url
     * @param callback
     */
    public void GET(final Activity activity, String url, final OnOkHttpCallback callback) {
        Request request = new Request.Builder().url(url).build();

        mOkClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onError(call.request(), e);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(string)) {
                            if (callback != null) {
                                callback.onSuccess(string);
                            }
                        }
                    }
                });
            }

        });
    }

    /**
     * 上传文件
     *
     * @param url
     * @param imagePath
     * @param params
     * @param callback
     */
    public void UpFile(String url, String imagePath, Map<String, String> params, final OnOkHttpCallback callback) {
        byte[] bytes = ImageFactory.getimage(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        //添加参数
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        //添加文件
        builder.addFormDataPart("image_file", "image.JPG", requestBody);

        final Request request = new Request.Builder().url(url).post(builder.build()).build();

        mOkClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onError(request, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    callback.onSuccess(response.body().string());
                }
            }
        });
    }

    /**
     * 上传文件
     *
     * @param url
     * @param imagePath
     * @param params
     * @param callback
     */
    public void UpFile(final Activity activity, String url, String imagePath, Map<String, String> params, final OnOkHttpCallback callback) {
        byte[] bytes = ImageFactory.getimage(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        //添加参数
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        //添加文件
        builder.addFormDataPart("image_file", "image.JPG", requestBody);

        final Request request = new Request.Builder().url(url).post(builder.build()).build();

        mOkClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (callback != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(request, e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (callback != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                callback.onSuccess(response.body().string());
                            } catch (IOException e) {
                                callback.onError(request, e);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 上传多个文件
     *
     * @param url
     * @param imagePath
     * @param params
     * @param callback
     */
    public void UpFile(String url, List<String> imagePath, Map<String, String> params, final OnOkHttpCallback callback) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        //添加参数
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        //添加文件
        for (String s : imagePath) {
            byte[] bytes = ImageFactory.getimage(s);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
            builder.addFormDataPart("image_file", "image.JPG", requestBody);
        }

        final Request request = new Request.Builder().url(url).post(builder.build()).build();

        mOkClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onError(request, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    callback.onSuccess(response.body().string());
                }
            }
        });
    }

    /**
     * get形式的请求方式
     *
     * @param url
     * @param callback
     */
    public static void Post(final Activity activity, String url, String params, final OnOkHttpCallback callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params);
        Request request = new Request.Builder().post(body).url(url).build();

        mOkClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onError(call.request(), e);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(string)) {
                            if (callback != null) {
                                callback.onSuccess(string);
                            }
                        }
                    }
                });
            }

        });
    }

    /**
     * get形式的请求方式
     *
     * @param url
     * @param callback
     */
    public void POST(final Activity activity, String url, String params, final OnOkHttpCallback callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params);
        Request request = new Request.Builder().post(body).url(url).build();

        mOkClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onError(call.request(), e);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(string)) {
                            if (callback != null) {
                                callback.onSuccess(string);
                            }
                        }
                    }
                });
            }

        });
    }

    /**
     * 下载文件
     */
    public void downLoadFile(final String url, final String filePath, final OnDownloadListener listener) {

        Request request = new Request.Builder().url(url).build();

        mOkClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.onDownloadFailed();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(filePath);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    String path = savePath + "/" + getNameFromUrl(url);
                    listener.onDownloadSuccess(path);
                } catch (Exception e) {
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });

    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }


    /**
     * 下载文件的接口
     */
    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(String filePath);

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

    /**
     * 获取数据的接口
     *
     * @author zhanglei
     */
    public interface OnOkHttpCallback {
        void onSuccess(String body);

        void onError(Request error, Exception e);
    }

    /**
     * 上传文件
     *
     * @param url
     * @param imagePath
     * @param params
     * @param callback
     */
    public void discernCard(String url, String imagePath, Map<String, String> params, final OnOkHttpCallback callback) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        byte[] bytes = zpImage(bitmap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        //添加参数
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        //添加文件
        builder.addFormDataPart("image_file", "image.JPG", requestBody);

        final Request request = new Request.Builder().url(url).post(builder.build()).build();

        mOkClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onError(request, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    callback.onSuccess(response.body().string());
                }
            }
        });
    }

    /**
     * 对图片进行质量压缩
     * 压缩到大概700K
     *
     * @param bitmap
     * @return
     */
    public byte[] zpImage(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int options = 80;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
        while ((outputStream.toByteArray().length / 1024) > 700) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
            options -= 10;
            if (options < 10) {
                break;
            }
        }

        return outputStream.toByteArray();
    }

}
