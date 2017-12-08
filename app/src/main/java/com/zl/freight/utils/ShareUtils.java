package com.zl.freight.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by zhanglei on 2017\12\8 0008.
 */

public class ShareUtils {

    public static void share(Context context){
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_TEXT, "给你一个网站要不：http://www.baidu.com");//添加分享内容
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "分享到");
        context.startActivity(share_intent);
    }

}
