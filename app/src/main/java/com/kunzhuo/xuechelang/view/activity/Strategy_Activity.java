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
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.NoviceList_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.NormalUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.Novice_Adapter;
import com.kunzhuo.xuechelang.widget.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/26 0026.
 */
public class Strategy_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_staList)
    MyListView idStaList;


    private Context context;
    private ProgressDialog dialog;
    private Novice_Adapter novice_Adapter;
    private int Type = 0;

    @Override
    protected int setLayoutId() {
        return R.layout.strategy_layout;
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
        itemTitlemsg.setText("学车攻略");
        novice_Adapter = new Novice_Adapter(context, null);
        idStaList.setAdapter(novice_Adapter);
        Type = getIntent().getIntExtra("Type", 0);

        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载, 请稍后...");
        dialog.show();

        idStaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NoviceList_Bean bean = (NoviceList_Bean) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(context, WebView_Activity.class);
                intent.putExtra("Title", bean.getItitle());
                intent.putExtra("IID", bean.getIID());
                startActivity(intent);
            }
        });

        Resquest.getNoviceList(handler, Type, 1, 48);
    }

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

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<NoviceList_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<NoviceList_Bean> mList = mapper.transformCollection(jsonObject, "List");

                        novice_Adapter.setData(mList);
                        novice_Adapter.notifyDataSetChanged();

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
