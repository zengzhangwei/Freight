package com.zl.zlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by zhanglei on 2017/4/14.
 * 图片加载类
 */

public class ImageLoader {

    public static void loadImageUrl(Activity activity, String url, ImageView imageView) {
        Glide.with(activity).load(url).into(imageView);
    }

    public static void loadImageUrl(Fragment activity, String url, ImageView imageView) {
        Glide.with(activity).load(url).into(imageView);
    }

    public static void loadImageUrl(Context activity, String url, ImageView imageView) {
        Glide.with(activity).load(url).into(imageView);
    }

}
