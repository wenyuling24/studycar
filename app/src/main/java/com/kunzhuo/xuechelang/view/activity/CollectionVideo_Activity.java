package com.kunzhuo.xuechelang.view.activity;

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
import com.kunzhuo.xuechelang.model.Video_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.VideoSecond_Adapter2;
import com.kunzhuo.xuechelang.widget.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class CollectionVideo_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_videotxt)
    TextView idVideotxt;
    @BindView(R.id.id_videolistview)
    MyListView idVideolistview;


    private Context context;
    private VideoSecond_Adapter2 adapter;
    private String Uid;

    @Override
    protected int setLayoutId() {
        return R.layout.collectvideo_layout;
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
        itemTitlemsg.setText("视频收藏");
        Uid = getIntent().getStringExtra("Uid");

        adapter = new VideoSecond_Adapter2(context, null);
        idVideolistview.setAdapter(adapter);

        idVideolistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(context, PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 2);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        Resquest.getCollection_Sel(handler, Uid, 1, 100);
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

                            idVideotxt.setVisibility(View.GONE);
                            idVideolistview.setVisibility(View.VISIBLE);

                            Type type = new TypeToken<List<Video_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<Video_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();

                        }
                        if (Code.equals("8")) {

                            idVideotxt.setVisibility(View.VISIBLE);
                            idVideolistview.setVisibility(View.GONE);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
}
