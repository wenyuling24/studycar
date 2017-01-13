package com.kunzhuo.xuechelang.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.Answer_Bean;
import com.kunzhuo.xuechelang.model.TitleList_KM_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.LogUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.eventutils.BooleanEvent;
import com.kunzhuo.xuechelang.utils.eventutils.FirstEvent;
import com.kunzhuo.xuechelang.video.VideoViewActivity;
import com.kunzhuo.xuechelang.view.adapter.Result_Adapter;
import com.kunzhuo.xuechelang.widget.MyListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by waaa on 2016/9/2.
 */
public class Problem_Fragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    @BindView(R.id.id_problemType)
    TextView idProblemType;
    @BindView(R.id.id_problemTextMsg)
    TextView idProblemTextMsg;
    @BindView(R.id.id_problemImgMsg)
    ImageView idProblemImgMsg;
    @BindView(R.id.id_problemMsglayout)
    LinearLayout idProblemMsglayout;
    @BindView(R.id.problem_listview)
    MyListView problemListview;
    @BindView(R.id.before_Qestion)
    TextView beforeQestion;
    @BindView(R.id.after_Qestion)
    TextView afterQestion;
    @BindView(R.id.multiselect_btn)
    Button multiselectBtn;
    @BindView(R.id.sc)
    LinearLayout sc;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.surface_view)
    VideoView surfaceView;
    @BindView(R.id.id_adTxt)
    TextView idAdTxt;
    @BindView(R.id.id_Value)
    TextView idValue;
    @BindView(R.id.id_ValueLayout)
    LinearLayout idValueLayout;
    @BindView(R.id.id_Coption)
    TextView idCoption;

    private Result_Adapter adapter;
    private int pageNum; // 页数
    private int Type = 1; // 类型
    private String T_Subject; //科目ID
    private int RecType;
    private List<String> list = new ArrayList<>();
    private boolean[] flag = {false, false, false, false}; // 四个选项
    private boolean flagisEn = false;
    private ProgressDialog dialog;
    private boolean flagModel = true;
    private String Uid = "";
    private TitleList_KM_Bean title_Bean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(getActivity()))
            return;
        EventBus.getDefault().register(this);

