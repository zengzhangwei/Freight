package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.utils.API;
import com.zl.freight.utils.LoginUtils;
import com.zl.zlibrary.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 17/12/19
 * 没有登录时的个人中心页
 */
public class NoLoginPersonFragment extends BaseFragment {


    @BindView(R.id.tv_to_login)
    TextView tvToLogin;
    Unbinder unbinder;

    public NoLoginPersonFragment() {
        // Required empty public constructor
    }

    public static NoLoginPersonFragment newInstance() {

        Bundle args = new Bundle();

        NoLoginPersonFragment fragment = new NoLoginPersonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_login_person, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_to_login)
    public void onViewClicked() {
        LoginUtils.jumpToActivityResult(mActivity, new Intent(), API.ISLOGIN);
    }
}
