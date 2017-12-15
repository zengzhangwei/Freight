package com.zl.freight.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.zhy.autolayout.AutoLinearLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.ui.dialog.AddressSearchDialog;
import com.zl.freight.utils.LocationUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 *         地址选择器（从地图上选择）
 */
public class AddressChooseActivity extends BaseActivity implements BaiduMap.OnMapStatusChangeListener, OnGetGeoCoderResultListener, OnGetSuggestionResultListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ac_map)
    MapView mMapView;
    @BindView(R.id.iv_location_logo)
    ImageView ivLocationLogo;
    @BindView(R.id.tv_location_data)
    TextView tvLocationData;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search_data)
    EditText etSearchData;
    @BindView(R.id.iv_list)
    ImageView ivList;
    @BindView(R.id.search_linear)
    AutoLinearLayout searchLinear;
    private BaiduMap mBaiduMap;
    private LocationUtils locationUtils;
    private GeoCoder mSearch;
    private SuggestionSearch mSuggestionSearch;
    private AddressSearchDialog searchDialog;
    private ProgressDialog progressDialog;
    private UiSettings mUiSettings;
    private double latitude = 0;
    private double longitude = 0;
    private String address;
    private AlertDialog locationErrorDialog;
    private AlertDialog helperDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_choose);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        locationUtils.setOnLocationListener(new LocationUtils.OnLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                //设定中心点坐标
                setLoactionCenter(location.getLatitude(), location.getLongitude());
                tvLocationData.setText(location.getAddrStr());
                //赋值数据
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                address = location.getAddrStr();
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });

        searchDialog.setOnItemClickListener(new AddressSearchDialog.OnAddressItemClickListener() {
            @Override
            public void onItemClick(int position, SuggestionResult.SuggestionInfo info) {
                setLoactionCenter(info.pt.latitude, info.pt.longitude);
                tvLocationData.setText(info.city + "" + info.district + "" + info.key);
                //赋值数据
                latitude = info.pt.latitude;
                longitude = info.pt.longitude;
                address = info.city + "" + info.district + "" + info.key;
            }
        });
        mBaiduMap.setOnMapStatusChangeListener(this);
        mSearch.setOnGetGeoCodeResultListener(this);
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
    }

    private void initData() {

    }

    private void initView() {
        tvTitle.setText("选择地址");
        tvTitleRight.setText("使用帮助");
        mBaiduMap = mMapView.getMap();
        locationUtils = new LocationUtils(mActivity);
        locationUtils.startLocation();
        tvLocationData.setText("定位中...");
        //获取搜索模块
        mSearch = GeoCoder.newInstance();
        //热词搜索
        mSuggestionSearch = SuggestionSearch.newInstance();
        //搜索结果显示窗口
        searchDialog = new AddressSearchDialog(mActivity);
        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setZoomGesturesEnabled(false);
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("搜索中...");
        locationErrorDialog = new AlertDialog.Builder(mActivity).setMessage("位置未获取成功，是否退出")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        helperDialog = new AlertDialog.Builder(mActivity)
                .setTitle("功能使用帮助")
                .setMessage("在使用地图中的搜索功能时，搜索的格式一定要满足 XX市XXXX的格式，如若不按照格式书写将无法完成搜索。")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mSearch.destroy();
        mSuggestionSearch.destroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * 以下四个方法是地图状态变化的方法
     *
     * @param mapStatus
     */
    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        LatLngBounds bound = mapStatus.bound;
        searchLocation(bound.getCenter());
    }

    /**
     * 根据经纬度进行搜索
     */
    private void searchLocation(LatLng latLng) {
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng));
        tvLocationData.setText("定位中...");
    }

    /**
     * 以下的两个是搜索的回调方法
     *
     * @param result
     */
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        setAddressData(result);
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        setAddressData(result);
    }

    /**
     * 热词搜索的回调方法
     *
     * @param suggestionResult
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        progressDialog.dismiss();
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            //未找到相关结果
            showToast("未找到相关结果");
            return;
        }
        List<SuggestionResult.SuggestionInfo> allSuggestions = suggestionResult.getAllSuggestions();
        searchDialog.updateData(allSuggestions);
        searchDialog.show(searchLinear);
    }

    @OnClick({R.id.iv_search, R.id.iv_back, R.id.iv_list, R.id.tv_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //退出
            case R.id.iv_back:
                back();
                break;
            //地图搜索
            case R.id.iv_search:
                search();
                break;
            //显示搜索结果窗口
            case R.id.iv_list:
                searchDialog.show(searchLinear);
                break;
            //进入使用帮助
            case R.id.tv_title_right:
                helperDialog.show();
                break;
        }
    }

    /**
     * 开始搜索
     */
    private void search() {
        String data = etSearchData.getText().toString().trim();
        if (TextUtils.isEmpty(data)) {
            showToast("搜索内容不能为空");
            return;
        }
        if (!data.contains("市")) {
            showToast("搜索格式不对，请查看使用帮助");
            return;
        }
        String city = data.substring(0, (data.indexOf("市") + 1));
        String search = data.substring((data.indexOf("市") + 1), data.length());
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(search)
                .city(city));
        progressDialog.show();
    }

    /**
     * 设定地图中心位置
     */
    private void setLoactionCenter(double latitude, double longitude) {
        //设定中心点坐标
        LatLng cenpt = new LatLng(latitude, longitude);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    /**
     * 搜索完成设置地图显示
     *
     * @param result
     */
    private void setAddressData(GeoCodeResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有检索到结果
            showToast("对不起，没有找到搜索的位置");
            return;
        }
        setLoactionCenter(result.getLocation().latitude, result.getLocation().longitude);
        //获取地理编码结果
        tvLocationData.setText(result.getAddress());
        //赋值数据
        latitude = result.getLocation().latitude;
        longitude = result.getLocation().longitude;
        address = result.getAddress();
    }

    /**
     * 搜索完成设置地图显示
     *
     * @param result
     */
    private void setAddressData(ReverseGeoCodeResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有检索到结果
            showToast("对不起，没有找到搜索的位置");
            return;
        }
        setLoactionCenter(result.getLocation().latitude, result.getLocation().longitude);
        //获取地理编码结果
        tvLocationData.setText(result.getAddress());
        //赋值数据
        latitude = result.getLocation().latitude;
        longitude = result.getLocation().longitude;
        address = result.getAddress();
    }

    /**
     * 返回的方法
     */
    private void back() {
        if (latitude != 0 && longitude != 0 && !TextUtils.isEmpty(address)) {
            Intent intent = new Intent();
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.putExtra("address", address);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            locationErrorDialog.show();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return true;
    }
}
