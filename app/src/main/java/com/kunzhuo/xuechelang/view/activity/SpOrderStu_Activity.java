package com.kunzhuo.xuechelang.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.SpOrdersStu_Bean;
import com.kunzhuo.xuechelang.model.SpOrders_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.SpOrderStu_Adapter;
import com.kunzhuo.xuechelang.view.adapter.SpOrder_Adapter;
import com.kunzhuo.xuechelang.widget.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/7 0007.
 */
public class SpOrderStu_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_coachlisttxt)
    TextView idCoachlisttxt;
    @BindView(R.id.first_trystudylistview)
    MyListView firstTrystudylistview;

    private Context context;
    private String Uid = "";
    private SpOrderStu_Adapter adapter;

    @Override
    protected int setLayoutId() {
        return R.layout.trystudy_layout;
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
        itemTitlemsg.setText("学车郎—我的陪练订单");
        Uid = getIntent().getStringExtra("Uid");

        adapter = new SpOrderStu_Adapter(context, null);
        firstTrystudylistview.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Resquest.getSpOrdered_I(handlerList, Uid);
    }

    Handler handlerList = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {
                            idCoachlisttxt.setVisibility(View.GONE);
                            firstTrystudylistview.setVisibility(View.VISIBLE);

                            Type type = new TypeToken<List<SpOrdersStu_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<SpOrdersStu_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();
                        }
                        if (Code.equals("8")) {
                            idCoachlisttxt.setVisibility(View.VISIBLE);
                            firstTrystudylistview.setVisibility(View.GONE);
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


    @OnClick({R.id.item_back, R.id.item_titlemsg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.item_titlemsg:
                break;
        }
    }
}