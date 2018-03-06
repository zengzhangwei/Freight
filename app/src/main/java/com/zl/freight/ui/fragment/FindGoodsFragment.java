package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseWindow;
import com.zl.freight.mode.AddressListBean;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.GoodsListBean;
import com.zl.freight.mode.KeyValueBean;
import com.zl.freight.mode.TopNewsBean;
import com.zl.freight.ui.activity.GoodsDetailActivity;
import com.zl.freight.ui.activity.WebActivity;
import com.zl.freight.ui.dialog.CarLengthDialog;
import com.zl.freight.ui.window.AddressDialog;
import com.zl.freight.ui.window.ChooseAddressWindow;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.LocationUtils;
import com.zl.freight.utils.LoginUtils;
import com.zl.freight.utils.OnDismissListener;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.adapter.RecyclerAdapter;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author zhanglei
 * @date 17/12/7
 * 找货界面
 */
public class FindGoodsFragment extends BaseFragment {


    @BindView(R.id.find_goods_rlv)
    RecyclerView findGoodsRlv;
    Unbinder unbinder;
    @BindView(R.id.find_goods_trl)
    TwinklingRefreshLayout findGoodsTrl;
    @BindView(R.id.tv_top_news)
    TextView tvTopNews;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.linear_news)
    AutoLinearLayout linearNews;
    @BindView(R.id.tv_car_type)
    TextView tvCarType;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.iv_vv)
    ImageView ivVv;
    @BindView(R.id.tv_end)
    TextView tvEnd;

    private RecyclerAdapter<GoodsListBean> mAdapter;
    private List<GoodsListBean> mList = new ArrayList<>();
    private LocationUtils locationUtils;
    private BDLocation bdLocation;
    private int page = 1;
    private BaseUserEntity userData;
    private String carLong;
    private String carType;
    private TopNewsBean carInformationEntity;
    private CarLengthDialog carLengthDialog;
    private ChooseAddressWindow addressWindow;
    private TextView mTextView;
    private String Lineto, Linefrom = "";
    private boolean isSatrt;
    private AddressDialog addressDialog;

    public FindGoodsFragment() {
        // Required empty public constructor
    }

    public static FindGoodsFragment newInstance() {

        Bundle args = new Bundle();

        FindGoodsFragment fragment = new FindGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_goods, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getDataList(true);
        }
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                intent.putExtra("data", mList.get(position));
                LoginUtils.jumpToActivity(mActivity, intent);
            }
        });
        findGoodsTrl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                getDataList(true);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                getDataList(false);
            }
        });
        locationUtils.setOnLocationListener(new LocationUtils.OnLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                SpUtils.setLocation(mActivity, location.getLatitude(), location.getLongitude());
                bdLocation = location;
                getDataList(true);
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {
                Log.e("error", s);
            }
        });
        carLengthDialog.setOnGetCarLengthDataListener(new CarLengthDialog.OnGetCarLengthDataListener() {
            @Override
            public void carLengthData(KeyValueBean length, KeyValueBean type, KeyValueBean goodsType) {
                tvCarType.setText(length.getCodeName() + "  " + type.getCodeName());
                updateDataList(length.getId(), type.getId());
            }
        });
        carLengthDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                falseCheck();
            }
        });
        addressDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                falseCheck();
            }
        });
        addressDialog.setOnReturnAddressListener(new AddressDialog.OnReturnAddressListener() {
            @Override
            public void onAddress(String data) {
                if (mTextView != null) {
                    mTextView.setText(data);
                }
                if (isSatrt) {
                    Linefrom = data;
                } else {
                    Lineto = data;
                }
                getDataList(true);
            }
        });

    }

    private void initData() {
        getNews();
    }

    /**
     * 获取管理员发布的广告
     */
    private void getNews() {
        SoapUtils.Post(mActivity, API.GetAdminInfo, null, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONArray array = new JSONArray(data);
                    carInformationEntity = GsonUtils.fromJson(array.optString(0), TopNewsBean.class);
                    tvTopNews.setText(carInformationEntity.getInfoContent() + "                                   ");
                    tvTopNews.setSelected(true);
                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * 获取列表数据
     */
    private void getDataList(final boolean b) {
        if (bdLocation == null) return;
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        Map<String, String> params = new HashMap<>();

        if (b) {
            page = 1;
        } else {
            page++;
        }
        params.put("UserRole", userData.getUserRole());
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        params.put("IsLine", "1");
        params.put("LineId", "");
        params.put("Linefrom", Linefrom);
        params.put("Lineto", Lineto);
        params.put("CarX", bdLocation.getLatitude() + "");
        params.put("CarY", bdLocation.getLongitude() + "");
        params.put("PageIndex", page + "");
        params.put("PageSize", "10");

        SoapUtils.Post(mActivity, API.GetNearBySend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                isFnish(b);
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                isFnish(b);
                try {
                    if (b) {
                        mList.clear();
                    }
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

    private void isFnish(boolean b) {
        if (findGoodsTrl == null) {
            return;
        }
        if (b) {
            findGoodsTrl.finishRefreshing();
        } else {
            findGoodsTrl.finishLoadmore();
        }
    }

    /**
     * 更新界面数据
     *
     * @param carLong
     * @param carType
     */
    public void updateDataList(String carLong, String carType) {
        this.carLong = carLong;
        this.carType = carType;
        getDataList(true);
    }

    private void initView() {
        mAdapter = new RecyclerAdapter<GoodsListBean>(mActivity, mList, R.layout.goods_item) {
            @Override
            protected void convert(ViewHolder holder, GoodsListBean s, int position) {
                handleData(holder, s, position);
            }
        };
        findGoodsRlv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        findGoodsRlv.setAdapter(mAdapter);
        findGoodsTrl.setHeaderView(new ProgressLayout(mActivity));
        locationUtils = new LocationUtils(mActivity);
        locationUtils.startLocation();
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        carLong = userData.getCarLong();
        carType = userData.getCarType();

        carLengthDialog = new CarLengthDialog(mActivity);
        addressWindow = new ChooseAddressWindow(mActivity);
        addressDialog = new AddressDialog(mActivity, 1);
    }

    /**
     * 处理列表数据
     *
     * @param holder
     */
    private void handleData(ViewHolder holder, final GoodsListBean s, int position) {
        CircleImageView imageView = holder.getView(R.id.iv_user_icon);
        ImageLoader.loadUserIcon(mActivity, s.getUserIcon(), imageView);
        holder.getView(R.id.iv_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemUtils.call(mActivity, s.getUserName());
            }
        });
        //出发地
        holder.setText(R.id.tv_origin, s.getStartPlace());
        //目的地
        holder.setText(R.id.tv_end_point, s.getEndPlace());
        //发布人
        holder.setText(R.id.tv_user_name, s.getRealName());
        //货物发布时间
        holder.setText(R.id.tv_goods_issue_time, s.getCreateAt());
        //货物描述
//        holder.setText(R.id.tv_car_data, getResources().getString(R.string.data));
        String data = s.getCodeName1() + "米  " + s.getCodeName() + "/" + s.getGoodsWeight() + s.getWeightUnit() + " " + s.getCodeName5() + "\n装车时间"
                + s.getGoDate() + s.getGoTime() + "  " + s.getCodeName3();
        holder.setText(R.id.tv_car_data, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_top_news, R.id.iv_close, R.id.tv_car_type, R.id.tv_start, R.id.tv_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //点击广告
            case R.id.tv_top_news:
                if (carInformationEntity != null) {
                    Intent intent = new Intent(mActivity, WebActivity.class);
                    intent.putExtra("title", carInformationEntity.getInfoTitle());
                    intent.putExtra("url", carInformationEntity.getInfoLink());
                    startActivity(intent);
                }
                break;
            //点击关闭广告
            case R.id.iv_close:
                linearNews.setVisibility(View.GONE);
                break;
            //选择车长和车型
            case R.id.tv_car_type:
                carLengthDialog.show(view);
                setCheck(tvCarType);
                break;
            //起点
            case R.id.tv_start:
                addressDialog.show(view);
                isSatrt = true;
                setCheck(tvStart);
                break;
            //终点
            case R.id.tv_end:
                isSatrt = false;
                addressDialog.show(view);
                setCheck(tvEnd);
                break;
        }
    }

    private void setCheck(TextView textView) {
        falseCheck();
        mTextView = textView;
        textView.setSelected(true);
    }

    /**
     * 取消选中
     */
    private void falseCheck() {
        tvCarType.setSelected(false);
        tvStart.setSelected(false);
        tvEnd.setSelected(false);
    }
}