//        Bundle bundle = getArguments();
//        pageNum = bundle.getInt("pageNum");
//        Type = bundle.getInt("Type");
//        T_Subject = bundle.getString("T_Subject");
//        RecType = bundle.getInt("RecType");
//        ToastUtil.show(getActivity(), RecType + "  类型标志RecType");

    }

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

    @Override
    protected void upDetoryViews() {

    }

    private void initViews() {

        adapter = new Result_Adapter(getActivity(), null);

        problemListview.setAdapter(adapter);
//        problemListview.setOnItemClickListener(this);
        System.out.println(Type + " Type" + T_Subject + " T_Subject" + pageNum + "  pageNum 项");

        Uid = DefaultUtils.getShared(getActivity(), DefaultUtils.USER, DefaultUtils.USER_ID);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在加载, 请稍后...");
    }


    @Override
    public void onResume() {
        super.onResume();
        problemListview.setEnabled(true);
//        dialog.show();
        setModelShared();

        Bundle bundle = getArguments();
        pageNum = bundle.getInt("pageNum");
        Type = bundle.getInt("Type");
        T_Subject = bundle.getString("T_Subject");
        RecType = bundle.getInt("RecType");
//        ToastUtil.show(getActivity(), RecType + "  类型标志RecType");

//        ToastUtil.show(getActivity(), Type + "类型2");

        if (RecType == 98) { // 错题
            Resquest.getTitleError_List(handler, Type, Uid, pageNum, 1);

        } else if (RecType == 99) { // 收藏列表

            Resquest.getColnTopicSel(handler, Uid, Type, pageNum, 1);

        } else {
//            ToastUtil.show(getActivity(), " getTitleList_KM");
            Resquest.getTitleList_KM(handler, Type, T_Subject, pageNum, 1, Uid);
        }

    }

    public void setModelShared() {
        SharedPreferences shared = getActivity().getSharedPreferences(DefaultUtils.QUSMODEL, getActivity().MODE_WORLD_WRITEABLE);
        flagModel = shared.getBoolean(DefaultUtils.QUSMODELFLAG, true);

//        ToastUtil.show(getActivity(), flagModel + " 是否是做题模式");
    }

    @Subscribe
    public void onEventMainThread(BooleanEvent event) {

        boolean msg = event.getMsg();
        flagModel = msg;

        if (flagModel) {
            // 做题模式
            problemListview.setOnItemClickListener(this);
            multiselectBtn.setOnClickListener(this);
            setAnswerLayout(flagModel);
            doWorkModel();

        } else if (!flagModel) {
            // 看答案模式
            setAnswerLayout(flagModel);
            setQusSeeAnswer(title_Bean);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        list.clear();

        if (surfaceView != null) {
            surfaceView.stopPlayback();
            surfaceView = null;
        }

        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @OnClick({R.id.before_Qestion, R.id.after_Qestion})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.before_Qestion:
                // 上一题
                break;
            case R.id.after_Qestion:
                // 下一题
                break;
            case R.id.multiselect_btn:

                // 多选时的确认按钮
                String c_Value = title_Bean.getC_option(); // 正确答案
                String c_ChooseValue = list.toString().substring(1, list.toString().length() - 1).replace(",", "").replace(" ", "").trim();


                if (c_ChooseValue.equals(c_Value)) { // 正确

                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = title_Bean;
                    handler3.sendMessage(msg);

                    EventBus.getDefault().post(
                            new FirstEvent(title_Bean.getPageNum()));

                    if (RecType == 98) {

                        Resquest.delTitleError_Del(handler4, Uid, title_Bean.getCID());

                    }


                } else { // 错误

                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = title_Bean;

                    handler3.sendMessage(msg);
                }

                break;


        }
    }


    Handler handler3 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1: // 通知答案刷新吧
                    ToastUtil.show(getActivity(), "正确");

                    TitleList_KM_Bean title_Bean = (TitleList_KM_Bean) msg.obj;

                    String str = title_Bean.getC_option(); // ABC

                    for (int j = 0; j < adapter.getCount(); j++) {

                        Answer_Bean tempBean = (Answer_Bean) problemListview.getItemAtPosition(j);

                        for (int i = 0; i < str.length(); i++) {

                            String option = str.substring(i, i + 1);

                            if (tempBean.getA_option().equals(option)) {

                                View tempView = problemListview.getChildAt(j);

                                ImageView temp_idJudgeflag = (ImageView) tempView.findViewById(R.id.id_judgeflag);
                                TextView temp_idResultChoose = (TextView) tempView.findViewById(R.id.id_resultChoose);
                                TextView temp_idResultMsg = (TextView) tempView.findViewById(R.id.id_resultMsg);

                                temp_idJudgeflag.setVisibility(View.VISIBLE);
                                temp_idJudgeflag.setImageResource(R.drawable.pass);
                                temp_idResultChoose.setVisibility(View.INVISIBLE);
                                temp_idResultMsg.setTextColor(Color.rgb(42, 196, 22));
                            }
                        }
                    }

                    problemListview.setEnabled(false);
                    multiselectBtn.setClickable(false);
                    flagisEn = true;

                    break;
                case 0: // 什么都不做

                    ToastUtil.show(getActivity(), "  错误");

                    TitleList_KM_Bean title_Bean2 = (TitleList_KM_Bean) msg.obj;

                    String str2 = title_Bean2.getC_option(); // ABC

                    for (int j = 0; j < adapter.getCount(); j++) {

                        Answer_Bean tempBean = (Answer_Bean) problemListview.getItemAtPosition(j);

                        for (int i = 0; i < str2.length(); i++) {

                            String option = str2.substring(i, i + 1);

                            if (tempBean.getA_option().equals(option)) {

                                View tempView = problemListview.getChildAt(j);
                                ImageView temp_idJudgeflag = (ImageView) tempView.findViewById(R.id.id_judgeflag);
                                TextView temp_idResultChoose = (TextView) tempView.findViewById(R.id.id_resultChoose);
                                TextView temp_idResultMsg = (TextView) tempView.findViewById(R.id.id_resultMsg);

                                temp_idJudgeflag.setVisibility(View.VISIBLE);
                                temp_idJudgeflag.setImageResource(R.drawable.pass);
                                temp_idResultChoose.setVisibility(View.INVISIBLE);
                                temp_idResultMsg.setTextColor(Color.rgb(42, 196, 22));
                            }
                        }
                    }

                    problemListview.setEnabled(false);
                    multiselectBtn.setClickable(false);
                    flagisEn = true;
                    setAnswerLayout(false);

                    break;
            }

            return true;
        }
    });

    /**
     * 一个空字符List
     *
     * @param list
     * @return
     */
    public List<String> setStrList(List<String> list) { // 放置4个空字符串
        list.add(0, "");
        list.add(1, "");
        list.add(2, "");
        list.add(3, "");
        list.add(4, "");
        return list;
    }

    /**
     * 答案布局
     *
     * @param flag
     */
    public void setAnswerLayout(boolean flag) {

        if (!flag) {
            idValueLayout.setVisibility(View.VISIBLE);
        } else if (flag) {
            idValueLayout.setVisibility(View.GONE);
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Answer_Bean answer_bean = (Answer_Bean) parent.getItemAtPosition(position);
        String T_option = title_Bean.getT_option(); // 获取类型值
        String C_option = title_Bean.getC_option(); // 正确答案
//        ToastUtil.show(getActivity(), C_option + " 正确答案11");
        String C_value = title_Bean.getC_value();  // 答案解释

        ImageView idJudgeflag = (ImageView) view.findViewById(R.id.id_judgeflag);
        TextView idResultChoose = (TextView) view.findViewById(R.id.id_resultChoose);
        TextView idResultMsg = (TextView) view.findViewById(R.id.id_resultMsg);
        RelativeLayout id_resultlay = (RelativeLayout) view.findViewById(R.id.id_resultlay);

        if (T_option.equals("单选")) {
            String A_option = answer_bean.getA_option();
            id_resultlay.setBackgroundColor(Color.rgb(246, 246, 246)); // 背景变灰色
            if (A_option.equals(C_option)) { // 正确答案
                idJudgeflag.setVisibility(View.VISIBLE);
                idJudgeflag.setImageResource(R.drawable.pass);
                idResultChoose.setVisibility(View.INVISIBLE);
                idResultMsg.setTextColor(Color.rgb(42, 196, 22));
                problemListview.setEnabled(false);
                EventBus.getDefault().post(
                        new FirstEvent(title_Bean.getPageNum()));

                if (RecType == 98) {
                    Resquest.delTitleError_Del(handler4, Uid, title_Bean.getCID());
                }

            } else {
                problemListview.setEnabled(false);
                setAnswerLayout(false);
                idJudgeflag.setVisibility(View.VISIBLE);
                idJudgeflag.setImageResource(R.drawable.no_pass);
                idResultChoose.setVisibility(View.INVISIBLE);
                idResultMsg.setTextColor(Color.rgb(228, 72, 74));

                for (int i = 0; i < adapter.getCount(); i++) {

                    Answer_Bean tempBean = (Answer_Bean) parent.getItemAtPosition(i);

                    if (tempBean.getA_option().equals(C_option)) {

                        View tempView = parent.getChildAt(i);
                        ImageView temp_idJudgeflag = (ImageView) tempView.findViewById(R.id.id_judgeflag);
                        TextView temp_idResultChoose = (TextView) tempView.findViewById(R.id.id_resultChoose);
                        TextView temp_idResultMsg = (TextView) tempView.findViewById(R.id.id_resultMsg);

                        temp_idJudgeflag.setVisibility(View.VISIBLE);
                        temp_idJudgeflag.setImageResource(R.drawable.pass);
                        temp_idResultChoose.setVisibility(View.INVISIBLE);
                        temp_idResultMsg.setTextColor(Color.rgb(42, 196, 22));
                    }
                }
            }
        } else if (T_option.equals("判断"))

        {
            id_resultlay.setBackgroundColor(Color.rgb(246, 246, 246)); // 背景变灰色
            String A_value = answer_bean.getA_value();
            String exC_option = null;

            if (C_option.equals("N")) {
                exC_option = "错误";
            } else if (C_option.equals("Y")) {
                exC_option = "正确";
            }
            if (A_value.equals(exC_option)) { // 正确

                idJudgeflag.setVisibility(View.VISIBLE);
                idJudgeflag.setImageResource(R.drawable.pass);
                idResultChoose.setVisibility(View.INVISIBLE);
                idResultMsg.setTextColor(Color.rgb(42, 196, 22));
                problemListview.setEnabled(false);

                EventBus.getDefault().post(
                        new FirstEvent(title_Bean.getPageNum()));

                if (RecType == 98) {

                    Resquest.delTitleError_Del(handler4, Uid, title_Bean.getCID());

                }

            } else { // 错误

                problemListview.setEnabled(false);
                setAnswerLayout(false);

                idJudgeflag.setVisibility(View.VISIBLE);
                idJudgeflag.setImageResource(R.drawable.no_pass);
                idResultChoose.setVisibility(View.INVISIBLE);
                idResultMsg.setTextColor(Color.rgb(228, 72, 74));

                for (int i = 0; i < adapter.getCount(); i++) {

                    Answer_Bean tempBean = (Answer_Bean) parent.getItemAtPosition(i);

                    if (tempBean.getA_value().equals(exC_option)) {

                        View tempView = parent.getChildAt(i);

                        ImageView temp_idJudgeflag = (ImageView) tempView.findViewById(R.id.id_judgeflag);
                        TextView temp_idResultChoose = (TextView) tempView.findViewById(R.id.id_resultChoose);
                        TextView temp_idResultMsg = (TextView) tempView.findViewById(R.id.id_resultMsg);

                        temp_idJudgeflag.setVisibility(View.VISIBLE);
                        temp_idJudgeflag.setImageResource(R.drawable.pass);
                        temp_idResultChoose.setVisibility(View.INVISIBLE);
                        temp_idResultMsg.setTextColor(Color.rgb(42, 196, 22));
                    }
                }
            }

        } else if (T_option.equals("多选"))

        { //ACD
            boolean flagposition = flag[position];

            if (!flagisEn) {
                if (!flagposition) {
                    id_resultlay.setBackgroundColor(Color.rgb(170, 170, 170)); // 背景变白色
                    idResultMsg.setTextColor(Color.rgb(255, 255, 255));

                    // 获取多选答案 C_option 把点击获取的所有答案存起来 然后根据获取的下标排序 再和正确答案比较
                    int sort = answer_bean.getA_sort(); // 排序值
                    String str = answer_bean.getA_option(); // 答案
                    list.remove(sort);
                    list.add(sort, str);
                    flag[position] = true;

                } else if (flagposition) {

                    id_resultlay.setBackgroundColor(Color.rgb(246, 246, 246)); // 背景变灰色
                    idResultMsg.setTextColor(Color.rgb(51, 51, 51));
                    // 获取多选答案 C_option 把点击获取的所有答案存起来 然后根据获取的下标排序 再和正确答案比较
                    int sort = answer_bean.getA_sort(); // 排序值
                    String str = answer_bean.getA_option(); // 答案
                    list.remove(sort);
                    list.add(sort, "");
                    flag[position] = false;

                }
            }
        }
    }


    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        java.lang.reflect.Type type = new TypeToken<List<TitleList_KM_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<TitleList_KM_Bean> mList = mapper.transformCollection(jsonObject, "List");

                        title_Bean = mList.get(0);
                        String ID = mList.get(0).getID();
                        Resquest.getTitleAnswerList(handler2, ID);

                        setMsgOnLayout(mList.get(0));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(getActivity(), "网络异常");
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

                        adapter.setData(mList);
                        adapter.notifyDataSetChanged();

//                        dialog.cancel();

                        if (!flagModel) {
                            // 看答案模式
                            setAnswerLayout(flagModel);
                            setQusSeeAnswer(title_Bean);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(getActivity(), "网络异常");
                    break;
            }

            return true;
        }
    });

    Handler handler4 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

