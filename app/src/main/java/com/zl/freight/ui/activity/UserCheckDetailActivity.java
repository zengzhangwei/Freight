package com.zl.freight.ui.activity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.autolayout.AutoLinearLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.CarUserBean;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.view.MyGridView;
import com.zl.zlibrary.view.MyListView;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/13
 * 用户审核详情页
 */
public class UserCheckDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_check_ok)
    TextView tvCheckOk;
    @BindView(R.id.tv_check_no)
    TextView tvCheckNo;
    @BindView(R.id.user_check_detail_bottom)
    AutoLinearLayout userCheckDetailBottom;
    @BindView(R.id.user_check_detail_list)
    MyListView userCheckDetailList;
    @BindView(R.id.user_check_detail_grid)
    MyGridView userCheckDetailGrid;
    private AlertDialog alertDialog;
    private AlertDialog etDialog;
    private List<String> mList = new ArrayList<>();
    private List<String> imgList = new ArrayList<>();

    private UniversalAdapter<String> mAdapter;
    private UniversalAdapter<String> imgAdapter;
    private BaseUserEntity userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check_detail);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        userData = (BaseUserEntity) getIntent().getSerializableExtra("data");
        userData.setCarType("");
        userData.setCarLong("");
        getData();
    }

    /**
     * 获取用户信息
     */
    private void getData() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", userData.getId());
        params.put("UserRole", userData.getUserRole());
        SoapUtils.Post(mActivity, API.ShowUserInfo, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", "获取用户信息失败");
            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONArray array = new JSONArray(data);
                    CarUserBean carUserBean = GsonUtils.fromJson(array.optString(0), CarUserBean.class);
                    upDateUi(carUserBean);
                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * 更新界面
     *
     * @param carUserBean
     */
    private void upDateUi(CarUserBean carUserBean) {
        mList.add("姓名：" + userData.getRealName());
        mList.add("手机号：" + userData.getUserName());
        mList.add("身份证号：" + userData.getIdCardNumber());
        mList.add("类别：实名认证审核");
        mList.add("提交日期：" + userData.getCreateAt());
        String isCheck = userData.getIsCheck();
        if (!TextUtils.isEmpty(isCheck)) {
            if (isCheck.equals("0")) {
                mList.add("审核状态：未审核");
            } else if (isCheck.equals("1")) {
                mList.add("审核状态：已审核");
            } else if (isCheck.equals("2")) {
                mList.add("审核状态：审核未通过");
            }
        }

        imgList.add(userData.getIdCard1());
        imgList.add(userData.getIdCard2());
        if (carUserBean.getUserRole().equals(API.DRIVER + "")) {
            mList.add("车牌照：" + carUserBean.getCarNo());
            mList.add("车长车型：" + carUserBean.getCodeName1() + "米/" + carUserBean.getCodeName());
            imgList.add(carUserBean.getDrivingLlicence());
            imgList.add(carUserBean.getVehicleLicense());
            imgList.add(carUserBean.getCarPic1());
            imgList.add(carUserBean.getCarPic2());
        } else {
            if (!TextUtils.isEmpty(carUserBean.getCompanyPic())) {
                imgList.add(carUserBean.getCompanyPic());
            }
            if (!TextUtils.isEmpty(carUserBean.getStorePic())) {
                imgList.add(carUserBean.getStorePic());
            }
            if (!TextUtils.isEmpty(carUserBean.getStorePic1())) {
                imgList.add(carUserBean.getStorePic1());
            }
            if (!TextUtils.isEmpty(carUserBean.getStorePic2())) {
                imgList.add(carUserBean.getStorePic2());
            }
        }

        mAdapter.notifyDataSetChanged();
        imgAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        userCheckDetailGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mActivity, PictureActivity.class);
                intent.putExtra("url", imgList.get(i));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, view, PictureActivity.PICTURE);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    private void initView() {

        tvTitle.setText(R.string.user_check);
        alertDialog = new AlertDialog.Builder(mActivity)
                .setMessage("确定通过审核吗")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        commitOk();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();

        View view = LayoutInflater.from(mActivity).inflate(R.layout.et_layout, null);
        final EditText et = view.findViewById(R.id.et_input);
        etDialog = new AlertDialog.Builder(this).setTitle("添加问题描述")
                .setView(view)
                .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String data = et.getText().toString().trim();
                        if (TextUtils.isEmpty(data)) {
                            showToast("内容描述不能为空");
                            return;
                        } else {
                            commitNo(data);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();

        mAdapter = new UniversalAdapter<String>(mActivity, mList, R.layout.text_item) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                holder.setText(R.id.text_item, s);
            }
        };

        imgAdapter = new UniversalAdapter<String>(mActivity, imgList, R.layout.image_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                ImageView view = holder.getView(R.id.iv_item);
                ImageLoader.loadImageUrl(mActivity, s, view);
            }
        };

        userCheckDetailList.setAdapter(mAdapter);
        userCheckDetailGrid.setAdapter(imgAdapter);

    }

    /**
     * 通过审核
     */
    private void commitOk() {
        Map<String, String> params = new HashMap<>();
        userData.setIsCheck("1");
        params.put("baseUserEntityJson", GsonUtils.toJson(userData));
        params.put("IsCheck", "1");
        params.put("unCheckInfo", "");
        SoapUtils.Post(mActivity, API.CheckSave, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
                Log.e("error", "");
            }

            @Override
            public void onSuccess(String data) {
                Log.e("error", data);
                showToast("审核通过");
                finish();
            }
        });

    }

    /**
     * 驳回审核
     *
     * @param content
     */
    private void commitNo(String content) {
        Map<String, String> params = new HashMap<>();
        userData.setIsCheck("2");
        params.put("baseUserEntityJson", GsonUtils.toJson(userData));
        params.put("IsCheck", "2");
        params.put("unCheckInfo", content);
        SoapUtils.Post(mActivity, API.CheckSave, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
                Log.e("error", "");
            }

            @Override
            public void onSuccess(String data) {
                Log.e("error", data);
                showToast("审核通过");
                finish();
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_check_ok, R.id.tv_check_no})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_check_ok:
                alertDialog.show();
                break;
            case R.id.tv_check_no:
                etDialog.show();
                break;
        }
    }
}
