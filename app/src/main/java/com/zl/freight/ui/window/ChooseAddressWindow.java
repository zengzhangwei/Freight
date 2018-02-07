package com.zl.freight.ui.window;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseWindow;
import com.zl.zlibrary.view.WheelView;

import java.util.ArrayList;


/**
 * Created by zhanglei on 2017/8/8.
 * 地址选择器（三级联动）
 */

public class ChooseAddressWindow extends BaseWindow {
    private final View view;
    private final PopupWindow popupWindow;
    private TextView text_ok;
    private TextView text_title;
    private WheelView wheelview, xiangWv, cunWv;

    private ArrayList<String> mList = new ArrayList<>();

    private ArrayList<String> xianList = new ArrayList<>();
    private ArrayList<String> xiangList = new ArrayList<>();
    private ArrayList<String> cunList = new ArrayList<>();

    private int xianIndex = 0;
    private int xiangIndex = 0;
    private int cunIndex = 0;

    public ChooseAddressWindow(AppCompatActivity mActivity) {
        super(mActivity);
        view = LayoutInflater.from(mActivity).inflate(R.layout.choose_address_layout, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.WindowAnim);
        initView();
        initListener();
    }

    private void initListener() {
        wheelview.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {

            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        xiangWv.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {

            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        cunWv.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                cunIndex = id;
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        text_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

                int[] indexs = new int[3];
                String address;
                if (xianIndex == 0) {
                    address = wheelview.getSelectedText();
                    indexs[0] = xianIndex;
                    indexs[1] = -1;
                    indexs[2] = -1;
                } else {
                    address = wheelview.getSelectedText() + " " + xiangWv.getSelectedText() + " " + cunWv.getSelectedText();
                    indexs[0] = xianIndex;
                    indexs[1] = xiangIndex;
                    indexs[2] = cunIndex;
                }

                if (onOkClickListener != null) {
                    onOkClickListener.onClickOk(indexs, address);
                }
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1f);
            }
        });
    }

    public void showWindow(View view) {
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        setAlpha(0.6f);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        mList.clear();
        xianList.clear();
        xiangList.clear();
        cunList.clear();

        wheelview.refreshData(xianList);
        xiangWv.refreshData(xiangList);
        cunWv.refreshData(cunList);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    public void setTitle(String title) {
        text_title.setText(title);
    }

    private void initView() {
        text_ok = (TextView) view.findViewById(R.id.choose_list_ok);
        text_title = (TextView) view.findViewById(R.id.choose_list_title);
        wheelview = (WheelView) view.findViewById(R.id.address_xian_wv);
        xiangWv = (WheelView) view.findViewById(R.id.address_xiang_wv);
        cunWv = (WheelView) view.findViewById(R.id.address_cun_wv);

//        wheelview.setOffset(2);
//        xiangWv.setOffset(2);
//        cunWv.setOffset(2);
    }

    private OnOkClickListener onOkClickListener;

    public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    public interface OnOkClickListener {
        void onClickOk(int[] indexs, String address);
    }

}
