package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.blankj.utilcode.util.SpanUtils;
import com.google.gson.Gson;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.freight.R;
import com.zl.freight.mode.PathListBean;
import com.zl.freight.ui.activity.AddPathActivity;
import com.zl.freight.ui.activity.PathListActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.view.MyListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 18/2/7
 * 司机添加常跑路线
 */
public class AddPathFragment extends BaseFragment {


    @BindView(R.id.linear_add_path)
    AutoRelativeLayout linearAddPath;
    @BindView(R.id.my_path_listview)
    MyListView myPathListview;
    Unbinder unbinder;
    private List<PathListBean> mList = new ArrayList<>();
    private UniversalAdapter<PathListBean> mAdapter;

    public AddPathFragment() {
        // Required empty public constructor
    }

    public static AddPathFragment newInstance() {

        Bundle args = new Bundle();

        AddPathFragment fragment = new AddPathFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_path, container, false);
        unbinder = ButterKnife.bind(this, view);
        view.setClickable(true);
        initView();
        initListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initListener() {
        myPathListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mActivity, PathListActivity.class);
                intent.putExtra("data", mList.get(i));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        SoapUtils.Post(mActivity, API.ShowLine, params, new SoapCallback() {
            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONArray array = new JSONArray(data);
                    mList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        mList.add(GsonUtils.fromJson(array.optString(i), PathListBean.class));
                    }
                    for (int i = 0; i < mList.size(); i++) {
                        Map<String, String> map = new HashMap<>();
                        Map<String, String> location = SpUtils.getLocation(mActivity);
                        map.put("UserRole", "1");
                        map.put("UserId", SpUtils.getUserData(mActivity).getId());
                        map.put("CarX", location.get("x"));
                        map.put("CarY", location.get("y"));
                        map.put("LineId", mList.get(i).getId());
                        final int finalI = i;
                        SoapUtils.Post(mActivity, API.GetLineCount, map, new SoapCallback() {
                            @Override
                            public void onError(String error) {
                                Log.e("error", "");
                            }

                            @Override
                            public void onSuccess(String data) {
                                mList.get(finalI).setCount(Integer.parseInt(data));
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        mAdapter = new UniversalAdapter<PathListBean>(mActivity, mList, R.layout.path_item_layout) {
            @Override
            public void convert(final UniversalViewHolder holder, int position, final PathListBean s) {
                holder.setText(R.id.tv_origin, s.getLineFrom());
                holder.setText(R.id.tv_end_point, s.getLineTo());
                holder.setText(R.id.tv_car_data, "货源数  " + s.getCount());
                Log.e("LineId", s.getId());
            }
        };
        myPathListview.setAdapter(mAdapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.linear_add_path)
    public void onViewClicked() {
        startActivity(new Intent(mActivity, AddPathActivity.class));
    }
}
