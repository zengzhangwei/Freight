package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zl.freight.R;
import com.zl.zlibrary.base.BaseFragment;

/**
 * @author zhanglei
 * @date 17/12/11
 * 交易记录
 */
public class TransactionLogFragment extends BaseFragment {


    public TransactionLogFragment() {
        // Required empty public constructor
    }

    public static TransactionLogFragment newInstance() {

        Bundle args = new Bundle();

        TransactionLogFragment fragment = new TransactionLogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_log, container, false);
        view.setClickable(true);
        return view;
    }

}
