package com.kunzhuo.xuechelang.view.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.AnswerList_Bean;
import com.kunzhuo.xuechelang.model.Answer_Bean;
import com.kunzhuo.xuechelang.model.TitleList_KM_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by waaa on 2016/9/1.
 * 题目加答案
 */
public class Problem_Activity extends BaseActivity {
    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.item_Imgcenter)
    ImageView itemImgcenter;
    @BindView(R.id.item_num)
    TextView itemNum;
    @BindView(R.id.item_centerLayout)
    RelativeLayout itemCenterLayout;
    @BindView(R.id.item_Imgright)
    ImageView itemImgright;
    @BindView(R.id.item_study)
    TextView itemStudy;
    @BindView(R.id.item_rightLayout)
    RelativeLayout itemRightLayout;
    @BindView(R.id.id_problemType)
    TextView idProblemType;
    @BindView(R.id.id_problemTextMsg)
    TextView idProblemTextMsg;
    @BindView(R.id.id_problemImgMsg)
    ImageView idProblemImgMsg;
    @BindView(R.id.id_problemMsglayout)
    LinearLayout idProblemMsglayout;

    private Context context;

    @Override
    protected int setLayoutId() {
        return R.layout.problem_layout;
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
        itemTitlemsg.setVisibility(View.GONE);
        itemCenterLayout.setVisibility(View.VISIBLE);
        itemRightLayout.setVisibility(View.VISIBLE);
        int Type = getIntent().getIntExtra("Type", 1);
        String T_Subject = getIntent().getStringExtra("T_Subject");
        int ZJ_Count = getIntent().getIntExtra("ZJ_Count", 0);
//        Resquest.getTitleList_KM(handler, Type, T_Subject, 1, 1, Uid);

    }


    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        Type type = new TypeToken<List<TitleList_KM_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<TitleList_KM_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        String ID = mList.get(0).getID();
                        Resquest.getTitleAnswerList(handler2, ID);

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

    Handler handler2 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<Answer_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<Answer_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        System.out.println(mList.toString() + " 获取的内容2转换");

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

    @OnClick(R.id.item_back)
    public void onClick() {
        finish();
    }
}
