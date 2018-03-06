package com.zl.zlibrary.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zl.zlibrary.R;
import com.zl.zlibrary.adapter.RecyclerAdapter;
import com.zl.zlibrary.adapter.ViewHolder;
import com.zl.zlibrary.mode.AddressBean;
import com.zl.zlibrary.utils.DividerGridItemDecoration;
import com.zl.zlibrary.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/12/3.
 */

public class AddressDialog implements View.OnClickListener {

    private Activity context;
    private TextView tvProvince;
    private TextView tvCity;
    private TextView tvDistrict;
    private RecyclerView rlv, rlvCity, rlvCounty;
    private int tag = 1;
    private RecyclerAdapter<AddressBean> mAdapter, countyAdapter, cityAdapter;
    private List<AddressBean> mList = new ArrayList<>();
    private List<AddressBean> cityList = new ArrayList<>();
    private List<AddressBean> countyList = new ArrayList<>();
    //省数据
    private List<String> pList = Arrays.asList("北京", "天津", "上海", "河北", "河南", "湖南", "湖北", "山西", "山东", "陕西", "辽宁", "吉林", "黑龙江", "甘肃", "内蒙古", "青海", "广州", "海南", "福建", "四川");
    //市数据
    private List<String> cList = Arrays.asList("邢台", "邯郸", "石家庄", "衡水", "沧州", "张家口", "承德", "廊坊", "保定", "唐山", "临沂", "潍坊", "青岛", "葫芦岛", "庆阳");
    //区数据
    private List<String> dList = Arrays.asList("红桥区", "南开区", "塘沽区", "武清区", "河东区", "河西区", "海淀区", "朝阳区", "东城区", "西城区", "高碑店", "大兴区", "河北大区", "河南大区", "东北一区", "湖南一区");
    private PopupWindow popupWindow;

    public AddressDialog(Activity context) {
        this.context = context;
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1.0f);
            }
        });
        tvProvince.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        tvDistrict.setOnClickListener(this);
    }

    private void initData() {

    }

    private void getProvinceData(){

    }

    private void updateData(int tag) {
        this.tag = tag;
        mList.clear();
        switch (this.tag) {
            case 1:
                tvProvince.setSelected(true);
                tvCity.setSelected(false);
                tvDistrict.setSelected(false);
                break;
            case 2:
                tvProvince.setSelected(false);
                tvCity.setSelected(true);
                tvDistrict.setSelected(false);
                break;
            case 3:
                tvProvince.setSelected(false);
                tvCity.setSelected(false);
                tvDistrict.setSelected(true);
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (tag) {
//                    case 1:
//                        tvProvince.setText(s);
//                        break;
//                    case 2:
//                        tvCity.setText(s);
//                        break;
//                    case 3:
//                        tvDistrict.setText(s);
//                        break;
//                }
//            }
//        });
        mAdapter = new RecyclerAdapter<AddressBean>(context, mList, R.layout.address_item_layout) {
            @Override
            protected void convert(ViewHolder holder, final AddressBean s, int position) {
                TextView view = holder.getView(R.id.tv_address_item);
                view.setText(s.getCodeName());
            }
        };

        cityAdapter = new RecyclerAdapter<AddressBean>(context, cityList, R.layout.address_item_layout) {
            @Override
            protected void convert(ViewHolder holder, final AddressBean s, int position) {
                TextView view = holder.getView(R.id.tv_address_item);
                view.setText(s.getCodeName());
            }
        };

        countyAdapter = new RecyclerAdapter<AddressBean>(context, countyList, R.layout.address_item_layout) {
            @Override
            protected void convert(ViewHolder holder, final AddressBean s, int position) {
                TextView view = holder.getView(R.id.tv_address_item);
                view.setText(s.getCodeName());
            }
        };

        View view = LayoutInflater.from(context).inflate(R.layout.address_choose_layout, null);
        tvProvince = view.findViewById(R.id.tv_province);
        tvCity = view.findViewById(R.id.tv_city);
        tvDistrict = view.findViewById(R.id.tv_district);
        tvProvince.setSelected(true);
        rlv = view.findViewById(R.id.address_rlv);
        rlv.setLayoutManager(new GridLayoutManager(context, 4));
        rlv.addItemDecoration(new DividerGridItemDecoration(context));
        rlv.setAdapter(mAdapter);
        rlvCity = view.findViewById(R.id.address_rlv_city);
        rlvCity.setLayoutManager(new GridLayoutManager(context, 4));
        rlvCity.addItemDecoration(new DividerGridItemDecoration(context));
        rlvCity.setAdapter(cityAdapter);
        rlvCounty = view.findViewById(R.id.address_rlv_county);
        rlvCounty.setLayoutManager(new GridLayoutManager(context, 4));
        rlvCounty.addItemDecoration(new DividerGridItemDecoration(context));
        rlvCounty.setAdapter(countyAdapter);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.WindowAnim);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_province) {
            updateData(1);
        } else if (i == R.id.tv_city) {
            updateData(2);
        } else if (i == R.id.tv_district) {
            updateData(3);
        }
    }

    /**
     * 设置界面的透明度
     */
    private void setAlpha(float f) {
        Window window = context.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = f;
        window.setAttributes(attributes);
    }

    public void show(View view) {
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        setAlpha(0.6f);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }
}