//                    ToastUtil.show(getActivity(), jsonObject.toString() + "回答正确");

                    try {
                        String code = jsonObject.getString("Code");
                        if (code.equals("0")) {
//                            ToastUtil.show(getActivity(), "回答正确");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(getActivity(), "网络异常");
                    break;
            }

            return true;
        }
    });


    /**
     * 把获取的内容放置上去
     *
     * @param bean
     */
    private void setMsgOnLayout(final TitleList_KM_Bean bean) {

        // 设置单选多选
        idProblemType.setText(bean.getT_option());
        // 设置文字内容
        idProblemTextMsg.setText(bean.getT_title());

        int T_Tagging = bean.getT_Tagging();
        if (T_Tagging == 0) { // 无后续内容
            idProblemMsglayout.setVisibility(View.GONE);
        }
        if (T_Tagging == 1) { //1:  则表示视频

            final String srcUrl = "http://123.57.41.216:4521" + bean.getT_src();

            idProblemMsglayout.setVisibility(View.VISIBLE);
            idProblemImgMsg.setVisibility(View.GONE);
            surfaceView.setVisibility(View.VISIBLE);
            surfaceView.setVideoPath(srcUrl);

            MediaController mc = new MediaController(getActivity());
            mc.setVisibility(View.GONE);
            surfaceView.setMediaController(mc);
            surfaceView.setVideoLayout(VideoView.VIDEO_LAYOUT_ORIGIN, 0);
            surfaceView.getHolder().setFixedSize(360, 240);  //设置分辨率
            surfaceView.requestFocus();

            surfaceView
                    .setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.setPlaybackSpeed(1.0f);

                            int height = mediaPlayer.getVideoHeight();
                            int width = mediaPlayer.getVideoWidth();
                            float hwidth = mediaPlayer.getVideoAspectRatio();

                        }
                    });

            surfaceView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    surfaceView.setVideoPath(srcUrl);

                }
            });

            surfaceView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
