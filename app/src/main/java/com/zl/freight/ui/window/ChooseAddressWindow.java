package com.zl.freight.ui.window;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zl.freight.R;
import com.zl.freight.base.BaseWindow;
import com.zl.freight.mode.AddressListBean;
import com.zl.freight.utils.API;
import com.zl.freight.utils.OnDismissListener;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.view.WheelView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhanglei on 2017/8/8.
 * 地址选择器（三级联动）
 */

public class ChooseAddressWindow extends BaseWindow {
    private final View view;
    private final PopupWindow popupWindow;
    private TextView text_ok;
    private TextView text_title;
    private WheelView wheelview, xiangWv, cunWv;

    private ArrayList<String> mList = new ArrayList<>();

    //    private ArrayList<String> xianList = new ArrayList<>();
//    private ArrayList<String> xiangList = new ArrayList<>();
//    private ArrayList<String> cunList = new ArrayList<>();
    private List<String> xianList = new ArrayList<>();
    private List<String> xiangList = new ArrayList<>();
    private List<String> cunList = new ArrayList<>();

    private List<AddressListBean> shenList = new ArrayList<>();
    private List<AddressListBean> shiList = new ArrayList<>();
    private List<AddressListBean> xList = new ArrayList<>();

    private int xianIndex = 0;
    private int xiangIndex = 0;
    private int cunIndex = 0;

    public ChooseAddressWindow(Activity mActivity) {
        super(mActivity);
        view = LayoutInflater.from(mActivity).inflate(R.layout.choose_address_layout, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.WindowAnim);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        wheelview.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                xianIndex = id;
                getCityList(shenList.get(id).getId());
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        xiangWv.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                xiangIndex = id;
                getXianList(shiList.get(id));
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        cunWv.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                cunIndex = id;
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        text_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

                if (onOkClickListener != null) {
                    onOkClickListener.onClickOk(shenList.get(xianIndex), shiList.get(xiangIndex), xList.get(cunIndex));
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

    public void showWindow(View view) {
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        setAlpha(0.6f);
    }

    /**
     * 初始化数据
     */
    public void initData() {

        SoapUtils.Post(mActivity, API.GetSheng, null, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", "");
            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        shenList.add(GsonUtils.fromJson(array.optString(i), AddressListBean.class));
                    }

                    for (AddressListBean addressListBean : shenList) {
                        xianList.add(addressListBean.getCodeName());
                    }
                    wheelview.refreshData(xianList);

                    getCityList(shenList.get(0).getId());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("error", "");
            }
        });
    }

    /**
     * 获取市数据
     *
     * @param id
     */
    private void getCityList(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("ParentId", id);
        SoapUtils.Post(mActivity, API.GetCity, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", "");
            }

            @Override
            public void onSuccess(String data) {
                try {
                    shiList.clear();
                    xiangList.clear();
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        shiList.add(GsonUtils.fromJson(array.optString(i), AddressListBean.class));
                    }

                    for (AddressListBean addressListBean : shiList) {
                        xiangList.add(addressListBean.getCodeName().trim());
                    }
                    xiangWv.refreshData(xiangList);

                    getXianList(shiList.get(0));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("error", "");
            }
        });
    }


    /**
     * 获取县数据
     */
    private void getXianList(final AddressListBean bean) {
        final AddressListBean listBean = new AddressListBean();
        listBean.setCodeName("全" + bean.getCodeName().trim());
        listBean.setId(bean.getId());
        listBean.setParentId(bean.getParentId());
        Map<String, String> params = new HashMap<>();
        params.put("ParentId", bean.getId());
        SoapUtils.Post(mActivity, API.GetCity, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", "");
            }

            @Override
            public void onSuccess(String data) {
                try {
                    xList.clear();
                    cunList.clear();
                    JSONArray array = new JSONArray(data);
                    xList.add(listBean);
                    for (int i = 0; i < array.length(); i++) {
                        xList.add(GsonUtils.fromJson(array.optString(i), AddressListBean.class));
                    }

                    for (AddressListBean addressListBean : xList) {
                        cunList.add(addressListBean.getCodeName().trim());
                    }
                    cunWv.refreshData(cunList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    public void setTitle(String title) {
        text_title.setText(title);
    }

    private void initView() {
        text_ok = (TextView) view.findViewById(R.id.choose_list_ok);
        text_title = (TextView) view.findViewById(R.id.choose_list_title);
        wheelview = (WheelView) view.findViewById(R.id.address_xian_wv);
        xiangWv = (WheelView) view.findViewById(R.id.address_xiang_wv);
        cunWv = (WheelView) view.findViewById(R.id.address_cun_wv);

//        wheelview.setOffset(2);
//        xiangWv.setOffset(2);
//        cunWv.setOffset(2);
    }

    private OnOkClickListener onOkClickListener;

    public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    public interface OnOkClickListener {
        void onClickOk(AddressListBean sheng, AddressListBean shi, AddressListBean xian);
    }

    protected OnDismissListener onDismissListener;

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

}
