package com.zl.freight.ui.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.utils.WindowUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017\12\8 0008.
 */

public class CarLengthDialog {

    private Activity mActivity;
    private PopupWindow popupWindow;
    private UniversalAdapter<String> lAdapter;
    private UniversalAdapter<String> tAdapter;
    private UniversalAdapter<String> gAdapter;
    private List<String> lList = Arrays.asList("不限", "1.8", "2.5", "3", "3.3", "3.6", "4.2", "4.5", "5", "5.2", "6.2",
            "6.8", "7.2", "7.6", "8.2", "8.6", "9.6", "11.7", "12.5", "13", "13.5", "14", "15", "16", "17", "17.5", "18");
    private List<String> tList = Arrays.asList("不限", "平板", "高栏", "厢式", "高低板", "保温",
            "冷藏", "威胁品", "自卸", "中卡", "面包", "棉被车");
    private List<String> gList = Arrays.asList("普货", "重货", "泡货", "设备");
    private GridView lGrid;
    private GridView tGrid;
    private GridView gGrid;
    private View tvOk;
    private View ivClear;
    private int lPosition = 0;
    private int tPosition = 0;
    private int gPosition = 0;

    public CarLengthDialog(Activity mActivity) {
        this.mActivity = mActivity;
        initView();
        initListener();
    }

    private void initListener() {
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
        gGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gPosition = i;
                gAdapter.notifyDataSetChanged();
            }
        });
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
                WindowUtils.setAlpha(mActivity, 1.0f);
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
        gAdapter = new UniversalAdapter<String>(mActivity, gList, R.layout.type_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                setText(holder, position, gPosition, s);
            }
        };
        View view = LayoutInflater.from(mActivity).inflate(R.layout.car_length_type_dialog_layout, null);
        View title = view.findViewById(R.id.clt_title);
        ivClear = title.findViewById(R.id.iv_clear);
        lGrid = view.findViewById(R.id.grid_card_length);
        tGrid = view.findViewById(R.id.grid_card_type);
        gGrid = view.findViewById(R.id.grid_goods_type);
        tvOk = view.findViewById(R.id.tv_ok);
        view.findViewById(R.id.tv_use_car_type).setVisibility(View.GONE);
        view.findViewById(R.id.tv_breakbulk).setVisibility(View.GONE);
        view.findViewById(R.id.tv_carload).setVisibility(View.GONE);
        lGrid.setAdapter(lAdapter);
        tGrid.setAdapter(tAdapter);
        gGrid.setAdapter(gAdapter);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(com.zl.zlibrary.R.style.WindowAnim);
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

    public void show(View view) {
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        WindowUtils.setAlpha(mActivity, 0.6f);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

}
