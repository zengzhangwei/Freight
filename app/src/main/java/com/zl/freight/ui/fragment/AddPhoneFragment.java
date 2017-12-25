package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
 * @date 17/12/25
 * 添加备用手机号
 */
public class AddPhoneFragment extends BaseFragment {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.et_input_phone1)
    EditText etInputPhone1;
    @BindView(R.id.et_input_phone2)
    EditText etInputPhone2;
    @BindView(R.id.tv_pp_ok)
    TextView tvPpOk;
    Unbinder unbinder;

    public AddPhoneFragment() {
        // Required empty public constructor
    }

    public static AddPhoneFragment newInstance() {

        Bundle args = new Bundle();

        AddPhoneFragment fragment = new AddPhoneFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_phone, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvTitle.setText("添加备用手机");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_back, R.id.tv_pp_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                getFragmentManager().popBackStack();
                break;
            //提交
            case R.id.tv_pp_ok:
                commit();
                break;
        }
    }

    /**
     * 提交
     */
    private void commit() {
        String phone1 = etInputPhone1.getText().toString().trim();
        String phone2 = etInputPhone2.getText().toString().trim();
        if (TextUtils.isEmpty(phone1) || TextUtils.isEmpty(phone2)) {
            showToast("必须填写两个手机号");
            return;
        }

        if (!RegexUtils.isMobileExact(phone1) && !RegexUtils.isMobileExact(phone2)) {
            showToast("手机号格式不对");
            return;
        }

        if (onReturnPhoneListener != null) {
            onReturnPhoneListener.onPhone(phone1, phone2);
        }

        getFragmentManager().popBackStack();
    }

    private OnReturnPhoneListener onReturnPhoneListener;

    public void setOnReturnPhoneListener(OnReturnPhoneListener onReturnPhoneListener) {
        this.onReturnPhoneListener = onReturnPhoneListener;
    }

    public interface OnReturnPhoneListener {
        void onPhone(String phone1, String phone2);
    }
}
