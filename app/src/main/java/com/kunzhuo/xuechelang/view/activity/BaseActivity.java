package com.kunzhuo.xuechelang.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.Toast;

import com.kunzhuo.xuechelang.view.IBaseView;
import com.umeng.analytics.MobclickAgent;


import butterknife.ButterKnife;

/**
 * 项目activity父类
 *
 * @author 1234
 */
public abstract class BaseActivity extends FragmentActivity implements IBaseView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(setLayoutId());

        ButterKnife.bind(this);

        setDefaultViews();

        updateViews();

        requestData();

    }


    protected abstract int setLayoutId();

    protected abstract void setDefaultViews();

    protected abstract void requestData();

    protected abstract void updateViews();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }


    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
