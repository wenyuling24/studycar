package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.ToolsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/2 0002.
 * 免费试驾
 */
public class TextDrive_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_editName)
    EditText idEditName;
    @BindView(R.id.id_editPhone)
    EditText idEditPhone;
    @BindView(R.id.id_TextDriveBtn)
    Button idTextDriveBtn;


    private Context context;
    private ProgressDialog dialog;

    @Override
    protected int setLayoutId() {
        return R.layout.textdrive_layout;
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
        itemTitlemsg.setText("学车郎—试驾");

        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载, 请稍后...");
        idEditPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);

    }


    @OnClick({R.id.item_back, R.id.id_TextDriveBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.id_TextDriveBtn:
                String Name = idEditName.getText().toString();
                String Phone = idEditPhone.getText().toString();

                if (!Name.equals("")) {

                    if (!Phone.equals("") && ToolsUtils.isMobileNO(Phone)) {

                        dialog.show();

                        Resquest.textTestDrive_Add(handler, Name, Phone);

                    } else {
                        ToastUtil.show(context, "请输入正确的电话号码");
                    }

                } else {

                    ToastUtil.show(context, "姓名不能为空");
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
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {

                            ToastUtil.show(context, "提交成功, 我们的客服会在24小时内和您联系");
                            finish();
                        }
                        if (Code.equals("8")) {


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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
