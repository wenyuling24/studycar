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
 * Created by waaa on 2016/8/30.
 */
public class CustomDialog extends Dialog {
    private TextView tv_title;
    private TextView tv_message;
    private TextView btn_insure;
    private TextView btn_cancel;
    private LinearLayout container;

    public CustomDialog(Context context) {
        super(context, R.style.BaseDialog);
        initViews(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);

        initViews(context);
    }

    private void initViews(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels * 0.8);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_base,
                null);
        setContentView(view, lp);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_message = (TextView) findViewById(R.id.tv_message);
        btn_insure = (TextView) findViewById(R.id.btn_insure);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        container = (LinearLayout) findViewById(R.id.container);
    }

    public static class Builder {
        private String title = "";
        private String message = "";
        private String positiveTest = "";
        private String negativeTest = "";
        private OnClickListener onPositiveListener;
        private OnClickListener onNegativeListener;
        private Context mContext;
        private View addedView;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public CustomDialog create() {
            final CustomDialog mDialog = new CustomDialog(mContext);
            mDialog.tv_title.setText(title);
            mDialog.tv_message.setText(message);

            if (!TextUtils.isEmpty(positiveTest)) {
                mDialog.btn_insure.setText(positiveTest);
            }

            if (!TextUtils.isEmpty(negativeTest)) {
                mDialog.btn_cancel.setText(negativeTest);
            }
            if (addedView != null) {
                mDialog.tv_message.setVisibility(View.GONE);
                mDialog.container.addView(addedView);
            }

            if (onPositiveListener != null) {
                mDialog.btn_insure
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                onPositiveListener.onClick(mDialog,
                                        DialogInterface.BUTTON_POSITIVE);
                            }
                        });
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

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int rid) {
            this.message = mContext.getString(rid);
            return this;
        }

        public Builder setOnPositiveListener(
                OnClickListener onClickListener) {
            onPositiveListener = onClickListener;
            return this;
        }

        public Builder setOnNeagtiveListener(
                OnClickListener onClickListener) {
            onNegativeListener = onClickListener;
            return this;
        }

        public Builder setOnPositiveListener(String btnName,
                                             OnClickListener onClickListener) {
            positiveTest = btnName;
            onPositiveListener = onClickListener;
            return this;
        }

        public Builder setOnNeagtiveListener(String btnName,
                                             OnClickListener onClickListener) {
            negativeTest = btnName;
            onNegativeListener = onClickListener;
            return this;
        }

        public Builder setOnPositiveListener(int nameId,
                                             OnClickListener onClickListener) {
            positiveTest = mContext.getString(nameId);
            onPositiveListener = onClickListener;
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
