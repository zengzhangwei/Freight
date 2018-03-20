package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zl.freight.R;
import com.zl.zlibrary.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebFragment extends BaseFragment {


    @BindView(R.id.fragment_web)
    WebView fragmentWeb;
    Unbinder unbinder;

    public WebFragment() {
        // Required empty public constructor
    }

    public static WebFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        String url = getArguments().getString("url");
        fragmentWeb.getSettings().setJavaScriptEnabled(true);
        fragmentWeb.loadUrl(url);
        fragmentWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragmentWeb != null){
            fragmentWeb.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fragmentWeb != null){
            fragmentWeb.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (fragmentWeb != null){
            fragmentWeb.destroy();
        }
    }
}