//                LogUtils.i(LogUtils.LOG_TAG, "what=" + what);
                    return false;
                }
            });
            surfaceView.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {

                }
            });
            surfaceView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    LogUtils.i(LogUtils.LOG_TAG, "percent" + percent);
                }
            });


            surfaceView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH); // 高画质

            // VIDEOQUALITY_LOW
            // 流畅
            // VIDEOQUALITY_MEDIUM
            // 普通
            // VIDEOQUALITY_HIGH   // 高画质

            surfaceView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

//                    Intent appIntent = new Intent(getActivity(), VideoViewActivity.class);
//                    appIntent.putExtra(VideoViewActivity.VIDEO_PATH, srcUrl);
//                    startActivity(appIntent);

                    return false;
                }
            });


        }
        if (T_Tagging == 2) { // 图片
            String srcUrl = "http://123.57.41.216:4521" + bean.getT_src();
            idProblemMsglayout.setVisibility(View.VISIBLE);
            idProblemImgMsg.setVisibility(View.VISIBLE);
            surfaceView.setVisibility(View.GONE);
//            ToastUtil.show(getActivity(), srcUrl + " 图片地址");
            ImageLoader.getInstance().displayImage(srcUrl, idProblemImgMsg);
        }
        if (bean.getT_option().equals("多选")) {
            multiselectBtn.setVisibility(View.VISIBLE);
            setStrList(list);

        } else {
            multiselectBtn.setVisibility(View.GONE);
        }

        if (bean.getT_option().equals("判断")) {

            String C_option = bean.getC_option();

            if (C_option.equals("N")) {
                idCoption.setText("错误");
            } else if (C_option.equals("Y")) {
                idCoption.setText("正确");
            }
        } else
            idCoption.setText(bean.getC_option());

        idValue.setText(bean.getC_value().replace("\\n", "\n"));


        if (flagModel) {
            // 做题模式
            setAnswerLayout(flagModel);
            doWorkModel();
            problemListview.setOnItemClickListener(this);
            multiselectBtn.setOnClickListener(this);

        }
    }


    // 背题模式 直接看答案
    public void setQusSeeAnswer(TitleList_KM_Bean bean) {

        String T_option = bean.getT_option(); // 获取类型值
        String C_option = bean.getC_option(); // 正确答案

        for (int j = 0; j < adapter.getCount(); j++) {

            Answer_Bean answer_bean = (Answer_Bean) problemListview.getItemAtPosition(j);

            View tempView = problemListview.getChildAt(j);

            ImageView idJudgeflag = (ImageView) tempView.findViewById(R.id.id_judgeflag);
            TextView idResultChoose = (TextView) tempView.findViewById(R.id.id_resultChoose);
            TextView idResultMsg = (TextView) tempView.findViewById(R.id.id_resultMsg);
            RelativeLayout id_resultlay = (RelativeLayout) tempView.findViewById(R.id.id_resultlay);


            if (T_option.equals("单选")) {
                String A_option = answer_bean.getA_option();
                if (A_option.equals(C_option)) { // 正确答案
                    idJudgeflag.setVisibility(View.VISIBLE);
                    idJudgeflag.setImageResource(R.drawable.pass);
                    idResultChoose.setVisibility(View.INVISIBLE);
                    idResultMsg.setTextColor(Color.rgb(42, 196, 22));
                    problemListview.setEnabled(false);

                }

            } else if (T_option.equals("判断"))

            {
                String A_value = answer_bean.getA_value();
                String exC_option = null;

                if (C_option.equals("N")) {
                    exC_option = "错误";
                } else if (C_option.equals("Y")) {
                    exC_option = "正确";
                }
                if (A_value.equals(exC_option)) { // 正确

                    idJudgeflag.setVisibility(View.VISIBLE);
                    idJudgeflag.setImageResource(R.drawable.pass);
                    idResultChoose.setVisibility(View.INVISIBLE);
                    idResultMsg.setTextColor(Color.rgb(42, 196, 22));
                    problemListview.setEnabled(false);

                }
            } else if (T_option.equals("多选")) {

                String str = C_option; // ABC

                for (int i = 0; i < str.length(); i++) {

                    String option = str.substring(i, i + 1);

                    if (answer_bean.getA_option().equals(option)) {

                        idJudgeflag.setVisibility(View.VISIBLE);
                        idJudgeflag.setImageResource(R.drawable.pass);
                        idResultChoose.setVisibility(View.INVISIBLE);
                        idResultMsg.setTextColor(Color.rgb(42, 196, 22));
                    }
                }
            }

            problemListview.setEnabled(false);
            multiselectBtn.setClickable(false);
            flagisEn = true;
        }
    }

    public void doWorkModel() {

        problemListview.setEnabled(true);

        for (int j = 0; j < adapter.getCount(); j++) {

            View tempView = problemListview.getChildAt(j);

            ImageView idJudgeflag = (ImageView) tempView.findViewById(R.id.id_judgeflag);
            TextView idResultChoose = (TextView) tempView.findViewById(R.id.id_resultChoose);
            TextView idResultMsg = (TextView) tempView.findViewById(R.id.id_resultMsg);

            idJudgeflag.setVisibility(View.GONE);
            idResultChoose.setVisibility(View.VISIBLE);
            idResultMsg.setTextColor(Color.rgb(51, 51, 51));
        }

    }

}
