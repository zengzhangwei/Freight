package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zl.freight.R;

/**
 * @author zhanglei
 * @date 17/12/7
 * 发货界面
 */
public class SendGoodsFragment extends Fragment {


    public SendGoodsFragment() {
        // Required empty public constructor
    }

    public static SendGoodsFragment newInstance() {

        Bundle args = new Bundle();

        SendGoodsFragment fragment = new SendGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_goods, container, false);
    }

}
