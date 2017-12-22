package com.zl.freight.ui.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.zl.zlibrary.utils.WindowUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanglei on 2017\12\8 0008.
 * 选择获取类型的dialog
 */

public class GoodsTypeDialog extends BaseDialog {

    private PopupWindow popupWindow;
    private List<KeyValueBean> mList = new ArrayList<>();
    private UniversalAdapter<KeyValueBean> mAdapter;
    private ListView listView;
    private View ivClear;
    private TextView dialogTitle;
    private TextView tvInputOk;
    private EditText etInputType;

    public GoodsTypeDialog(Activity mActivity) {
        super(mActivity);
        initView();
        initData();
        initListener();
    }

    private void initData() {
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
                        mList.add(GsonUtils.fromJson(array.optString(i), KeyValueBean.class));
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
                Log.e("data", data);
            }
        });
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dismiss();
                if (onReturnDataListener != null) {
                    onReturnDataListener.returnData(mList.get(i).getCodeName());
                }
            }
        });
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tvInputOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onReturnDataListener != null) {
                    String data = etInputType.getText().toString().trim();
                    onReturnDataListener.returnData(TextUtils.isEmpty(data) ? "" : data);
                }
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1.0f);
            }
        });
    }

    private void initView() {
        mAdapter = new UniversalAdapter<KeyValueBean>(mActivity, mList, R.layout.tv_simple_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, KeyValueBean s) {
                holder.setText(R.id.tv_simple_item, s.getCodeName());
            }
        };
        View view = LayoutInflater.from(mActivity).inflate(R.layout.goods_type_layout, null);
        View title = view.findViewById(R.id.gt_title);
        ivClear = title.findViewById(R.id.iv_clear);
        dialogTitle = title.findViewById(R.id.tv_dialog_title);
        listView = view.findViewById(R.id.gt_list);
        tvInputOk = view.findViewById(R.id.tv_input_ok);
        etInputType = view.findViewById(R.id.et_input_type);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(com.zl.zlibrary.R.style.WindowAnim);
        listView.setAdapter(mAdapter);
        dialogTitle.setText(R.string.goods_type);
    }

    public void show(View view) {
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        WindowUtils.setAlpha(mActivity, 0.6f);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    private OnReturnDataListener onReturnDataListener;

    public void setOnReturnDataListener(OnReturnDataListener onReturnDataListener) {
        this.onReturnDataListener = onReturnDataListener;
    }

    public interface OnReturnDataListener {
        void returnData(String data);
    }

}
