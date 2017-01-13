package com.kunzhuo.xuechelang.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.AndroidApplication;
import com.kunzhuo.xuechelang.MainActivity;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.User_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.ToolsUtils;
import com.kunzhuo.xuechelang.widget.TimeButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by waaa on 2016/9/2.
 */
public class IndentifyLogin_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_identifyLoginPhone)
    EditText idIdentifyLoginPhone;
    @BindView(R.id.id_identifyLogincode)
    EditText idIdentifyLogincode;
    @BindView(R.id.id_identifyLoginBtngetIndentify)
    TimeButton idIdentifyLoginBtngetIndentify;
    @BindView(R.id.identifyLogin_submit)
    Button identifyLoginSubmit;
    private Context context;

    @Override
    protected int setLayoutId() {
        return R.layout.identifylogin_layout;
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
        AndroidApplication.activityList.add(this);
        idIdentifyLoginPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        idIdentifyLogincode.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        itemTitlemsg.setText("短信验证码登录");

    }


    @OnClick({R.id.item_back, R.id.id_identifyLoginBtngetIndentify, R.id.identifyLogin_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.id_identifyLoginBtngetIndentify:
                String phone = idIdentifyLoginPhone.getText().toString();

                if (ToolsUtils.isMobileNO(phone)) {
                    Resquest.sendSendSMS(handler, phone, 3);

                } else {

                    ToastUtil.show(context, "请输入正确的手机号");
                }
                break;
            case R.id.identifyLogin_submit:
                String phoneNum = idIdentifyLoginPhone.getText().toString();
                String indentifyCode = idIdentifyLogincode.getText().toString();

                if (ToolsUtils.isMobileNO(phoneNum)) {
                    if (indentifyCode.length() == 4) {
                        Resquest.login_UserInform(handler2, phoneNum, "", indentifyCode);
                    } else {
                        ToastUtil.show(context, "请输入正确的验证码");
                    }

                } else {
                    ToastUtil.show(context, "请输入正确的手机号");
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

                    System.out.println(jsonObject.toString() + " 获取短信1");
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

                    System.out.println(jsonObject.toString() + " 验证码登录1");
                    try {
                        String code = jsonObject.getString("Code");

                        if (code.equals("0")) {
                            ToastUtil.show(context, "登录成功");
                            java.lang.reflect.Type type = new TypeToken<List<User_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);

                            List<User_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            User_Bean bean = mList.get(0);
                            String Uid = bean.getUID();
                            Gson gson = new Gson();
                            String msgJson = gson.toJson(bean);

                            DefaultUtils.putShared(context, msgJson, DefaultUtils.USER, DefaultUtils.USER_JSONMSG);
                            DefaultUtils.putShared(context, Uid, DefaultUtils.USER, DefaultUtils.USER_ID);

                            for (int i = 0; i < AndroidApplication.activityList.size(); i++) {

                                AndroidApplication.activityList.get(i).finish();
                            }
                            AndroidApplication.activityList.clear();

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("index", 0);
                            startActivity(intent);

                        }
                        if (code.equals("8")) {
                            ToastUtil.show(context, "验证码错误");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
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
