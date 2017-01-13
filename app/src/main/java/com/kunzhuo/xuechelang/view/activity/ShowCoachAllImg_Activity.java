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
import com.kunzhuo.xuechelang.model.MapUserPic_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.MapUserPic_Adapter_2;
import com.kunzhuo.xuechelang.widget.MyGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/15 0015.
 * 展示所有教练的照片
 */
public class ShowCoachAllImg_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_coachimgList)
    MyGridView idCoachimgList;


    private Context context;
    private ProgressDialog dialog;
    private MapUserPic_Adapter_2 adapter;
    private String ID = "";
    private int PicCount = 4;

    @Override
    protected int setLayoutId() {
        return R.layout.showcoachallimg_layout;
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
        ID = getIntent().getStringExtra("ID");
        PicCount = getIntent().getIntExtra("PicCount", 0);
        String Name = getIntent().getStringExtra("Name");
        itemTitlemsg.setText(Name + "的全部照片");

        adapter = new MapUserPic_Adapter_2(context, null);
        idCoachimgList.setAdapter(adapter);
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载, 请稍后...");
        dialog.show();

        idCoachimgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(context, GalleryActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("PicCount", PicCount);
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });

        Resquest.getMapUserPic(handler3, ID, 1, PicCount);
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

    Handler handler3 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<MapUserPic_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<MapUserPic_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        adapter.setData(mList);
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.cancel();
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
