package com.zl.freight.ui.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.mapapi.search.sug.SuggestionResult;
import com.zl.freight.R;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanglei on 2017/12/14.
 */

public class AddressSearchDialog extends BaseDialog {

    private ListView listAddress;
    private List<SuggestionResult.SuggestionInfo> mList = new ArrayList<>();
    private UniversalAdapter<SuggestionResult.SuggestionInfo> mAdapter;
    private OnAddressItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnAddressItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AddressSearchDialog(Activity mActivity) {
        super(mActivity);
        initView();
        initListener();
    }

    private void initListener() {
        listAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(i, mList.get(i));
                }
                dismissDialog();
            }
        });
    }

    private void initView() {
        mAdapter = new UniversalAdapter<SuggestionResult.SuggestionInfo>(mActivity, mList, R.layout.location_address_item) {
            @Override
            public void convert(UniversalViewHolder holder, int position, SuggestionResult.SuggestionInfo suggestionInfo) {
                holder.setText(R.id.tv_location_name, suggestionInfo.city + "/" + suggestionInfo.district + "/" + suggestionInfo.key);
            }
        };
        View view = LayoutInflater.from(mActivity).inflate(R.layout.list_layout, null);
        listAddress = view.findViewById(R.id.list_address);
        listAddress.setAdapter(mAdapter);
        initPopupWindow(view);
    }

    /**
     * 更新数据
     *
     * @param list
     */
    public void updateData(List<SuggestionResult.SuggestionInfo> list) {
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    public void show(View view) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(view, 0, 10);
        }
    }

    public interface OnAddressItemClickListener {
        void onItemClick(int position, SuggestionResult.SuggestionInfo info);
    }

}
