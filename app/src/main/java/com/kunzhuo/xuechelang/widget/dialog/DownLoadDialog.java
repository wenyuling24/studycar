package com.kunzhuo.xuechelang.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;


/**
 * @author zhangQ
 * @date 2015-12-24
 * @description
 */
public class DownLoadDialog extends Dialog {
    private TextView tv_title;
    private TextView btn_cancel;
    private LinearLayout container;

    public DownLoadDialog(Context context) {
        super(context, R.style.BaseDialog);

        initViews(context);
    }

    public DownLoadDialog(Context context, int theme) {
        super(context, theme);

        initViews(context);
    }

    private void initViews(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels * 0.8);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_download, null);
        setContentView(view, lp);

        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        container = (LinearLayout) findViewById(R.id.container);
    }

    public static class Builder {
        private String title = "";
        private String negativeTest = "";
        private OnClickListener onNegativeListener;
        private Context mContext;
        private View addedView;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public DownLoadDialog create() {
            final DownLoadDialog mDialog = new DownLoadDialog(mContext);
            mDialog.tv_title.setText(title);

            if (!TextUtils.isEmpty(negativeTest)) {
                mDialog.btn_cancel.setText(negativeTest);
            }
            if (addedView != null) {
                mDialog.container.addView(addedView);
            }

            if (onNegativeListener != null) {
                mDialog.btn_cancel
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                onNegativeListener.onClick(mDialog,
                                        DialogInterface.BUTTON_NEGATIVE);
                            }
                        });
            }
            return mDialog;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(int rid) {
            this.title = mContext.getString(rid);
            return this;
        }

        public Builder setOnNeagtiveListener(
                OnClickListener onClickListener) {
            onNegativeListener = onClickListener;
            return this;
        }

        public Builder setOnNeagtiveListener(String btnName,
                                             OnClickListener onClickListener) {
            negativeTest = btnName;
            onNegativeListener = onClickListener;
            return this;
        }

        public Builder setOnNeagtiveListener(int nameId,
                                             OnClickListener onClickListener) {
            negativeTest = mContext.getString(nameId);
            onNegativeListener = onClickListener;
            return this;
        }

        public Builder addView(View v) {
            this.addedView = v;
            return this;
        }
    }

}
