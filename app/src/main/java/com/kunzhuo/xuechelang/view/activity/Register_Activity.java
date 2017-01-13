package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import com.kunzhuo.xuechelang.wxapi.ContantsUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by waaa on 2016/9/2.
 */
public class Register_Activity extends BaseActivity {
    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_editPhone)
    EditText idEditPhone;
    @BindView(R.id.id_editPsssword)
    EditText idEditPsssword;
    @BindView(R.id.id_editInvitationcode)
    EditText idEditInvitationcode;
    @BindView(R.id.id_identifycode)
    EditText idIdentifycode;
    @BindView(R.id.register_submit)
    Button registerSubmit;
    @BindView(R.id.id_BtngetIndentify)
    TimeButton idBtngetIndentify;
    @BindView(R.id.id_protocol)
    TextView idProtocol;
    private Context context;
    private ProgressDialog dialog;

    int[] checkTypeStr = new int[]{0, 1};
    // 分配空间1
    CheckBox[] checkBoxsType = new CheckBox[2];
    int Type = 0;

    public static IWXAPI WXapi;
    private String weixinCode;
    private String get_access_token = "";
    private String access_token = "";

    private HttpUtils httpUtils;

    @Override
    protected int setLayoutId() {
        return R.layout.register_layout;
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
        AndroidApplication.activityList.add(this);
        context = this;
        itemTitlemsg.setText("注册信息");
        idEditPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        idIdentifycode.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        idProtocol.setMovementMethod(LinkMovementMethod.getInstance());
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在提交数据, 请稍后...");

        httpUtils = new HttpUtils(10000);

        WXapi = WXAPIFactory.createWXAPI(context,
                ContantsUtils.WX_APP_ID, true);
        WXapi.registerApp(ContantsUtils.WX_APP_ID);

        initCheckBox();
    }

    private void initCheckBox() {
        checkBoxsType[0] = (CheckBox) findViewById(R.id.type_studentBtn);
        checkBoxsType[1] = (CheckBox) findViewById(R.id.type_coachBtn);

        for (int i = 0; i < checkBoxsType.length; i++) {

            checkBoxsType[i].setOnCheckedChangeListener(new MyCheckChangeListenerType());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        judgeWXcode();
    }

    class MyCheckChangeListenerType implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.type_studentBtn:

                    if (checkBoxsType[0].isChecked()) {
                        checkBoxsType[1].setChecked(false);
                        Type = checkTypeStr[0];

                    } else {
                        Type = checkTypeStr[0];
                    }

                    break;
                case R.id.type_coachBtn:

                    if (checkBoxsType[1].isChecked()) {
                        checkBoxsType[0].setChecked(false);
                        Type = checkTypeStr[1];

                    } else {
                        Type = checkTypeStr[0];

                    }
                    break;

            }

        }
    }

    @OnClick({R.id.item_back, R.id.register_submit, R.id.id_BtngetIndentify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.register_submit:
                String phoneNum = idEditPhone.getText().toString();
                String password = idEditPsssword.getText().toString();
                String yqm = idEditInvitationcode.getText().toString();
                String indentify = idIdentifycode.getText().toString();

                if (ToolsUtils.isMobileNO(phoneNum)) {
                    if (ToolsUtils.checkPasswdLegality(password) && (password.length() > 5 && password.length() < 17)) {
                        if (indentify.length() == 4) {
                            dialog.show();

                            Resquest.registerUserRegs(handler, phoneNum, password, Type, yqm, indentify);

                        } else {
                            ToastUtil.show(context, "请输入正确的验证码");
                        }

                    } else {
                        ToastUtil.show(context, "请输入正确的密码, 6到16位字母数字或者特殊字符组成");
                    }

                } else {
                    ToastUtil.show(context, "请输入正确的手机号码");
                }


                break;
            case R.id.id_BtngetIndentify:
                String phone = idEditPhone.getText().toString();
                if (ToolsUtils.isMobileNO(phone)) {
                    Resquest.sendSendSMS(handler2, phone, 1);

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
                    dialog.cancel();
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    System.out.println(jsonObject.toString() + " 注册返回");
                    try {
                        String code = jsonObject.getString("Code");

                        if (code.equals("0")) {
                            ToastUtil.show(context, "注册成功");

                            java.lang.reflect.Type type = new TypeToken<List<User_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);

                            List<User_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            User_Bean bean = mList.get(0);

                            if (bean.getWeChat() == 0) {  // 未绑定微信

                                Uid = bean.getUID();

                                ToastUtil.show(context, "注册成功， 为您跳转微信绑定界面");

                                WXLogin();

                            } else {

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


                        }
                        if (code.equals("8")) {
                            ToastUtil.show(context, "验证码错误");
                        }
                        if (code.equals("5")) {
                            ToastUtil.show(context, "您的号码已注册, 请返回登录");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    dialog.cancel();
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

                    System.out.println(jsonObject.toString() + " 获取短信返回");
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

    /**
     * �微信登录�
     */
    private void WXLogin() {

        SendAuth.Req req = new SendAuth.Req();

        // 授权读取用户信息
        req.scope = "snsapi_userinfo";
        // 自定义信息
        req.state = "wechat_sdk_demo";
        // 向微信发送请求
        WXapi.sendReq(req);

    }

    public void judgeWXcode() {

        if (AndroidApplication.getResp() != null) {

            if (AndroidApplication.getResp().getType() == ConstantsAPI.COMMAND_SENDAUTH) {
                // code����
                weixinCode = ((SendAuth.Resp) AndroidApplication.getResp()).code;

				/*
                 * ����ǰ��õ���AppID��AppSecret��code��ƴ�ӳ�URL
				 */
                get_access_token = getCodeRequest(weixinCode);

                Thread thread = new Thread(downloadRun);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // 清除保存信息
//            SharedPreferences preference = getSharedPreferences(
//                    SPUtils.SP_USERINFO_NAME,
//                    getActivity().MODE_WORLD_WRITEABLE);
//            SharedPreferences.Editor editor = preference.edit();
//            editor.clear().commit();
        }
    }

    /**
     * @param code ��Ȩʱ��΢�Żص�����
     * @return URL
     */
    public static String getCodeRequest(String code) {
        String result = null;
        result = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + urlEnodeUTF8(ContantsUtils.WX_APP_ID) + "&secret="
                + urlEnodeUTF8(ContantsUtils.WX_AppSecret) + "&code="
                + urlEnodeUTF8(code) + "&grant_type=authorization_code";
        return result;
    }

    /**
     * @param access_token ��ȡaccess_tokenʱ����
     * @param openid       ��ȡaccess_tokenʱ����
     * @return URL
     */
    public static String getUserInfo(String access_token, String openid) {
        String result = null;
        result = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + urlEnodeUTF8(access_token) + "&openid="
                + urlEnodeUTF8(openid);
        return result;
    }

    public static String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Runnable downloadRun = new Runnable() {

        @Override
        public void run() {
            WXGetAccessToken();
        }
    };

    private void WXGetAccessToken() {

        httpUtils.send(HttpRequest.HttpMethod.POST, get_access_token,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException e, String msg) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String rule = responseInfo.result;

                        JsonParser jsonParser = new JsonParser();
                        JsonElement jsonElement = jsonParser.parse(rule); // 将json字符串转换成JsonElement
                        JsonObject jsonObject = jsonElement.getAsJsonObject();

                        if (rule != null) {
                            access_token = jsonObject.get("access_token").getAsString(); //(String) jsonObject.get("access_token");
                            String openid = jsonObject.get("openid").getAsString(); //(String) jsonObject.get("openid");
                            String get_user_info_url = getUserInfo(
                                    access_token, openid);

                            WXGetUserInfo(get_user_info_url);
                        }

                    }
                });

    }

    /**
     * @param get_user_info_url ����URL
     */
    private void WXGetUserInfo(String get_user_info_url) {

        httpUtils.send(HttpRequest.HttpMethod.POST, get_user_info_url,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException e, String msg) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        String rule = responseInfo.result;

                        JsonParser jsonParser = new JsonParser();
                        JsonElement jsonElement = jsonParser.parse(rule); // 将json字符串转换成JsonElement
                        JsonObject jsonObject = jsonElement.getAsJsonObject();

                        unionid = jsonObject.get("unionid").getAsString(); //(String) jsonObject.get("unionid");
                        openid = jsonObject.get("openid").getAsString(); //(String) json1.get("openid");
                        nickname = jsonObject.get("nickname").getAsString(); //(String) json1.get("nickname");
                        headimgurl = jsonObject.get("headimgurl").getAsString(); // (String) json1.get("headimgurl");

                        Resquest.judgeWeChatJudgment(handlerjudgeWechat, openid);

                        AndroidApplication.setResp(null);

                    }
                });

    }

    String openid = "";
    String unionid = "";
    String nickname = "";
    String headimgurl = "";
    String Uid = "";

    Handler handlerjudgeWechat = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {

                            ToastUtil.show(context, "您的微信号已经绑定过，正在为您跳转");
                            //调用微信登录的方法
                            Resquest.loginUserLogin_WeChat(handler, openid);
                        }
                        if (Code.equals("8")) {

                            Resquest.addUserWeChatAdd(handlerWeiChat, Uid, openid, nickname, headimgurl, unionid);

                        }
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

    Handler handlerWeiChat = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String code = jsonObject.getString("Code");

                        if (code.equals("0")) {
                            String uid = jsonObject.getString("Uid");
                            int Type = jsonObject.getInt("Type");

                            if (Type == 0) {

                                try {
                                    java.lang.reflect.Type type = new TypeToken<List<User_Bean>>() {
                                    }.getType();
                                    SimpleDataMapper mapper = new SimpleDataMapper(type);

                                    List<User_Bean> mList = mapper.transformCollection(jsonObject, "List");

                                    User_Bean bean = mList.get(0);
                                    String Uid = bean.getUID();
                                    String openid = bean.getOpenid();
                                    Gson gson = new Gson();
                                    String msgJson = gson.toJson(bean);

                                    DefaultUtils.putShared(context, msgJson, DefaultUtils.USER, DefaultUtils.USER_JSONMSG);
                                    DefaultUtils.putShared(context, Uid, DefaultUtils.USER, DefaultUtils.USER_ID);
                                    DefaultUtils.putShared(context, openid, DefaultUtils.USER, DefaultUtils.OPEN_ID);

                                    for (int i = 0; i < AndroidApplication.activityList.size(); i++) {

                                        AndroidApplication.activityList.get(i).finish();
                                    }
                                    AndroidApplication.activityList.clear();

                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtra("index", 0);
                                    startActivity(intent);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (Type == 1) {

                                Intent intent = new Intent(context, Adduserpwd_Acitvity.class);
                                intent.putExtra("Uid", uid);
                                startActivity(intent);

                            }
                            if (Type == 3) {

                                ToastUtil.show(context, "您的用户信息还未设置完整,为您跳转到设置界面");
                                Intent intent = new Intent(context, Adduserpwd_Acitvity.class);
                                intent.putExtra("Uid", uid);
                                startActivity(intent);
                            }
                        }
                        if (code.equals("8")) {
                            ToastUtil.show(context, "验证码错误");

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
