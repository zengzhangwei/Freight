package com.zl.freight.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.utils.API;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FrontCameraActivity extends BaseActivity implements SurfaceHolder.Callback {


    public static final String TAG = "FrontCameraActivity";
    @BindView(R.id.fc_surfaceView)
    SurfaceView fcSurfaceView;
    @BindView(R.id.iv_take_picture)
    ImageView ivTakePicture;
    private Camera mCamera;
    private SurfaceHolder holder;
    private Camera.AutoFocusCallback focusCallback;
    private int viewWidth;
    private int viewHeight;
    private Point screenResolution;
    private Point cameraResolution;
    private int previewFormat;
    private String previewFormatString;
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_camera);
        ButterKnife.bind(this);

        //设置屏幕常亮（禁止锁屏）
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (CheckCameraHardware(this)) {
            initView();
        } else {
            Toast.makeText(this, "相机不可用...", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 检查设备是否支持摄像头
     **/
    private boolean CheckCameraHardware(Context mContext) {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // 摄像头存在
            return true;
        } else {
            // 摄像头不存在
            return false;
        }
    }

    private void initView() {
        mCamera = getCameraInstance();
        if (mCamera == null) {
            showToast("无法打开相机");
            finish();
            return;
        }

        holder = fcSurfaceView.getHolder();
        holder.addCallback(this);
        // surfaceview不维护自己的缓冲区，等待屏幕渲染引擎将内容推送到用户面前
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        /**
         * 对焦的回调
         */
        focusCallback = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                /*调用前置摄像头拍照时不知道为什么没有总是对焦失败，
                所以不管对焦成不成功我们都进行拍照*/
                mCamera.takePicture(new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {

                    }
                }, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {

                    }
                }, pictureCallback);
                //对焦成功按下快门
                if (success) {

                }
            }
        };

    }


    /**
     * 获取图片
     */
    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            storagePicture(data);
        }
    };

    /**
     * 保存照片并返回
     *
     * @param data
     */
    private void storagePicture(byte[] data) {
        showDialog("储存照片中...");
        Observable.just(data)
                .map(new Function<byte[], String>() {
                    @Override
                    public String apply(@NonNull byte[] bytes) throws Exception {
                        return storageImageAndStopCamera(bytes);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        hideDialog();
                        Intent intent = new Intent();
                        intent.putExtra("path", s);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        hideDialog();
                        showToast("储存失败请重试");
                    }
                });
    }

    /**
     * 储存拍摄的照片并停止相机的运行
     */
    private String storageImageAndStopCamera(byte[] data) {

        File file = new File(API.image_file_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        //创建一个文件流将拍的照片存储到本地
        String path = API.image_file_path + System.currentTimeMillis() + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            final Bitmap resource = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (resource == null) {
                showToast("拍照失败");
            }
            final Matrix matrix = new Matrix();
            //在这里将图片旋转90度，要不图片就是横着的
            matrix.setRotate(270);
            final Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);
            if (bitmap != null) {
                mCamera.stopPreview();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return path;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (fcSurfaceView != null) {
            viewWidth = fcSurfaceView.getWidth();
            viewHeight = fcSurfaceView.getHeight();
        }
    }

    /**
     * 官方建议的安全地访问摄像头的方法
     **/
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(1);
        } catch (Exception e) {
            Log.e("getMessage", e.getMessage());
        }
        return c;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initCamera();
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        try {
            //通过SurfaceView显示预览
            mCamera.setPreviewDisplay(holder);
            //摄像头进行旋转90°
            mCamera.setDisplayOrientation(90);
            //对相机做处理
            CameraConfigurationManager configManager = new CameraConfigurationManager(
                    getApplicationContext());
            configManager.initFromCameraParameters(mCamera);
            configManager.setDesiredCameraParameters(mCamera);
            //开始预览
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //销毁相机,释放资源
        release();
    }

    /**
     * 释放资源
     */
    private void release() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            holder = null;
            fcSurfaceView = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    //拍照
    @OnClick(R.id.iv_take_picture)
    public void onViewClicked() {
        if (mCamera == null) return;
        //自动对焦后拍照
        mCamera.autoFocus(focusCallback);
    }
}
