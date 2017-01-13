package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.MockRecord_Bean;
import com.kunzhuo.xuechelang.model.User_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.MockRecord_Adapter;
import com.kunzhuo.xuechelang.widget.MyListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by waaa on 2016/9/19.
 */
public class MockRecord_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_roardImg)
    ImageView idRoardImg;
    @BindView(R.id.id_roardNum)
    TextView idRoardNum;
    @BindView(R.id.roard_listview)
    MyListView roardListview;
    @BindView(R.id.scroll)
    ScrollView scroll;

    private Context context;
    private String Uid = "";
    private int Type = 1;
    private MockRecord_Adapter adapter;
    private ProgressDialog dialog;

    @Override
    protected int setLayoutId() {
        return R.layout.mockroard_layout;
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

        itemTitlemsg.setText("考试记录");

        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载, 请稍后...");
        dialog.show();

        Type = getIntent().getIntExtra("Type", 1);

        Uid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_ID);

        adapter = new MockRecord_Adapter(context, null);
        roardListview.setAdapter(adapter);

        String userJson = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_JSONMSG);

        getUserPhone(userJson);
    }

    public void getUserPhone(String userJson) {

        if (!userJson.equals("")) {
            Gson gson = new Gson();
            User_Bean bean = gson.fromJson(userJson, User_Bean.class);


            if (!bean.getPicSrc().equals("")) {

                ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getPicSrc(), idRoardImg);

            } else {

                if (!bean.getWx_portrait().equals("")) {
                    ImageLoader.getInstance().displayImage(bean.getWx_portrait(), idRoardImg);
                }

            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        Resquest.getMnksRankingJL(handler, Uid, Type);
    }

    @OnClick(R.id.item_back)
    public void onClick() {
        finish();
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

                            java.lang.reflect.Type type = new TypeToken<List<MockRecord_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<MockRecord_Bean> mList = mapper.transformCollection(jsonObject, "List");
                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();
                            int Max = jsonObject.getInt("Max");
                            idRoardNum.setText("你的最高历史成绩为" + Max + "分");

                        }
                        if (Code.equals("8")) {

                            idRoardNum.setText("暂无考试记录");
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
