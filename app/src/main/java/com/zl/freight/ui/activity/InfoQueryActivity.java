package com.zl.freight.ui.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.zlibrary.utils.HttpUtils;

import java.util.Calendar;

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
    @BindView(R.id.tv_send_goods_count)
    TextView tvSendGoodsCount;
    @BindView(R.id.tv_get_goods_count)
    TextView tvGetGoodsCount;
    @BindView(R.id.tv_top_up_money)
    TextView tvTopUpMoney;
    private DatePickerDialog pickerDialog;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_query);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        tvTitle.setText(R.string.info_query);
        tvTitleRight.setText(R.string.with_time_query);
        Calendar calendar = Calendar.getInstance();
        pickerDialog = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                year = y;
                month = (m + 1);
                day = d;
                tvTitleRight.setText(year + "年" + month + "月" + day + "日");
                query();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void initData() {
        query();
    }

    /**
     * 按日期查询
     */
    private void query() {

    }

    @OnClick({R.id.iv_back, R.id.tv_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_title_right:
                pickerDialog.show();
                break;
        }
    }
}
