package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zl.freight.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 17/12/7
 * 发货界面
 */
public class SendGoodsFragment extends Fragment {


    @BindView(R.id.tv_tonne)
    TextView tvTonne;
    @BindView(R.id.tv_square)
    TextView tvSquare;
    Unbinder unbinder;
    @BindView(R.id.editText)
    EditText editText;

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
        View view = inflater.inflate(R.layout.fragment_send_goods, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvTonne.setSelected(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_tonne, R.id.tv_square})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_tonne:
                tvTonne.setSelected(true);
                tvSquare.setSelected(false);
                break;
            case R.id.tv_square:
                tvTonne.setSelected(false);
                tvSquare.setSelected(true);
                break;
        }
    }
}
