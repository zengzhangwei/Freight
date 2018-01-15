package com.zl.freight.ui.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.zl.freight.R;

/**
 * Created by Administrator on 2018/1/15.
 */

public class MessageDialog {

    private Context mContext;
    private final AlertDialog alertDialog;

    public MessageDialog(Context mContext) {
        this.mContext = mContext;
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_layout, null);
        view.findViewById(R.id.tv_message_to_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });
        view.findViewById(R.id.tv_message_to_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });
        alertDialog = new AlertDialog.Builder(mContext).setView(view).create();
    }

    public void show() {
        alertDialog.show();
    }

    public void hide() {
        alertDialog.dismiss();
    }

}
