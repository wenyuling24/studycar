package com.kunzhuo.xuechelang.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.TitleList_KM_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.LogUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.eventutils.BooleanEvent;
import com.kunzhuo.xuechelang.utils.eventutils.FirstEvent;
import com.kunzhuo.xuechelang.view.fragment.Problem_Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by waaa on 2016/9/2.
 */
public class ProblemMain_Activity extends BaseActivity {

    private static final String TAG = "ProblemMain_Activity";

    @BindView(R.id.item_qusback)
    ImageView itemBack;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_questiontitle)
    RelativeLayout idQuestiontitle;
    @BindView(R.id.id_quscollectImg)
    ImageView idQuscollectImg;
    @BindView(R.id.id_quecollectTxt)
    TextView idQuecollectTxt;
    @BindView(R.id.id_quscollectlayout)
    RelativeLayout idQuscollectlayout;
    @BindView(R.id.id_menuTxt)
    TextView idMenuTxt;
    @BindView(R.id.id_menuPosition)
    TextView idMenuPosition;
    @BindView(R.id.id_menuImg)
    ImageView idMenuImg;
    @BindView(R.id.id_menulayout)
    RelativeLayout idMenulayout;
    @BindView(R.id.id_NoTxt)
    TextView idNoTxt;
    @BindView(R.id.id_Nolayout)
    RelativeLayout idNolayout;
    @BindView(R.id.id_YesTxt)
    TextView idYesTxt;
    @BindView(R.id.id_Yeslayout)
    RelativeLayout idYeslayout;
    @BindView(R.id.id_questionbottom)
    RelativeLayout idQuestionbottom;
    @BindView(R.id.id_answerBtn)
    TextView idAnswerBtn;
    @BindView(R.id.id_seeanswerBtn)
    TextView idSeeanswerBtn;
    @BindView(R.id.id_answerBtnLayout)
    LinearLayout idAnswerBtnLayout;
    @BindView(R.id.shadowView)
    ImageView shadowView;

    private Context context;
    private int Type;
    private int ZJ_Count;
    private String T_Subject = "";
    private String Uid = "";
    private String ChapterID = "";
    private int RecType = 1;
    private int Number = 0; // 已做题数
    private int Posi = 1;
    private TitleList_KM_Bean title_Bean;
    private boolean isClick = false;
    private static int preSelectedPage = 0;
    private static int scrollState;
    private int position = 0;

    @Override
    protected int setLayoutId() {
        return R.layout.problemain_layout;
    }

    @Override
    protected void setDefaultViews() {
        initViews();
    }

    @Override
    protected void requestData() {

        EventBus.getDefault().register(this);

    }

    @Override
    protected void updateViews() {

    }

    private void initViews() {
        context = this;
        setModelShared(true);
        Type = getIntent().getIntExtra("Type", 1); // 类型
        T_Subject = getIntent().getStringExtra("T_Subject"); //ID
        ZJ_Count = getIntent().getIntExtra("ZJ_Count", 0); //总题目数

        ChapterID = getIntent().getStringExtra("ChapterID");
        RecType = getIntent().getIntExtra("RecType", 1);
        Number = getIntent().getIntExtra("Number", 0);

        Uid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_ID);

