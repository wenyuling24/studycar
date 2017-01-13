package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.ChartsList_Bean;
import com.kunzhuo.xuechelang.model.User_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.widget.emouse.EmoteInputView;
import com.kunzhuo.xuechelang.widget.emouse.EmoticonsEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
public class CoachComment_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.chat_emoview)
    EmoteInputView mInputView;
    @BindView(R.id.id_comBtn)
    Button idComBtn;
    @BindView(R.id.id_comEdit)
    EmoticonsEditText mEetTextDitorEditer;

    private Context context;
    private String openid;
    private String Head_portrait;
    private String Name;
    private String ID;
    private ProgressDialog dialog;

    @Override
    protected int setLayoutId() {
        return R.layout.coachcomment_layout;
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
        itemTitlemsg.setText("发表评论");
        ID = getIntent().getStringExtra("TotalID");
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在提交,请稍后...");

        mInputView.setEditText(mEetTextDitorEditer);

        String userJson = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_JSONMSG);

        getUserPhone(userJson);
    }


    @OnClick({R.id.item_back, R.id.id_comBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.id_comBtn:

                String comment = mEetTextDitorEditer.getText().toString();

                if (comment.equals("")) {

                    ToastUtil.show(context, "输入的内容不能为空");
                } else {
                    Resquest.addMapCommentAdd(handler, ID, comment, Head_portrait, Name, openid);
                    dialog.show();
                }


                break;
        }
    }

    public void getUserPhone(String userJson) {

        if (!userJson.equals("")) {
            Gson gson = new Gson();
            User_Bean bean = gson.fromJson(userJson, User_Bean.class);

            if (!bean.getOpenid().equals("")) {

                openid = bean.getOpenid();
            }
            if (!bean.getWx_portrait().equals("")) {

                Head_portrait = bean.getWx_portrait();
            }
            if (!bean.getWx_nickname().equals("")) {

                Name = bean.getWx_nickname();
            }

        }
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {
                            ToastUtil.show(context, "评论成功");
                            dialog.cancel();
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.cancel();

                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
                    dialog.cancel();
                    break;
            }

            return true;
        }
    });
}
