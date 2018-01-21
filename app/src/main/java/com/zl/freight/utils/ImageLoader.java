package com.zl.freight.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zl.freight.R;
import com.zl.zlibrary.utils.ImageFactory;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhanglei on 2017/4/14.
 * 图片加载类
 */

public class ImageLoader {

    public static void loadImageUrl(Activity activity, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) return;

        Glide.with(activity)
                .load(API.BaseUrl + url)
                .placeholder(R.drawable.icon_loding)
                .crossFade()
                .into(imageView);
    }

    public static void loadImageUrl(Fragment activity, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(activity)
                .load(API.BaseUrl + url)
                .placeholder(R.drawable.icon_loding)
                .crossFade()
                .into(imageView);
    }

    public static void loadImageUrl(Context activity, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(activity)
                .load(API.BaseUrl + url)
                .placeholder(R.drawable.icon_loding)
                .crossFade()
                .into(imageView);
    }

    public static void loadUserIcon(Activity activity, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            ImageLoader.loadImageUrl(activity, url, imageView);
        } else {
            imageView.setImageResource(R.mipmap.icon_touxiang);
        }
    }

    /**
     * 加载本地图片
     *
     * @param imagePath
     * @param image
     */
    public static void loadImageFile(final String imagePath, final ImageView image) {
        Observable.just(imagePath)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull String s) throws Exception {
                        byte[] getimage = ImageFactory.getimage(imagePath);
                        return BitmapFactory.decodeByteArray(getimage, 0, getimage.length);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {
                        image.setImageBitmap(bitmap);
                    }
                });
    }

}
