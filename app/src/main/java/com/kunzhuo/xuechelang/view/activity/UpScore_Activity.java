package com.kunzhuo.xuechelang.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by waaa on 2016/9/19.
 */
public class UpScore_Activity extends BaseActivity {

    @BindView(R.id.item_Scoreback)
    ImageView itemScoreback;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_ScoreBtnLayout)
    LinearLayout idScoreBtnLayout;
    @BindView(R.id.id_Scoretitle)
    RelativeLayout idScoretitle;
    @BindView(R.id.id_ScoreImg)
    ImageView idScoreImg;
    @BindView(R.id.id_SoreNum)
    TextView idSoreNum;
    @BindView(R.id.id_SoreStr1)
    TextView idSoreStr1;
    @BindView(R.id.id_SoreTime)
    TextView idSoreTime;
    @BindView(R.id.id_SoreHege)
    TextView idSoreHege;
    @BindView(R.id.id_ErrorImg)
    ImageView idErrorImg;
    @BindView(R.id.id_errorLayout)
    RelativeLayout idErrorLayout;
    @BindView(R.id.id_MockImg)
    ImageView idMockImg;
    @BindView(R.id.id_mockLayout)
    RelativeLayout idMockLayout;
    @BindView(R.id.id_SjImg)
    ImageView idSjImg;
    @BindView(R.id.id_shuijiLayout)
    RelativeLayout idShuijiLayout;

    private Context context;
    private int Type = 1; // 科目类型
    private int Time = 0; // 答题花费时间
    private int yesNum = 0; // 答对的题目数
    private String Uid = "";


    @Override
    protected int setLayoutId() {
        return R.layout.score_layout;
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
        Type = getIntent().getIntExtra("Type", 1);
        Time = getIntent().getIntExtra("Time", 0);
        yesNum = getIntent().getIntExtra("yesNum", 0);
        Uid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_ID);

        if (Time / 60 < 10) { // XX:XX
            if (Time % 60 < 10) { // XX:0X
                idSoreTime.setText("0" + Time / 60 + ":" + "0" + Time % 60);
            } else {
                idSoreTime.setText("0" + Time / 60 + ":" + Time % 60);
            }
        } else {
            if (Time % 60 < 10) { // XX:0X
                idSoreTime.setText(Time / 60 + ":" + "0" + Time % 60);
            } else {
                idSoreTime.setText(Time / 60 + ":" + Time % 60);
            }
        }

        if (Type == 1) {
            idSoreNum.setText(yesNum + "");

            if (yesNum > 89) {
                idSoreHege.setText("已合格");
            } else {
                idSoreHege.setText("未合格");
            }

            Resquest.addMnksRanking(handler, Time + "", 1, yesNum, Type, Uid);
            Resquest.addMnksRanking(handler, Time + "", 2, yesNum, Type, Uid);
        }
        if (Type == 4) {
            idSoreNum.setText(yesNum * 2 + "");

            if (yesNum * 2 > 89) {
                idSoreHege.setText("已合格");
            } else {
                idSoreHege.setText("未合格");
            }
            Resquest.addMnksRanking(handler, Time + "", 1, yesNum * 2, Type, Uid);
            Resquest.addMnksRanking(handler, Time + "", 2, yesNum * 2, Type, Uid);
        }


    }

    @OnClick({R.id.item_Scoreback, R.id.id_errorLayout, R.id.id_mockLayout, R.id.id_shuijiLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_Scoreback:
                finish();
                break;
            case R.id.id_errorLayout:
                Intent intent2 = new Intent(context, ProblemMain_Activity.class);
                intent2.putExtra("ChapterID", "");
                intent2.putExtra("RecType", 98);
                intent2.putExtra("T_Subject", "");
                intent2.putExtra("ZJ_Count", 10000);
                intent2.putExtra("Type", Type);
                startActivity(intent2);
                finish();

                break;
            case R.id.id_mockLayout:

                Intent intent3 = new Intent(context, MockExam_Activity.class);
                intent3.putExtra("Type", Type);
                if (Type == 1) {
                    intent3.putExtra("ZJ_Count", 100);
                }
                if (Type == 4) {
                    intent3.putExtra("ZJ_Count", 50);
                }
                startActivity(intent3);

                finish();

                break;
            case R.id.id_shuijiLayout:

                Intent intent4 = new Intent(context, ProblemMain_Activity.class);
                intent4.putExtra("ChapterID", "");
                intent4.putExtra("RecType", 2);
                intent4.putExtra("Number", "");
                intent4.putExtra("T_Subject", "");
                intent4.putExtra("ZJ_Count", 10000);
                intent4.putExtra("Type", Type);
                startActivity(intent4);

                finish();

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

                            ToastUtil.show(context, "提交成功");
                        }


                    } catch (JSONException e) {
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

}
