package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.kunzhuo.xuechelang.model.MapCommentList_Bean;
import com.kunzhuo.xuechelang.model.TitleList_KM_Bean;
import com.kunzhuo.xuechelang.model.User_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.ToolsUtils;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by waaa on 2016/9/1.
 */
public class Login_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.item_Imgcenter)
    ImageView itemImgcenter;
    @BindView(R.id.item_num)
    TextView itemNum;
    @BindView(R.id.item_centerLayout)
    RelativeLayout itemCenterLayout;
    @BindView(R.id.item_rightTextmsg)
    TextView itemRightTextmsg;
    @BindView(R.id.item_Imgright)
    ImageView itemImgright;
    @BindView(R.id.item_study)
    TextView itemStudy;
    @BindView(R.id.item_rightLayout)
    RelativeLayout itemRightLayout;
    @BindView(R.id.id_logo)
    ImageView idLogo;
    @BindView(R.id.id_showphone)
    ImageView idShowphone;
    @BindView(R.id.id_login_input1)
    EditText idLoginInput1;
    @BindView(R.id.id_phoneshow)
    RelativeLayout idPhoneshow;
    @BindView(R.id.id_login_color4)
    TextView idLoginColor4;
    @BindView(R.id.id_showpassword)
    ImageView idShowpassword;
    @BindView(R.id.id_login_input2_1)
    EditText idLoginInput21;
    @BindView(R.id.id_passwordshow)
    RelativeLayout idPasswordshow;
    @BindView(R.id.id_login_color2)
    TextView idLoginColor2;
    @BindView(R.id.login_submit)
    Button loginSubmit;
    @BindView(R.id.login_register)
    Button loginRegister;
    @BindView(R.id.id_weichatImg)
    ImageView idWeichatImg;
    @BindView(R.id.weichat_loginlayout)
    RelativeLayout weichatLoginlayout;
    @BindView(R.id.id_indentifyImg)
    ImageView idIndentifyImg;
    @BindView(R.id.indentify_loginlayout)
    RelativeLayout indentifyLoginlayout;
    @BindView(R.id.login_password)
    RelativeLayout loginPassword;
    @BindView(R.id.sc)
    LinearLayout sc;
    @BindView(R.id.scroll)
    ScrollView scroll;

    private Context context;
    private ProgressDialog dialog;
    private HttpUtils httpUtils;

    public static IWXAPI WXapi;
    private String weixinCode;
    private String get_access_token = "";
    private String access_token = "";


    @Override
    protected int setLayoutId() {
        return R.layout.login_layout;
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
        itemTitlemsg.setText("登录");
        itemRightTextmsg.setVisibility(View.VISIBLE);
        itemRightTextmsg.setText("找回密码");
        idLoginInput1.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        dialog = new ProgressDialog(context);

        httpUtils = new HttpUtils(10000);

        WXapi = WXAPIFactory.createWXAPI(context,
                ContantsUtils.WX_APP_ID, true);
        WXapi.registerApp(ContantsUtils.WX_APP_ID);

    }

    @Override
    protected void onResume() {
        super.onResume();
        judgeWXcode();
    }

    @OnClick({R.id.item_back, R.id.item_rightTextmsg, R.id.login_submit, R.id.login_register, R.id.weichat_loginlayout, R.id.indentify_loginlayout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.item_rightTextmsg:
                Intent intentfindpassword = new Intent(context, FindPassword_Activity.class);
                startActivity(intentfindpassword);
                break;
            case R.id.login_submit:
                String phone = idLoginInput1.getText().toString();
                String password = idLoginInput21.getText().toString();

                if (ToolsUtils.isMobileNO(phone)) {
                    dialog.setMessage("正在登录, 请稍后...");
                    dialog.show();

                    Resquest.login_UserLogin(handler, phone, password, "");

                } else {
                    ToastUtil.show(context, "请输入正确的手机号码");
                }
                break;
            case R.id.login_register:
                Intent intent_register = new Intent(context, Register_Activity.class);
                startActivity(intent_register);
                break;
            case R.id.weichat_loginlayout:

                WXLogin();

                break;
            case R.id.indentify_loginlayout:
                Intent intent_indentifyLogin = new Intent(context, IndentifyLogin_Activity.class);
                startActivity(intent_indentifyLogin);
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

                    try {

                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {
                            ToastUtil.show(context, "登录成功");
                            java.lang.reflect.Type type = new TypeToken<List<User_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);

                            List<User_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            User_Bean bean = mList.get(0);
                            String Uid = bean.getUID();
                            String openid = bean.getOpenid();
                            Gson gson = new Gson();
                            String msgJson = gson.toJson(bean);

                            if (bean.getWeChat() == 0) { //未绑定微信

                                ToastUtil.show(context, "您的账号还未绑定微信,正在为您跳转微信绑定界面");

                                WXLogin();

                            } else {

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
                            }
                        }
                        if (Code.equals("1")) {
                            ToastUtil.show(context, "账号或者密码错误");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
                    dialog.cancel();
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

    Handler handlerjudgeWechat = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {

                            //调用微信登录的方法
                            Resquest.loginUserLogin_WeChat(handler, openid);
                        }
                        if (Code.equals("8")) {

                            ToastUtil.show(context, "您的微信号还没绑定手机号， 正在为您跳转到绑定界面");

                            // 去绑定手机号
                            Intent intent = new Intent(context, AsccountBind_Activity.class);
                            intent.putExtra("openid", openid);
                            intent.putExtra("wx_nickname", nickname);
                            intent.putExtra("wx_portrait", headimgurl);
                            intent.putExtra("wx_unionid", unionid);
                            startActivity(intent);

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
}
