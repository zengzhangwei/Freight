package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.mode.BaseOpinionEntity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.utils.GsonUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 18/1/2
 */
public class FeedbackFragment extends BaseFragment {

    @BindView(R.id.et_feedback)
    EditText etFeedback;
    @BindView(R.id.tv_commit_feedback)
    TextView tvCommitFeedback;
    Unbinder unbinder;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    public static FeedbackFragment newInstance() {

        Bundle args = new Bundle();

        FeedbackFragment fragment = new FeedbackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_commit_feedback)
    public void onViewClicked() {
        commit();
    }

    /**
     * 提交意见反馈
     */
    private void commit() {
        String content = etFeedback.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showToast("内容不能为空");
            return;
        }

        if (content.length() < 20) {
            showToast("内容长度不足");
            return;
        }

        BaseOpinionEntity entity = new BaseOpinionEntity();
        entity.setOpinion(content);
        entity.setUserId(SpUtils.getUserData(mActivity).getId());
        Map<String, String> params = new HashMap<>();
        params.put("OpinionJson", GsonUtils.toJson(entity));
        showDialog("提交意见中...");
        SoapUtils.Post(mActivity, API.InsertOpinion, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                hideDialog();
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                hideDialog();
                showToast("意见提交成功");
                mActivity.finish();
            }
        });
    }
}
