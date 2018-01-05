package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zl.freight.R;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.GoodsListBean;
import com.zl.freight.ui.activity.UpdateSendActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.adapter.ViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.SystemUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author zhanglei
 * @date 18/1/2
 */
public class MyGoodsListFragment extends BaseFragment {

    @BindView(R.id.my_goods_listView)
    ListView myGoodsListView;
    @BindView(R.id.my_goods_trl)
    TwinklingRefreshLayout myGoodsTrl;
    Unbinder unbinder;
    private int type;
    private List<GoodsListBean> mList = new ArrayList<>();
    private UniversalAdapter<GoodsListBean> mAdapter;
    private int position;

    public MyGoodsListFragment() {
        // Required empty public constructor
    }

    public static MyGoodsListFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        MyGoodsListFragment fragment = new MyGoodsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_goods_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initData() {
        myGoodsTrl.startRefresh();
    }

    private void initListener() {
        myGoodsTrl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                getListData(true);
            }
        });
        myGoodsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mActivity, UpdateSendActivity.class);
                intent.putExtra("listBean", mList.get(i));
                startActivity(intent);
            }
        });
        myGoodsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, R.id.menu_delete_order, Menu.NONE, "删除订单");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_order:
                deleteOrder();
                break;
        }
        return true;
    }

    /**
     * 删除订单
     */
    private void deleteOrder() {
        Map<String, String> params = new HashMap<>();
        params.put("SendId", mList.get(position).getId());
        SoapUtils.Post(mActivity, API.DeleteSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                showToast("删除成功");
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt("type", 0);
        }
        mAdapter = new UniversalAdapter<GoodsListBean>(mActivity, mList, R.layout.my_goods_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, GoodsListBean s) {
                handleData(holder, s, position);
            }
        };
        myGoodsListView.setAdapter(mAdapter);
        myGoodsTrl.setHeaderView(new ProgressLayout(mActivity));
        myGoodsTrl.setEnableLoadmore(false);
        registerForContextMenu(myGoodsListView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 处理列表数据
     *
     * @param holder
     */
    private void handleData(UniversalViewHolder holder, final GoodsListBean s, int position) {
        //出发地
        holder.setText(R.id.tv_origin, s.getStartPlace());
        //目的地
        holder.setText(R.id.tv_end_point, s.getEndPlace());
        //货物发布时间
        holder.setText(R.id.tv_goods_issue_time, s.getCreateAt());
        //货物描述
        String data = s.getCodeName1() + "米  " + s.getCodeName() + "/" + s.getGoodsWeight() + s.getWeightUnit() + " " + s.getCodeName5() + "\n装车时间"
                + s.getGoDate() + s.getGoTime() + "  " + s.getCodeName3();
        holder.setText(R.id.tv_car_data, data);
    }

    /**
     * 获取列表数据
     */
    private void getListData(final boolean b) {

        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        Map<String, String> params = new HashMap<>();
        params.put("UserRole", userData.getUserRole());
        params.put("UserId", userData.getId());
        params.put("SendState", type + "");
        SoapUtils.Post(mActivity, API.GetCarSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                if (b) {
                    myGoodsTrl.finishRefreshing();
                } else {
                    myGoodsTrl.finishLoadmore();
                }
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                if (b) {
                    mList.clear();
                    myGoodsTrl.finishRefreshing();
                } else {
                    myGoodsTrl.finishLoadmore();
                }
                try {
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        GoodsListBean sendEntity = GsonUtils.fromJson(array.optString(i), GoodsListBean.class);
                        mList.add(sendEntity);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        });
    }
}
