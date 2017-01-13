package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.Peilian_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.Peilian_Adapter;
import com.kunzhuo.xuechelang.widget.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/31 0031.
 */
public class Peilian_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.first_peilist)
    MyListView firstPeilist;
    @BindView(R.id.id_RightTxt)
    TextView idRightTxt;
    @BindView(R.id.id_rightLayoutWeichat)
    RelativeLayout idRightLayoutWeichat;

    private Context context;
    private Peilian_Adapter adapter;
    private ProgressDialog dialog;
    private String Uid = "";

    @Override
    protected int setLayoutId() {
        return R.layout.peilian_layout;
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

        Uid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_ID);

        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载, 请稍后...");
        dialog.show();

        idRightLayoutWeichat.setVisibility(View.VISIBLE);
        itemTitlemsg.setText("学车郎—陪练服务");
        idRightTxt.setText("我的陪练");
        adapter = new Peilian_Adapter(context, null);
        firstPeilist.setAdapter(adapter);

        firstPeilist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Peilian_Bean bean = (Peilian_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(context, PeiDesc_Activity.class);
                intent.putExtra("Id", bean.getID());
                startActivity(intent);
            }
        });

        Resquest.getSparringList(handler, 1, 100);
    }

    @OnClick({R.id.item_back, R.id.item_titlemsg, R.id.id_rightLayoutWeichat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.item_titlemsg:
                break;
            case R.id.id_rightLayoutWeichat:
                Intent intent = new Intent(context, SpOrderStu_Activity.class);
                intent.putExtra("Uid", Uid);
                startActivity(intent);
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

                            Type type = new TypeToken<List<Peilian_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<Peilian_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
