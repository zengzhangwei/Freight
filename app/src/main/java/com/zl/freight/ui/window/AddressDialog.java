package com.zl.freight.ui.window;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zl.freight.mode.AddressListBean;
import com.zl.freight.utils.API;
import com.zl.freight.utils.OnDismissListener;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private RecyclerAdapter<AddressListBean> mAdapter, countyAdapter, cityAdapter;
    private List<AddressListBean> mList = new ArrayList<>();
    private List<AddressListBean> cityList = new ArrayList<>();
    private List<AddressListBean> countyList = new ArrayList<>();
    private PopupWindow popupWindow;
    private String provinceId = "";
    private String provinceName = "";
    private String cityId = "";
    //0发货用，没有全省全市 1找货用，有全省全市
    private int type = 0;

    private OnReturnAddressListener onReturnAddressListener;
    private OnDismissListener onDismissListener;

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setOnReturnAddressListener(OnReturnAddressListener onReturnAddressListener) {
        this.onReturnAddressListener = onReturnAddressListener;
    }

    public interface OnReturnAddressListener {

        /**
         * 返回简单的地址 不包含省
         *
         * @param data
         */
        void onAddress(String data);

        /**
         * 返回详细的地址 包含省
         *
         * @param data
         */
        void onAddressDetail(String data);
    }

    public AddressDialog(Activity context) {
        this.context = context;
        initView();
        initData();
        initListener();
    }

    public AddressDialog(Activity context, int type) {
        this.context = context;
        this.type = type;
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

        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                cityId = "";
                if (mList.get(position).getCodeName().equals("全国")) {
                    if (onReturnAddressListener != null) {
                        onReturnAddressListener.onAddress("全国");
                        onReturnAddressListener.onAddressDetail("");
                    }
                    dismiss();
                    return;
                }
                tvProvince.setText(mList.get(position).getCodeName());
                provinceId = mList.get(position).getId();
                provinceName = mList.get(position).getCodeName();
                isShowRlv(rlvCity);
                isShow(tvCity);
                getCityData();
            }
        });

        cityAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                tvCity.setText(cityList.get(position).getCodeName());
                if (type == 1 && position == 0) {
                    if (onReturnAddressListener != null) {
                        String codeName = cityList.get(position).getCodeName();
                        String data = codeName.replace("全", "");
                        onReturnAddressListener.onAddress(data);
                        onReturnAddressListener.onAddressDetail(data);
                    }
                    dismiss();
                    return;
                } else if (provinceName.contains("北京市") || provinceName.contains("天津市") || provinceName.contains("上海市") || provinceName.contains("重庆市")) {
                    if (onReturnAddressListener != null) {
                        String codeName = cityList.get(position).getCodeName();
                        onReturnAddressListener.onAddress(provinceName + codeName);
                        onReturnAddressListener.onAddressDetail(provinceName + codeName);
                    }
                    dismiss();
                    return;
                }
                cityId = cityList.get(position).getId();
                isShowRlv(rlvCounty);
                isShow(tvDistrict);
                getCountyData();
            }
        });

        countyAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                tvDistrict.setText(countyList.get(position).getCodeName());
                if (type == 1 && position == 0) {
                    if (onReturnAddressListener != null) {
                        String codeName = countyList.get(position).getCodeName();
                        String data = codeName.replace("全", "");
                        onReturnAddressListener.onAddress(data);
                        onReturnAddressListener.onAddressDetail(provinceName + data);
                    }
                    dismiss();
                    return;
                }
                dismiss();
                String city = tvCity.getText().toString().trim();
                if (onReturnAddressListener != null) {
                    onReturnAddressListener.onAddress(city + countyList.get(position).getCodeName());
                    onReturnAddressListener.onAddressDetail(provinceName + city + countyList.get(position).getCodeName());
                }
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1f);
                if (onDismissListener != null) {
                    onDismissListener.onDismiss();
                }
            }
        });
    }

    /**
     * 获取市数据
     */
    private void getCityData() {
        if (TextUtils.isEmpty(provinceId)) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("ParentId", provinceId);
        SoapUtils.Post(context, API.GetCity, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", "");
            }

            @Override
            public void onSuccess(String data) {
                try {
                    cityList.clear();
                    if (type == 1) {
                        AddressListBean listBean = new AddressListBean();
                        String name = tvProvince.getText().toString().trim();
                        listBean.setCodeName("全" + name);
                        cityList.add(listBean);
                    }
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        cityList.add(GsonUtils.fromJson(array.optString(i), AddressListBean.class));
                    }
                    cityAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("error", "");
            }
        });
    }

    private void getCountyData() {
        if (TextUtils.isEmpty(cityId)) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("ParentId", cityId);
        SoapUtils.Post(context, API.GetCity, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", "");
            }

            @Override
            public void onSuccess(String data) {
                try {
                    countyList.clear();
                    if (type == 1) {
                        AddressListBean listBean = new AddressListBean();
                        String name = tvCity.getText().toString().trim();
                        listBean.setCodeName("全" + name);
                        countyList.add(listBean);
                    }
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        countyList.add(GsonUtils.fromJson(array.optString(i), AddressListBean.class));
                    }
                    countyAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("error", "");
            }
        });
    }

    private void initData() {
        getProvinceData();
    }

    private void getProvinceData() {
        SoapUtils.Post(context, API.GetSheng, null, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", "");
            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONArray array = new JSONArray(data);
                    AddressListBean addressListBean = new AddressListBean();
                    addressListBean.setCodeName("全国");
                    addressListBean.setParentId("0");
                    addressListBean.setId("0");
                    mList.add(addressListBean);
                    for (int i = 0; i < array.length(); i++) {
                        mList.add(GsonUtils.fromJson(array.optString(i), AddressListBean.class));
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("error", "");
            }
        });
    }

    private void updateData(int tag) {
        this.tag = tag;
        switch (this.tag) {
            case 1:
                isShowRlv(rlv);
                isShow(tvProvince);
                break;
            case 2:
                if (TextUtils.isEmpty(provinceId)) {
                    showToast("请先选择省");
                    return;
                }
                isShowRlv(rlvCity);
                isShow(tvCity);
                break;
            case 3:
                if (TextUtils.isEmpty(cityId)) {
                    showToast("请先选择市");
                    return;
                }
                isShowRlv(rlvCounty);
                isShow(tvDistrict);
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void isShow(View view) {
        tvProvince.setSelected(false);
        tvCity.setSelected(false);
        tvDistrict.setSelected(false);
        view.setSelected(true);
    }

    private void isShowRlv(View view) {
        rlv.setVisibility(View.GONE);
        rlvCity.setVisibility(View.GONE);
        rlvCounty.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        mAdapter = new RecyclerAdapter<AddressListBean>(context, mList, R.layout.address_item_layout) {
            @Override
            protected void convert(ViewHolder holder, final AddressListBean s, int position) {
                TextView view = holder.getView(R.id.tv_address_item);
                view.setText(s.getCodeName());
            }
        };

        cityAdapter = new RecyclerAdapter<AddressListBean>(context, cityList, R.layout.address_item_layout) {
            @Override
            protected void convert(ViewHolder holder, final AddressListBean s, int position) {
                TextView view = holder.getView(R.id.tv_address_item);
                view.setText(s.getCodeName());
            }
        };

        countyAdapter = new RecyclerAdapter<AddressListBean>(context, countyList, R.layout.address_item_layout) {
            @Override
            protected void convert(ViewHolder holder, final AddressListBean s, int position) {
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
        isShow(tvProvince);
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
