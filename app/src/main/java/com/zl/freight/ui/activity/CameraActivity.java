package com.zl.freight.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.utils.API;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author zhanglei
 * @date 16/12/10
 * 相机Activity
 */
public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private AutoRelativeLayout activity_camera;
    private Camera mCamera;
    private boolean isOk = false;
    private SurfaceView sv;
    private SurfaceHolder holder;
    private Camera.AutoFocusCallback focusCallback;
    private Camera.PreviewCallback previewCallback;
    private ImageView km;

    public static final int CAMERA_CODE = 4;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mCamera.autoFocus(focusCallback);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
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

        initListener();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        km.setOnClickListener(this);
        activity_camera.setOnClickListener(this);
    }

    private void initView() {

        /**
         * 对焦的回调
         */
        focusCallback = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    //发送空消息给Handler让其继续对焦
                    handler.sendEmptyMessageAtTime(1, 1500);
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
                    storageImageAndStopCamera(data);
                }
            }
        };

        //获取相机类并打开
        mCamera = getCameraInstance();
        //获取控件
        activity_camera = (AutoRelativeLayout) findViewById(R.id.activity_camera);
        sv = (SurfaceView) findViewById(R.id.camera_sv);
        km = (ImageView) findViewById(R.id.camera_km);

        if (mCamera == null) {
            showToast("无法打开相机");
            finish();
            return;
        }

        holder = sv.getHolder();
        holder.addCallback(this);
        // surfaceview不维护自己的缓冲区，等待屏幕渲染引擎将内容推送到用户面前
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    /**
     * 储存拍摄的照片并停止相机的运行
     */
    private void storageImageAndStopCamera(byte[] data) {
        isOk = false;
        Log.e("isok==", "" + data.length);
        mCamera.setPreviewCallback(null);
        //拍照成功，存储照片
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(API.image_file_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        //将预览图通过以下方法转换成可识别的图片
        Camera.Size size = mCamera.getParameters().getPreviewSize(); //获取预览大小
        final int w = size.width;  //宽度
        final int h = size.height;
        final YuvImage image = new YuvImage(data, ImageFormat.NV21, w, h, null);
        ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
        image.compressToJpeg(new Rect(0, 0, w, h), 100, os);
        byte[] tmp = os.toByteArray();

        //创建一个文件流将拍的照片存储到本地
//        String format = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss").format(new Date(System.currentTimeMillis()));
        String path = API.image_file_path + System.currentTimeMillis() + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            fos.write(tmp);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

        Intent intent = new Intent();
        intent.putExtra("path", path);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 官方建议的安全地访问摄像头的方法
     **/
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(0);
        } catch (Exception e) {

        }
        return c;
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

    @SuppressWarnings("deprecation")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
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
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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
            sv = null;
        }
    }

    /**
     * 处理点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.camera_km) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        isOk = true;
                    }
                }
            });
        }
//        else if (i == R.id.activity_camera) {
//            mCamera.autoFocus(new Camera.AutoFocusCallback() {
//                @Override
//                public void onAutoFocus(boolean success, Camera camera) {
//                    if (success) {
//                        isOk = true;
//                    }
//                }
//            });
//
//        }
    }
}
