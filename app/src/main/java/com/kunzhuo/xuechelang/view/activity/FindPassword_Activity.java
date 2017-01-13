package com.kunzhuo.xuechelang.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunzhuo.xuechelang.AndroidApplication;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.ToolsUtils;
import com.kunzhuo.xuechelang.widget.TimeButton;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by waaa on 2016/9/2.
 */
public class FindPassword_Activity extends BaseActivity {
    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_findpasswordeditPhone)
    EditText idFindpasswordeditPhone;
    @BindView(R.id.id_findpasswordeditPsssword)
    EditText idFindpasswordeditPsssword;
    @BindView(R.id.id_findpasswordeditPassword2)
    EditText idFindpasswordeditPassword2;
    @BindView(R.id.id_findpasswordidentifycode)
    EditText idFindpasswordidentifycode;
    @BindView(R.id.id_findpasswordBtngetIndentify)
    TimeButton idFindpasswordBtngetIndentify;
    @BindView(R.id.findpassword_submit)
    Button findpasswordSubmit;
    private Context context;

    @Override
    protected int setLayoutId() {
        return R.layout.findpassword_layout;
    }

    @Override
    protected void setDefaultViews() {
        initViews();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void updateViews() {

    }

    private void initViews() {
        context = this;
        idFindpasswordeditPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        idFindpasswordidentifycode.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        itemTitlemsg.setText("找回密码");

        idFindpasswordeditPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() >= idFindpasswordeditPsssword.getText().toString().length()
                        && count > 0) {
                    if (!idFindpasswordeditPassword2.getText().toString()
                            .equals(idFindpasswordeditPsssword.getText().toString())) {
                        ToastUtil.show(context, "两次输入的密码不一致");
                        findpasswordSubmit.setClickable(false);
                    } else {
                        findpasswordSubmit.setClickable(true);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @OnClick({R.id.item_back, R.id.id_findpasswordBtngetIndentify, R.id.findpassword_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.id_findpasswordBtngetIndentify:
                String phone = idFindpasswordeditPhone.getText().toString();
                if (ToolsUtils.isMobileNO(phone)) {

                    Resquest.sendSendSMS(handler, phone, 2);

                } else {
                    ToastUtil.show(context, "请输入正确的手机号码");
                }

                break;
            case R.id.findpassword_submit:
                String phoneNum = idFindpasswordeditPhone.getText().toString();
                String indentify = idFindpasswordidentifycode.getText().toString();
                String password = idFindpasswordeditPsssword.getText().toString();

                if (ToolsUtils.isMobileNO(phoneNum)) {
                    if (ToolsUtils.checkPasswdLegality(password)) {
                        if (indentify.length() == 4) {

                            Resquest.fandUserForgetPwd(handler2, phoneNum, password, 0, indentify);

                        } else {
                            ToastUtil.show(context, "请输入正确的验证码");
                        }

                    } else {
                        ToastUtil.show(context, "请输入正确的密码，6到16位字母数字或者特殊字符组成");
                    }

                } else {
                    ToastUtil.show(context, "请输入正确的手机号码");
                }


                break;
        }
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String code = jsonObject.getString("Code");

                        if (code.equals("0")) {
                            ToastUtil.show(context, "短信验证码获取成功");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
                    break;
            }

            return true;
        }
    });

    Handler handler2 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    System.out.println(jsonObject.toString() + " 找回密码1");
                    try {
                        String code = jsonObject.getString("Code");

                        if (code.equals("0")) {
                            ToastUtil.show(context, "找回成功");
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
                    break;
            }

            return true;
        }
    });
}
