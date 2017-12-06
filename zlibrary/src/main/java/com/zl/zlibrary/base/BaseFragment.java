package com.zl.zlibrary.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/8/25.
 */

public class BaseFragment extends Fragment {

    protected Activity mActivity;
    protected Fragment mFragment;
    protected Handler handler = new Handler();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = this;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void showToast(String massage) {
        Toast.makeText(mActivity, massage, Toast.LENGTH_SHORT).show();
    }
}
