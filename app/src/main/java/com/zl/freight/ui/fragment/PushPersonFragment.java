package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.zl.freight.R;
import com.zl.zlibrary.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhanglei
 */
public class PushPersonFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_input_push_name)
    EditText etInputPushName;
    @BindView(R.id.et_input_push_phone)
    EditText etInputPushPhone;
    @BindView(R.id.tv_pp_ok)
    TextView tvPpOk;
    Unbinder unbinder;
    private String pushName;
    private String pushPhone;

    public PushPersonFragment() {
        // Required empty public constructor
    }

    public static PushPersonFragment newInstance() {

        Bundle args = new Bundle();

        PushPersonFragment fragment = new PushPersonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_push_person, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvTitle.setText("推介人信息");
        view.setClickable(true);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_back, R.id.tv_pp_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.tv_pp_ok:
                commit();
                break;
        }
    }

    /**
     * 提交数据的操作
     */
    private void commit() {
        pushName = etInputPushName.getText().toString().trim();
        pushPhone = etInputPushPhone.getText().toString().trim();

        if (TextUtils.isEmpty(pushName) || TextUtils.isEmpty(pushPhone)) {
            showToast("内容不能为空");
        }

        if (!RegexUtils.isMobileExact(pushPhone)) {
            showToast(getResources().getString(R.string.please_input_ok_phone));
            return;
        }

        if (onRetrunDataListener != null) {
            onRetrunDataListener.onReturnData(pushName, pushPhone);
            getFragmentManager().popBackStack();
        }
    }

    private OnReturnDataListener onRetrunDataListener;

    public void setOnRetrunDataListener(OnReturnDataListener onRetrunDataListener) {
        this.onRetrunDataListener = onRetrunDataListener;
    }

    public interface OnReturnDataListener {
        void onReturnData(String name, String phone);
    }

}
