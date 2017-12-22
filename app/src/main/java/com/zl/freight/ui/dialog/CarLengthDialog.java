package com.zl.freight.ui.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.mode.KeyValueBean;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.WindowUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017\12\8 0008.
 */

public class CarLengthDialog {

    private Activity mActivity;
    private PopupWindow popupWindow;
    private UniversalAdapter<KeyValueBean> lAdapter;
    private UniversalAdapter<KeyValueBean> tAdapter;
    private UniversalAdapter<KeyValueBean> gAdapter;
    //车长数据
    private List<KeyValueBean> lList = new ArrayList<>();
    //车型数据
    private List<KeyValueBean> tList = new ArrayList<>();
    //货物类型数据
    private List<KeyValueBean> gList = new ArrayList<>();
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
        initData();
        initListener();
    }

    private void initData() {
        getLData();
        getTData();
        getGData();
    }

    /**
     * 获取货物类型数据
     */
    private void getGData() {
        Map<String, String> parmas = new HashMap<>();
        parmas.put("CodeName", "货物类型");
        SoapUtils.Post(mActivity, API.BaseDict, parmas, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", error);
            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        gList.add(GsonUtils.fromJson(array.optString(i), KeyValueBean.class));
                    }
                    gAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
                Log.e("data", data);
            }
        });
    }

    /**
     * 获取车型数据
     */
    private void getTData() {
        Map<String, String> parmas = new HashMap<>();
        parmas.put("CodeName", "车型");
        SoapUtils.Post(mActivity, API.BaseDict, parmas, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", error);
            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        tList.add(GsonUtils.fromJson(array.optString(i), KeyValueBean.class));
                    }
                    tAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
                Log.e("data", data);
            }
        });
    }

    /**
     * 获取车长数据
     */
    private void getLData() {
        Map<String, String> parmas = new HashMap<>();
        parmas.put("CodeName", "车长");
        SoapUtils.Post(mActivity, API.BaseDict, parmas, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", error);
            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        lList.add(GsonUtils.fromJson(array.optString(i), KeyValueBean.class));
                    }
                    lAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
                Log.e("data", data);
            }
        });
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
        lAdapter = new UniversalAdapter<KeyValueBean>(mActivity, lList, R.layout.type_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, KeyValueBean s) {
                setText(holder, position, lPosition, s.getCodeName());
            }
        };
        tAdapter = new UniversalAdapter<KeyValueBean>(mActivity, tList, R.layout.type_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, KeyValueBean s) {
                setText(holder, position, tPosition, s.getCodeName());
            }
        };
        gAdapter = new UniversalAdapter<KeyValueBean>(mActivity, gList, R.layout.type_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, KeyValueBean s) {
                setText(holder, position, gPosition, s.getCodeName());
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
