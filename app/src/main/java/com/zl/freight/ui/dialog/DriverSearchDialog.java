package com.zl.freight.ui.dialog;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.mode.KeyValueBean;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.base.BaseDialog;
import com.zl.zlibrary.utils.GsonUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017\12\21 0021.
 */

public class DriverSearchDialog extends BaseDialog implements View.OnClickListener {

    private UniversalAdapter<KeyValueBean> lAdapter;
    private UniversalAdapter<KeyValueBean> tAdapter;
    private UniversalAdapter<KeyValueBean> sAdapter;
    //车长数据
    private List<KeyValueBean> lList = new ArrayList<>();
    //车型数据
    private List<KeyValueBean> tList = new ArrayList<>();
    private List<KeyValueBean> sList = new ArrayList<>();
    private int lPosition = 0;
    private int tPosition = 0;
    private int sPosition = 0;
    private ImageView ivClear;
    private GridView lGrid, tGrid, sGrid;
    private TextView tvOk;

    public DriverSearchDialog(Activity mActivity) {
        super(mActivity);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        getLData();
        getTData();
        getSdData();
    }

    private void getSdData() {
        KeyValueBean bean1 = new KeyValueBean();
        KeyValueBean bean2 = new KeyValueBean();
        bean1.setCodeName("全部车辆");
        bean1.setId("1");
        bean2.setCodeName("只看空车");
        bean2.setId("2");
        sList.add(bean1);
        sList.add(bean2);
        sAdapter.notifyDataSetChanged();
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
        sAdapter = new UniversalAdapter<KeyValueBean>(mActivity, sList, R.layout.type_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, KeyValueBean s) {
                setText(holder, position, sPosition, s.getCodeName());
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
                commit();
                break;
        }
    }

    /**
     * 返回数据
     */
    private void commit() {
        dismissDialog();
        try {
            KeyValueBean l = lList.get(lPosition);
            KeyValueBean t = tList.get(tPosition);
            KeyValueBean s = sList.get(sPosition);
            if (onGetDriverSearchListener != null) {
                onGetDriverSearchListener.onDriverSearch(l, t, s);
            }
        } catch (Exception e) {

        }
    }

    private OnGetDriverSearchListener onGetDriverSearchListener;

    public void setOnGetDriverSearchListener(OnGetDriverSearchListener onGetDriverSearchListener) {
        this.onGetDriverSearchListener = onGetDriverSearchListener;
    }

    public interface OnGetDriverSearchListener {
        /**
         * @param length 车长
         * @param type   车型
         * @param stype  其他条件
         */
        void onDriverSearch(KeyValueBean length, KeyValueBean type, KeyValueBean stype);
    }
}
