package com.zl.freight.ui.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.base.BaseDialog;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017\12\21 0021.
 */

public class DriverSearchDialog extends BaseDialog implements View.OnClickListener {

    private UniversalAdapter<String> lAdapter;
    private UniversalAdapter<String> tAdapter;
    private UniversalAdapter<String> sAdapter;
    private List<String> lList = Arrays.asList("不限", "1.8", "2.5", "3", "3.3", "3.6", "4.2", "4.5", "5", "5.2", "6.2",
            "6.8", "7.2", "7.6", "8.2", "8.6", "9.6", "11.7", "12.5", "13", "13.5", "14", "15", "16", "17", "17.5", "18");
    private List<String> tList = Arrays.asList("不限", "平板", "高栏", "厢式", "高低板", "保温",
            "冷藏", "威胁品", "自卸", "中卡", "面包", "棉被车");
    private List<String> sList = Arrays.asList("全部车辆", "只看空车");
    private int lPosition = 0;
    private int tPosition = 0;
    private int sPosition = 0;
    private ImageView ivClear;
    private GridView lGrid, tGrid, sGrid;
    private TextView tvOk;

    public DriverSearchDialog(Activity mActivity) {
        super(mActivity);
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
        sGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sPosition = i;
                sAdapter.notifyDataSetChanged();
            }
        });
        ivClear.setOnClickListener(this);
        tvOk.setOnClickListener(this);
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
        sAdapter = new UniversalAdapter<String>(mActivity, sList, R.layout.type_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                setText(holder, position, sPosition, s);
            }
        };
        View view = LayoutInflater.from(mActivity).inflate(R.layout.driver_search_layout, null);
        View title = view.findViewById(R.id.clt_title);
        ivClear = title.findViewById(R.id.iv_clear);
        lGrid = view.findViewById(R.id.grid_card_length);
        tGrid = view.findViewById(R.id.grid_card_type);
        sGrid = view.findViewById(R.id.grid_car_status);
        tvOk = view.findViewById(R.id.tv_ok);
        lGrid.setAdapter(lAdapter);
        tGrid.setAdapter(tAdapter);
        sGrid.setAdapter(sAdapter);
        initPopupWindow(view);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                dismissDialog();
                break;
            case R.id.tv_ok:
                dismissDialog();
                break;
        }
    }
}
