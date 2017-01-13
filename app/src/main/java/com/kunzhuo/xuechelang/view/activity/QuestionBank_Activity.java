package com.kunzhuo.xuechelang.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.AnswerList_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.Answer_Adapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by waaa on 2016/8/31.
 * 获取理论章节 ChapterList
 */
public class QuestionBank_Activity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.questionbank_listview)
    ListView questionbankListview;
    @BindView(R.id.item_back)
    ImageView itemBack;
    private Answer_Adapter answer_Adapter;
    private Context context;
    private int type;
    private String Uid = "";

    @Override
    protected int setLayoutId() {
        return R.layout.questionbank_layout;
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
        itemTitlemsg.setText("专项练习");
        answer_Adapter = new Answer_Adapter(this, null);
        questionbankListview.setAdapter(answer_Adapter);
        questionbankListview.setOnItemClickListener(this);
        type = getIntent().getIntExtra("ChapterListType", 1);
        Uid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_ID);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Resquest.getChapterList(handler, type, Uid);
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<AnswerList_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<AnswerList_Bean> mList = mapper.transformCollection(jsonObject, "List");

                        answer_Adapter.setData(mList);
                        answer_Adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
            }

            return true;
        }
    });

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        AnswerList_Bean bean = (AnswerList_Bean) parent.getItemAtPosition(position);

        System.out.println(bean.getID() + " bean.getID()" + bean.getZJ_Count() + "  bean.getZJ_Count() 主");

        Intent intent = new Intent(context, ProblemMain_Activity.class);
        intent.putExtra("ChapterID", bean.getID());
        intent.putExtra("RecType", 4);
        intent.putExtra("Number", fromString(bean.getNumber()));
        intent.putExtra("T_Subject", bean.getID());
        intent.putExtra("ZJ_Count", bean.getZJ_Count());
        intent.putExtra("Type", 0);
        startActivity(intent);
    }

    private int fromString(String str) {

        if (!str.equals("")) {
            int num = Integer.parseInt(str);
            return num;
        } else {
            return 0;
        }


    }


    @OnClick({R.id.item_back})
    public void viewClick(View v) {
        switch (v.getId()) {
            case R.id.item_back:
                finish();
                break;
        }
    }

}
