package com.zl.freight.ui.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.zlibrary.base.BaseDialog;
import com.zl.zlibrary.view.WheelView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by zhanglei on 2017/12/9.
 * 选择装车时间的Dialog
 */

public class ChooseTimeDialog extends BaseDialog {

    private List<String> hArray = Arrays.asList("全天", "凌晨(0:00-6:00)", "上午(6:00-12:00)", "下午(12:00-18:00)", "晚上(18:00-24:00)");
    private ArrayList<String> hList = new ArrayList<>();
    private WheelView wvDay, wvHour;
    private View ivClear;
    private TextView dialogTitle;
    private View tvOk;

    public ChooseTimeDialog(Activity mActivity) {
        super(mActivity);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        wvDay.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (text.equals("随时装货")) {
                    ArrayList<String> a = new ArrayList<>();
                    a.add("全天");
                    wvHour.refreshData(a);
                } else if (text.contains("今天")) {
                    wvHour.refreshData(getHourData());
                } else {
                    wvHour.refreshData(hList);
                }

            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                if (onChooseTimeListener != null) {
                    String text1 = wvDay.getSelectedText();
                    String text2 = wvHour.getSelectedText();
                    onChooseTimeListener.onChooseTime(text1, text2);
                }
            }
        });
    }

    private void initData() {
        for (String s : hArray) {
            hList.add(s);
        }
        wvDay.setData(getDayData());
        wvHour.setData(hList);
    }

    private void initView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.choose_time_dialog_layout, null);
        View title = view.findViewById(R.id.ct_title);
        ivClear = title.findViewById(R.id.iv_clear);
        dialogTitle = title.findViewById(R.id.tv_dialog_title);
        tvOk = view.findViewById(R.id.tv_ok);
        wvDay = view.findViewById(R.id.wv_day);
        wvHour = view.findViewById(R.id.wv_hour);
        initPopupWindow(view);
        dialogTitle.setText(R.string.remark);
    }

    private ArrayList<String> getDayData() {
        ArrayList<String> list = new ArrayList<>();
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日");
        list.add("随时装货");
        list.add(df.format(d) + "(今天)");
        for (int i = 0; i < 6; i++) {
            if (i == 0) {
                list.add(df.format(new Date(d.getTime() + 1 * 24 * 60 * 60 * 1000)) + "(明天)");
            } else {
                list.add(df.format(new Date(d.getTime() + (i + 1) * 24 * 60 * 60 * 1000)));
            }
        }
        return list;
    }

    private ArrayList<String> getHourData() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(hArray);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");

        try {
            Long date = df.parse(df.format(new Date())).getTime();
            Long date0 = df.parse("00:00").getTime();
            Long date6 = df.parse("06:00").getTime();
            Long date12 = df.parse("12:00").getTime();
            Long date18 = df.parse("18:00").getTime();
            Long date24 = df.parse("24:00").getTime();
            if (date > date0 && date < date6) {

            } else if (date >= date6 && date < date12) {
                list.remove(1);
            } else if (date >= date12 && date < date18) {
                list.remove(1);
                list.remove(1);
            } else if (date >= date18 && date < date24) {
                list.remove(1);
                list.remove(1);
                list.remove(1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    private OnChooseTimeListener onChooseTimeListener;

    public void setOnChooseTimeListener(OnChooseTimeListener onChooseTimeListener) {
        this.onChooseTimeListener = onChooseTimeListener;
    }

    public interface OnChooseTimeListener {
        void onChooseTime(String date, String time);
    }

}
