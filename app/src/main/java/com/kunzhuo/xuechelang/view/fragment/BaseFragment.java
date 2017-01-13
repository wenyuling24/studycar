package com.kunzhuo.xuechelang.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zhangQ
 * @date 2015-12-21
 * @description fragment父类
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    private Unbinder unbinder;
    protected WeakReference<View> mRootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mRootView == null || mRootView.get() == null) {
            View view = inflater.inflate(setLayoutId(), container, false);
            mRootView = new WeakReference<>(view);
        } else {

            ViewGroup parent = (ViewGroup) mRootView.get().getParent();
            if (parent != null) {
                parent.removeView(mRootView.get());
            }

        }

        unbinder = ButterKnife.bind(this, mRootView.get());
        setDefaultViews();
        setDefaultViews(mRootView.get());
        requestData();

        return mRootView.get();
    }

    protected abstract int setLayoutId();

    protected abstract void setDefaultViews();

    /**
     * 可不继承
     *
     * @param view
     */
    protected void setDefaultViews(View view) {
    }

    protected abstract void requestData();

    protected abstract void updateViews();

    protected abstract void upDetoryViews();


    protected void toastMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    protected void toastMessage(int message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {

        upDetoryViews();
        super.onDestroyView();
        unbinder.unbind();

    }


}
