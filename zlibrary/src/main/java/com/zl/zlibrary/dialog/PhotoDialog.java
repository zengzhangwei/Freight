package com.zl.zlibrary.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zl.zlibrary.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/24.
 */

public class PhotoDialog implements View.OnClickListener {

    private Activity context;
    private PopupWindow popupWindow;
    private TextView tvCamera;
    private TextView tvPhoto;
    private TextView tvCancel;
    //相册
    public static final int SELECT_PHOTO = 0x123;
    //相机
    public static final int PICK_FROM_CAMERA = 0x234;
    public String imagePath;

    public PhotoDialog(Activity context) {
        this.context = context;
        initView();
        initListener();
    }

    private void initListener() {
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1.0f);
            }
        });
        tvCamera.setOnClickListener(this);
        tvPhoto.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.choose_photo_layout, null);
        tvCamera = view.findViewById(R.id.tv_camera);
        tvPhoto = view.findViewById(R.id.tv_photo);
        tvCancel = view.findViewById(R.id.tv_cancel);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.WindowAnim);
    }

    public void show(View view) {
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        setAlpha(0.6f);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_camera) {
            getCameraPhoto();
        } else if (i == R.id.tv_photo) {
            openAlbum();
        }
        popupWindow.dismiss();
    }

    /**
     * 进行拍照
     */
    protected void getCameraPhoto() {

        imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Ybjk";
        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        long time = System.currentTimeMillis();
        String imageName = new SimpleDateFormat("yyMMddHHmmss").format(new Date(time));
        imagePath = imagePath + "/" + imageName + ".jpg";
        File imageFile = new File(imagePath);
        Uri uri = Uri.fromFile(imageFile);

        //拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        context.startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    /**
     * 打开相册的方法
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        context.startActivityForResult(intent, SELECT_PHOTO);
    }

    /**
     * 设置界面的透明度
     */
    private void setAlpha(float f) {
        Window window = context.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = f;
        window.setAttributes(attributes);
    }
}
