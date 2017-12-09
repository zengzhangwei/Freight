package com.zl.freight.mode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/8.
 */

public class a {

    public static void main(String[] args) {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日");
        System.out.println("今天的日期：" + df.format(d));
        System.out.println("两天前的日期：" + df.format(new Date(d.getTime() - 2 * 24 * 60 * 60 * 1000)));
        System.out.println("三天后的日期：" + df.format(new Date(d.getTime() + 3 * 24 * 60 * 60 * 1000)));
    }

}