//        ToastUtil.show(context, Number + " 已做题数传过来的");

        if (ZJ_Count != 10000) {
            idMenuTxt.setText("/" + ZJ_Count);
        } else {
            idMenuTxt.setText("");
        }

        viewpager.setAdapter(new MyFragmentPagerAdapter(
                getSupportFragmentManager()));

        viewpager.setCurrentItem(Number);

        setNumCenter(Number + 1);

        viewpager.setOnPageChangeListener(new OnPageChangeListener() {

            boolean flag = false;

            @Override
            public void onPageSelected(int arg0) {
                position = arg0;
                setNumCenter(arg0 + 1);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

                shadowView.setTranslationX(viewpager.getWidth() - arg2);

                if (arg2 != 0) {
                    if (scrollState == 1) {//手指按下
                        if (preSelectedPage == arg0) {//表示往左拉，相应的tab往右走
//                            LogUtils.i(TAG, "ux==--> 手指左滑 整体页面--> ");
                            if (!flag) {
                                Number++;
                                flag = true;
                            }

//                            ToastUtil.show(context, Number + " 已做题数翻页向后");

                            LogUtils.i(TAG, Number + " 1 " + flag);
                        } else {
//                            LogUtils.i(TAG, "ux==--> 手指向右 整体页面<--");
                            if (!flag) {
                                Number--;
                                flag = true;
                            }

//                            ToastUtil.show(context, Number + " 已做题数翻页向前");

                            LogUtils.i(TAG, Number + " 2 " + flag);
                        }
                    } else if (scrollState == 2) {
                        if (preSelectedPage == arg0) {//往左拉
                            LogUtils.i(TAG, "ux==--> 手指左滑 整体页面--> 页面向右");


                        } else {//表示往右拉
                            LogUtils.i(TAG, "ux==--> 手指右滑 整体页面-->  页面向左");

                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                if (!isClick) {
                    scrollState = arg0;
                    preSelectedPage = position;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

//        ToastUtil.show(context, Type + "类型1");

        if (Posi == 1) {
            if (RecType == 98) { // 错题
                Resquest.getTitleError_List(handler, Type, Uid, Posi, 1);

            }
            if (RecType == 99) { // 收藏列表

                Resquest.getColnTopicSel(handler, Uid, Type, Posi, 1);

            } else {
                Resquest.getTitleList_KM(handler, Type, T_Subject, Posi, 1, Uid);
            }
        }

    }

    /**
     * 题目数
     *
     * @param position
     */
    private void setNumCenter(int position) {

        idMenuPosition.setText(position + "");
        Number = position;
        Posi = position;

        if (RecType == 98) { // 错题
            Resquest.getTitleError_List(handler, Type, Uid, position, 1);

        }
        if (RecType == 99) { // 收藏列表

            Resquest.getColnTopicSel(handler, Uid, Type, position, 1);

        } else {
            Resquest.getTitleList_KM(handler, Type, T_Subject, position, 1, Uid);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Subscribe
    public void onEventMainThread(FirstEvent event) {

        int msg = event.getMsg();
        System.out.println(msg + " 传过来的页数");
//        ToastUtil.show(context, msg + " 传过来的页数");
        Message message = new Message();
        message.what = 1;
        message.obj = msg;

        handler5.sendMessage(message);

//        viewpager.setCurrentItem(msg);

//        setNumCenter(msg);

    }

    /**
     * 记录已经做得题目
     */
    private void setQusRecord() {

        if (!Uid.equals("")) {
            if (RecType == 99 || RecType == 98) {
                finish();
            } else {
                Resquest.setRecord(handler4, ChapterID, RecType, Number, Type, Uid);
            }

        } else {
            finish();
        }
    }

    @OnClick({R.id.item_qusback, R.id.id_quscollectlayout, R.id.id_menulayout,
            R.id.id_Nolayout, R.id.id_Yeslayout, R.id.id_answerBtn, R.id.id_seeanswerBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_qusback: // 返回上级
                setQusRecord();
                break;
            case R.id.id_quscollectlayout: // 收藏

                if (!title_Bean.getCID().equals("")) { // 证明是已经收藏

                    Resquest.delColnTopicDel(handler3, title_Bean.getCID(), Uid);

                } else { // 还未收藏
                    Resquest.colnColnTopicAdd(handler2, title_Bean.getID(), Uid, Type);

                }

                break;
            case R.id.id_menulayout: // 查看全部
                break;
            case R.id.id_Nolayout: // 错误数量
                break;
            case R.id.id_Yeslayout: // 正确数量
                break;
            case R.id.id_answerBtn: // 答题模式
                idAnswerBtn.setTextColor(Color.rgb(29, 172, 250));
                idAnswerBtn.setBackgroundColor(Color.rgb(255, 255, 255));

                idSeeanswerBtn.setTextColor(Color.rgb(255, 255, 255));
                idSeeanswerBtn.setBackgroundResource(R.drawable.btn_bluestn);

                EventBus.getDefault().post(new BooleanEvent(true));
                setModelShared(true);

//                EventBus.getDefault().post(new BooleanEvent(true));

//                idSeeanswerBtn.setBackgroundColor(Color.rgb(29, 172, 250));
//                idAnswerBtnLayout.setBackgroundResource(R.drawable.btn_bluestn);
                break;
            case R.id.id_seeanswerBtn: // 看答案模式 背题模式

                idSeeanswerBtn.setTextColor(Color.rgb(29, 172, 250));
                idSeeanswerBtn.setBackgroundColor(Color.rgb(255, 255, 255));

                idAnswerBtn.setTextColor(Color.rgb(255, 255, 255));
                idAnswerBtn.setBackgroundResource(R.drawable.btn_bluestn);

//                idAnswerBtn.setBackgroundColor(Color.rgb(29, 172, 250));
//                idAnswerBtnLayout.setBackgroundResource(R.drawable.btn_bluestn);

                EventBus.getDefault().post(new BooleanEvent(false));
                setModelShared(false);

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setQusRecord();
        return false;
    }

    public void setModelShared(boolean flag) {
        SharedPreferences shared = getSharedPreferences(DefaultUtils.QUSMODEL, MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor edit = shared.edit();
        edit.putBoolean(DefaultUtils.QUSMODELFLAG, flag);
        edit.commit();
    }


    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm); // 这句代码必须实现

        }

        @Override
        public Fragment getItem(int position) {

            Problem_Fragment fragment = new Problem_Fragment();
            Bundle bundle = new Bundle();
            bundle.putInt("pageNum", position + 1);
            bundle.putInt("Type", Type);
            bundle.putString("T_Subject", T_Subject);
            bundle.putInt("RecType", RecType);
            fragment.setArguments(bundle);
            return fragment;
        }


        @Override
        public int getCount() {
            return ZJ_Count;
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

                        if (!title_Bean.getCID().equals("")) {
                            idQuscollectImg.setImageResource(R.drawable.icon_statyellow);
                            idQuecollectTxt.setText("已收藏");
                        } else {
                            idQuscollectImg.setImageResource(R.drawable.icon_stargrey);
                            idQuecollectTxt.setText("未收藏");
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

    Handler handler2 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String code = jsonObject.getString("Code");
                        if (code.equals("0")) {
                            idQuscollectImg.setImageResource(R.drawable.icon_statyellow);
                            idQuecollectTxt.setText("已收藏");
                            title_Bean.setCID(jsonObject.getString("ID"));
                        }
                        if (code.equals("99")) {
                            ToastUtil.show(context, "您还未登录,请先返回登录");
                        }
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

    Handler handler3 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String code = jsonObject.getString("Code");
                        if (code.equals("0")) {
                            idQuscollectImg.setImageResource(R.drawable.icon_stargrey);
                            idQuecollectTxt.setText("未收藏");
                            title_Bean.setCID("");
                        }
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

    Handler handler4 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String code = jsonObject.getString("Code");
                        if (code.equals("0")) {
                            finish();
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


    Handler handler5 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                viewpager.setCurrentItem((int) msg.obj);
            }

        }
    };


}
