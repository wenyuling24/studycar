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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/20 0020.
 */
public class Adduserpwd_Acitvity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_editPsssword)
    EditText idEditPsssword;
    @BindView(R.id.id_editInvitationcode)
    EditText idEditInvitationcode;
    @BindView(R.id.type_studentBtn)
    CheckBox typeStudentBtn;
    @BindView(R.id.type_coachBtn)
    CheckBox typeCoachBtn;
    @BindView(R.id.register_submit)
    Button registerSubmit;


    private Context context;
    private ProgressDialog dialog;

    int[] checkTypeStr = new int[]{0, 1};

    // 分配空间1
    CheckBox[] checkBoxsType = new CheckBox[2];
    int Type = 0;
    String Uid = "";

    @Override
    protected int setLayoutId() {
        return R.layout.adduserpwd_layout;
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
        itemTitlemsg.setText("设置账户信息");
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在提交数据, 请稍后...");
        Uid = getIntent().getStringExtra("Uid");

        initCheckBox();
    }

    private void initCheckBox() {
        checkBoxsType[0] = (CheckBox) findViewById(R.id.type_studentBtn);
        checkBoxsType[1] = (CheckBox) findViewById(R.id.type_coachBtn);

        for (int i = 0; i < checkBoxsType.length; i++) {

            checkBoxsType[i].setOnCheckedChangeListener(new MyCheckChangeListenerType());
        }
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

    @OnClick({R.id.item_back, R.id.register_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.register_submit:
                String password = idEditPsssword.getText().toString();

                if (ToolsUtils.checkPasswdLegality(password) && (password.length() > 5 && password.length() < 17)) {

                    dialog.show();
                    Resquest.addUserSystemEidt(handler, Uid, password, Type);


                } else {
                    ToastUtil.show(context, "请输入正确的密码, 6到16位字母数字或者特殊字符组成");
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

                    try {
                        String code = jsonObject.getString("Code");

                        if (code.equals("0")) {
                            ToastUtil.show(context, "绑定成功");

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
}
