package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.MapCommentList_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.MapComment_Adapter;
import com.kunzhuo.xuechelang.widget.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/15 0015.
 */
public class ShowCommentAll_Activity extends BaseActivity {


    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_coachcommentList)
    MyListView idCoachcommentList;
    @BindView(R.id.id_ComToBtn)
    Button idComToBtn;

    private Context context;
    private MapComment_Adapter comment_Adapter;
    private String ID = "";
    private int CommentCount = 1;
    private ProgressDialog dialog;
    private String TotalID;


    @Override
    protected int setLayoutId() {
        return R.layout.showcoachcomment_layout;
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

        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载, 请稍后...");
        dialog.show();

        ID = getIntent().getStringExtra("ID");
        CommentCount = getIntent().getIntExtra("CommentCount", 0);
        String Name = getIntent().getStringExtra("Name");
        TotalID = getIntent().getStringExtra("TotalID");
        itemTitlemsg.setText(Name + "的全部评论");
        comment_Adapter = new MapComment_Adapter(context, null);
        idCoachcommentList.setAdapter(comment_Adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Resquest.getMapCommentList(handler1, ID, 1, CommentCount);
    }

    @OnClick({R.id.item_back, R.id.item_titlemsg, R.id.id_ComToBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.item_titlemsg:
                break;
            case R.id.id_ComToBtn:

                String openid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.OPEN_ID);

                if (!openid.equals("")) {

                    Intent intent2 = new Intent(context, CoachComment_Activity.class);
                    intent2.putExtra("TotalID", TotalID);
                    startActivity(intent2);


                } else {
                    ToastUtil.show(context, "您还未登录,无法发表评论");
                }

                break;
        }
    }

    Handler handler1 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        CommentCount = jsonObject.getInt("CommCount");
                        Type type = new TypeToken<List<MapCommentList_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<MapCommentList_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        comment_Adapter.setData(mList);
                        comment_Adapter.notifyDataSetChanged();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
