package com.kunzhuo.xuechelang.view;

import android.support.annotation.StringRes;

/**
 * Created by zhangqiang on 16/5/26.
 */
public interface IBaseView {
    void showToast(String message);

    void showToast(@StringRes int Rid);

}
