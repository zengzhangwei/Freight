package com.zl.freight.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Administrator on 2018\2\26 0026.
 */

public class Mp3Utils {

    public static void paly(Context context, int path) {
        MediaPlayer player = MediaPlayer.create(context, path);
        player.start();
    }


}
