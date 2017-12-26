package com.zl.freight.ui.view;

import android.util.Log;

import com.zl.zlibrary.utils.GzipUtils;
import com.zl.zlibrary.utils.ImageFactory;

/**
 * Created by Administrator on 2017/12/8.
 */

public class v {

    public static void main(String[] args) {
        testZip();
    }

    /**
     * 测试zip压缩
     */
    private static void testZip() {
        String name = "张磊是世界上最帅的人";
        //进行base64编码
        String s = ImageFactory.base64Encode(name.getBytes());
        Log.e("testZip", s);
        //进行压缩
        byte[] nameByte = GzipUtils.gZip(s.getBytes());
        //将压缩完的数组变成字符串
        String zipStr = new String(nameByte);
        //将字符串解压缩
        byte[] bytes = GzipUtils.unGZip(zipStr.getBytes());
        //将解压缩完的byte数组变成字符串
        String s1 = new String(bytes);
        Log.e("testZip", s1);
    }

}
