package com.zl.freight.ui.activity;

import android.app.Activity;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.zl.freight.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressTestActivity extends Activity {

    @BindView(R.id.iv_pro)
    ImageView ivPro;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_test);
        ButterKnife.bind(this);
        final ClipDrawable drawable = (ClipDrawable) ivPro.getDrawable();
        new Thread() {
            @Override
            public void run() {
                try {

                    while (position < 10000) {
                        sleep(1);
                        position += 10;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawable.setLevel(position);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
