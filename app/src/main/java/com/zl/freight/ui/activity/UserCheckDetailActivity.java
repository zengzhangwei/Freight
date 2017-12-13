package com.zl.freight.ui.activity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoLinearLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.utils.ImageLoader;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/13
 * 用户审核详情页
 */
public class UserCheckDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_check_ok)
    TextView tvCheckOk;
    @BindView(R.id.tv_check_no)
    TextView tvCheckNo;
    @BindView(R.id.user_check_detail_bottom)
    AutoLinearLayout userCheckDetailBottom;
    @BindView(R.id.user_check_detail_list)
    ListView userCheckDetailList;
    @BindView(R.id.user_check_detail_grid)
    GridView userCheckDetailGrid;
    private AlertDialog alertDialog;
    private AlertDialog etDialog;
    private List<String> mList = Arrays.asList("姓名：张磊", "手机号：15075993917", "身份证号：130526199311146468",
            "类别：司机审核", "提交日期：2017-12-13 16:57", "审核状态：未审核");
    private List<String> imgList = Arrays.asList("http://image.3761.com/pic/85241434675216.jpg",
            "http://image.3761.com/pic/5111434675216.jpg",
            "http://image.3761.com/pic/58601434675217.jpg",
            "http://image.3761.com/pic/43701434675217.jpg",
            "http://image.3761.com/pic/1191434675217.jpg",
            "http://image.3761.com/pic/34951434675217.jpg");

    private UniversalAdapter<String> mAdapter;
    private UniversalAdapter<String> imgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check_detail);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initListener() {
        userCheckDetailGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mActivity, PictureActivity.class);
                intent.putExtra("url", imgList.get(i));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, view, PictureActivity.PICTURE);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    private void initView() {
        tvTitle.setText(R.string.user_check);
        alertDialog = new AlertDialog.Builder(mActivity)
                .setMessage("确定通过审核吗")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        commitOk();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();

        final EditText et = new EditText(this);

        etDialog = new AlertDialog.Builder(this).setTitle("添加问题描述")
                .setView(et)
                .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String data = et.getText().toString().trim();
                        if (TextUtils.isEmpty(data)) {
                            showToast("内容描述不能为空");
                            return;
                        } else {
                            commitNo(data);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();

        mAdapter = new UniversalAdapter<String>(mActivity, mList, R.layout.text_item) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                holder.setText(R.id.text_item, s);
            }
        };

        imgAdapter = new UniversalAdapter<String>(mActivity, imgList, R.layout.image_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                ImageView view = holder.getView(R.id.iv_item);
                ImageLoader.loadImageUrl(mActivity, s, view);
            }
        };

        userCheckDetailList.setAdapter(mAdapter);
        userCheckDetailGrid.setAdapter(imgAdapter);

    }

    /**
     * 通过审核
     */
    private void commitOk() {
        finish();
    }

    /**
     * 驳回审核
     *
     * @param content
     */
    private void commitNo(String content) {
        finish();
    }

    @OnClick({R.id.iv_back, R.id.tv_check_ok, R.id.tv_check_no})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_check_ok:
                alertDialog.show();
                break;
            case R.id.tv_check_no:
                etDialog.show();
                break;
        }
    }
}
