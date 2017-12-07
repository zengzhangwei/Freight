package com.zl.zlibrary.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanglei
 * @date 17/06/21
 */

public class FragmentHelper {

    private AppCompatActivity mActivity;
    private Fragment hideFragment;

    private FragmentHelper(Builder builder) {
        mActivity = builder.mActivity;
    }

    /**
     * 显示Fragment
     *
     * @param fragment
     */
    public void showFragment(Fragment fragment) {
        if (hideFragment != null) {
            hideFragment(hideFragment);
        }
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.show(fragment).commit();
        hideFragment = fragment;
    }

    /**
     * 隐藏Fragment
     *
     * @param fragment
     */
    private void hideFragment(Fragment fragment) {
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.hide(fragment).commit();
    }

    public static IParentLayout builder(AppCompatActivity activity) {
        return new Builder(activity);
    }

    public interface IParentLayout {
        public IFragmentInterface attach(int layoutId);
    }

    public interface IFragmentInterface {
        public IFragmentInterface addFragment(Fragment fragment);

        public IFragmentSetting beginSettings();

        public FragmentHelper commit();
    }

    public interface IFragmentSetting {
        public IFragmentInterface endSettings();
    }

    public static class Builder implements IParentLayout, IFragmentInterface, IFragmentSetting {

        private final FragmentTransaction transaction;
        private int layoutId;
        private AppCompatActivity mActivity;
        private List<Fragment> mList = new ArrayList<>();

        private Builder(AppCompatActivity context) {
            mActivity = context;
            transaction = mActivity.getSupportFragmentManager().beginTransaction();

        }

        @Override
        public IFragmentInterface attach(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        @Override
        public IFragmentInterface addFragment(Fragment fragment) {
            mList.add(fragment);
            transaction.add(layoutId, fragment).hide(fragment);
            return this;
        }

        @Override
        public IFragmentSetting beginSettings() {
            return this;
        }

        @Override
        public FragmentHelper commit() {
            transaction.commit();
            return new FragmentHelper(this);
        }

        @Override
        public IFragmentInterface endSettings() {
            return this;
        }
    }

}
