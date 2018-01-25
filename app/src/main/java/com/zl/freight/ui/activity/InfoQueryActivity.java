package com.zl.freight.ui.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/11
 * 统计查询界面
 */
public class InfoQueryActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_top_up_money)
    TextView tvTopUpMoney;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.linear_start_time)
    AutoLinearLayout linearStartTime;
    @BindView(R.id.iv_item_icon)
    ImageView ivItemIcon;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.linear_end_time)
    AutoLinearLayout linearEndTime;
    @BindView(R.id.auto_ll)
    AutoLinearLayout autoLl;
    @BindView(R.id.tv_ti_xian_money)
    TextView tvTiXianMoney;
    private String startTime = "2018-01-15";
    private String endTime = StringUtils.dateYYYY_MM_DD(System.currentTimeMillis());
    private TimePickerView pvTime;
    private boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_query);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        tvStartTime.setText(startTime);
        tvEndTime.setText(endTime);
        tvTitle.setText(R.string.info_query);
        //时间选择器
        //选中事件回调
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String time = new SimpleDateFormat("yyyy-MM-dd").format(date);
                if (isStart) {
                    startTime = time;
                    tvStartTime.setText(time);
                } else {
                    endTime = time;
                    tvEndTime.setText(time);
                }
                query();
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build();
        pvTime.setDate(Calendar.getInstance());
    }

    private void initData() {
        query();
    }

    /**
     * 按日期查询
     */
    private void query() {
        Map<String, String> params = new HashMap<>();
        params.put("StartTime", startTime);
        params.put("EndTime", endTime);
        //提现金额
        SoapUtils.Post(mActivity, API.GetOutcome, params, new SoapCallback() {
            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String data) {

            }
        });
        //充值金额
        SoapUtils.Post(mActivity, API.GetIncome, params, new SoapCallback() {
            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String data) {

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.linear_start_time, R.id.linear_end_time, R.id.tv_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.linear_start_time:
                isStart = true;
                pvTime.show();
                break;
            case R.id.linear_end_time:
                isStart = false;
                pvTime.show();
                break;
        }
    }
}
