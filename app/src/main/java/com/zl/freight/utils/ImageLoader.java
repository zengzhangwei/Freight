package com.zl.freight.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zl.freight.R;

/**
 * Created by zhanglei on 2017/4/14.
 * 图片加载类
 */

public class ImageLoader {

    public static void loadImageUrl(Activity activity, String url, ImageView imageView) {
        Glide.with(activity).load(API.BaseUrl + url).into(imageView);
    }

    public static void loadImageUrl(Fragment activity, String url, ImageView imageView) {
        Glide.with(activity).load(API.BaseUrl + url).into(imageView);
    }

    public static void loadImageUrl(Context activity, String url, ImageView imageView) {
        Glide.with(activity).load(API.BaseUrl + url).into(imageView);
    }

    public static void loadUserIcon(Activity activity, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            ImageLoader.loadImageUrl(activity, API.BaseUrl + url, imageView);
        } else {
            imageView.setImageResource(R.mipmap.icon_touxiang);
        }
    }

}
