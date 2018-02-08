package com.zl.freight.ui.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FrontCameraActivity extends BaseActivity implements SurfaceHolder.Callback {


    @BindView(R.id.fc_surfaceView)
    SurfaceView fcSurfaceView;
    private Camera mCamera;
    private SurfaceHolder holder;
    private boolean isOk;
    private Camera.AutoFocusCallback focusCallback;
    private Camera.PreviewCallback previewCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_camera);
        ButterKnife.bind(this);

        if (CheckCameraHardware(this)) {
            initView();
        } else {
            Toast.makeText(this, "相机不可用...", Toast.LENGTH_SHORT).show();
            return;
        }

        initView();
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
        //发送空消息给Handler让其继续对焦
        focusCallback = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    //发送空消息给Handler让其继续对焦

                }
            }
        };

        /**
         * 拍照回调
         */
        previewCallback = new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                if (isOk) {

                }
            }
        };
    }

    /**
     * 官方建议的安全地访问摄像头的方法
     **/
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(0);
        } catch (Exception e) {
            Log.e("getMessage",e.getMessage());
        }
        return c;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            mCamera.setPreviewDisplay(holder);
            CameraConfigurationManager configManager = new CameraConfigurationManager(
                    getApplicationContext());
            configManager.initFromCameraParameters(mCamera);
            configManager.setDesiredCameraParameters(mCamera);
            //开始预览
            mCamera.startPreview();
            mCamera.setPreviewCallback(previewCallback);
            mCamera.autoFocus(focusCallback);
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
            isOk = false;
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
}
