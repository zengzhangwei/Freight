package com.zl.freight.ui.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.base.BaseDialog;
import com.zl.zlibrary.utils.WindowUtils;
import com.zl.zlibrary.view.MyGridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhanglei on 2017/12/9.
 * 备注信息dialog
 */

public class RemarkDialog extends BaseDialog {

    private UniversalAdapter<String> lAdapter;
    private UniversalAdapter<String> tAdapter;
    private MyGridView lGrid, tGrid;
    private View ivClear, tvOk;
    private PopupWindow popupWindow;
    String[] lArray = mActivity.getResources().getStringArray(R.array.load_unload_mode_list);
    String[] tArray = mActivity.getResources().getStringArray(R.array.pay_mode_list);
    private List<String> lList = new ArrayList<>();
    private List<String> tList = new ArrayList<>();
    private int lPosition = 0;
    private int tPosition = 0;

    public RemarkDialog(Activity mActivity) {
        super(mActivity);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        for (String s : lArray) {
            lList.add(s);
        }
        for (String s : tArray) {
            tList.add(s);
        }
        lAdapter.notifyDataSetChanged();
        tAdapter.notifyDataSetChanged();

    }

    private void initListener() {
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1.0f);
            }
        });
        lGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lPosition = i;
                lAdapter.notifyDataSetChanged();
            }
        });
        tGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tPosition = i;
                tAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        lAdapter = new UniversalAdapter<String>(mActivity, lList, R.layout.type_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                setText(holder, position, lPosition, s);
            }
        };
        tAdapter = new UniversalAdapter<String>(mActivity, tList, R.layout.type_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                setText(holder, position, tPosition, s);
            }
        };
        View view = LayoutInflater.from(mActivity).inflate(R.layout.content_dialog_layout, null);
        View title = view.findViewById(R.id.cd_title);
        ivClear = title.findViewById(R.id.iv_clear);
        lGrid = view.findViewById(R.id.grid_card_length);
        tGrid = view.findViewById(R.id.grid_card_type);
        tvOk = view.findViewById(R.id.tv_ok);
        lGrid.setAdapter(lAdapter);
        tGrid.setAdapter(tAdapter);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(com.zl.zlibrary.R.style.WindowAnim);
    }

    public void show(View view) {
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        WindowUtils.setAlpha(mActivity, 0.6f);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    private void setText(UniversalViewHolder holder, int position, int p, String s) {
        TextView view = holder.getView(R.id.tv_type_item);
        view.setText(s);
        if (position == p) {
            view.setSelected(true);
        } else {
            view.setSelected(false);
        }
    }
}
