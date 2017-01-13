package com.kunzhuo.xuechelang.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.kunzhuo.xuechelang.utils.eventutils.RoardAnswer_Event;
import com.kunzhuo.xuechelang.view.fragment.Mock_Fragment;
import com.kunzhuo.xuechelang.widget.ReaderViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by waaa on 2016/9/13.
 * 模拟考试
 */
public class MockExam_Activity extends BaseActivity {

    @BindView(R.id.item_Mockback)
    ImageView itemMockback;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_timeView)
    TextView idTimeView;
    @BindView(R.id.id_answerBtnLayout)
    LinearLayout idAnswerBtnLayout;
    @BindView(R.id.id_Mocktitle)
    RelativeLayout idMocktitle;
    @BindView(R.id.id_uppaperImg)
    ImageView idUppaperImg;
    @BindView(R.id.id_uppaperTxt)
    TextView idUppaperTxt;
    @BindView(R.id.id_uppaperlayout)
    RelativeLayout idUppaperlayout;
    @BindView(R.id.id_menuMockTxt)
    TextView idMenuMockTxt;
    @BindView(R.id.id_menuMockPosition)
    TextView idMenuMockPosition;
    @BindView(R.id.id_menuMockImg)
    ImageView idMenuMockImg;
    @BindView(R.id.id_menuMocklayout)
    RelativeLayout idMenuMocklayout;
    @BindView(R.id.id_NoMockTxt)
    TextView idNoMockTxt;
    @BindView(R.id.id_NoMocklayout)
    RelativeLayout idNoMocklayout;
    @BindView(R.id.id_YesMockTxt)
    TextView idYesMockTxt;
    @BindView(R.id.id_YesMocklayout)
    RelativeLayout idYesMocklayout;
    @BindView(R.id.id_uppaperbottom)
    RelativeLayout idUppaperbottom;
    @BindView(R.id.Mock_viewpager)
    ReaderViewPager MockViewpager;
    @BindView(R.id.shadowView)
    ImageView shadowView;

    private static int minute = -1;
    private static int second = -1;
    private final static String tag = "MockExam_Activity.class";
    private Timer timer;
    private TimerTask timerTask;
    private Context context;
    private int Type = 1;
    private int ZJ_Count;
    private String Uid = "";
    private boolean isClick = false;
    private static int preSelectedPage = 0;
    private static int scrollState;
    private int position = 0;
    private int userTime = 0; // 用于记录花费时间

    @Override
    protected int setLayoutId() {
        return R.layout.mockexam_layout;
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
        Type = getIntent().getIntExtra("Type", 1); // 类型
        ZJ_Count = getIntent().getIntExtra("ZJ_Count", 0); //总题目数
        if (ZJ_Count != 10000) {
            idMenuMockTxt.setText("/" + ZJ_Count);
        } else {
            idMenuMockTxt.setText("");
        }

        minute = 44;
        second = 59;

        idTimeView.setText(minute + ":" + second);

        timerTask = new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);

        DefaultUtils.putSharedBoolean(context, true, DefaultUtils.ISFIRSTMOCK, DefaultUtils.ISFIRSTMOCKFLAG);

        MockViewpager.setAdapter(new MyFragmentPagerAdapter(
                getSupportFragmentManager()));

        MockViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean flag = false;

            @Override
            public void onPageSelected(int arg0) {
                position = arg0;
                setNumCenter(arg0 + 1);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                shadowView.setTranslationX(MockViewpager.getWidth() - arg2);

                if (arg2 != 0) {
                    if (scrollState == 1) {//手指按下
                        if (preSelectedPage == arg0) {//表示往左拉，相应的tab往右走
//                            LogUtils.i(TAG, "ux==--> 手指左滑 整体页面--> ");
//                            if (!flag) {
//                                Number++;
//                                flag = true;
//                            }

//                            ToastUtil.show(context, Number + " 已做题数翻页向后");

//                            LogUtils.i(tag, Number + " 1 " + flag);
                        } else {
//                            LogUtils.i(TAG, "ux==--> 手指向右 整体页面<--");
//                            if (!flag) {
//                                Number--;
//                                flag = true;
//                            }

//                            ToastUtil.show(context, Number + " 已做题数翻页向前");

//                            LogUtils.i(TAG, Number + " 2 " + flag);
                        }
                    } else if (scrollState == 2) {
//                        if (preSelectedPage == arg0) {//往左拉
//                            LogUtils.i(TAG, "ux==--> 手指左滑 整体页面--> 页面向右");
//
//
//                        } else {//表示往右拉
//                            LogUtils.i(TAG, "ux==--> 手指右滑 整体页面-->  页面向左");
//
//                        }
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

    /**
     * 题目数
     *
     * @param position
     */
    private void setNumCenter(int position) {

        DefaultUtils.putSharedBoolean(context, false, DefaultUtils.ISFIRSTMOCK, DefaultUtils.ISFIRSTMOCKFLAG);

        idMenuMockPosition.setText(position + "");

        Resquest.getTitleRecord_MNKS(handler2, Type, Uid, 0, position, 1);
    }

    @Override
    protected void onStart() {
        LogUtils.i(tag, "log---------->onStart!");
        super.onStart();
    }

    @Override
    protected void onStop() {
        LogUtils.i(tag, "log---------->onStop!");
        super.onStop();
    }

    @Override
    protected void onResume() {
        LogUtils.i(tag, "log---------->onResume!");
        super.onResume();
        Uid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_ID);
    }

    @Override
    protected void onRestart() {
        LogUtils.i(tag, "log---------->onRestart!");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        LogUtils.i(tag, "log---------->onPause!");
        super.onPause();
    }

    // 交卷
    private void upExam() {

        if (YesNum == 0 && NoNum == 0) {
            finish();
        } else {
            Intent intent = new Intent(context, UpScore_Activity.class);
            intent.putExtra("Type", Type);
            intent.putExtra("Time", userTime);
            intent.putExtra("yesNum", YesNum);
            startActivity(intent);
            finish();
        }

        DefaultUtils.getSharedClear(context, DefaultUtils.QUSMODEL);
    }

    @OnClick({R.id.item_Mockback, R.id.id_uppaperlayout, R.id.id_menuMocklayout, R.id.id_NoMocklayout, R.id.id_YesMocklayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_Mockback: // 返回
                upExam();
                break;
            case R.id.id_uppaperlayout: // 交卷
                upExam();
                break;
            case R.id.id_menuMocklayout: // 题目数
                break;
            case R.id.id_NoMocklayout: // 错题
                break;
            case R.id.id_YesMocklayout: // 对题
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        upExam();

        return false;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
//            System.out.println("handle!");
            userTime++;

            if (minute == 0) {
                if (second == 0) {
                    ToastUtil.show(context, "考试时间结束!");
                    upExam();
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (timerTask != null) {
                        timerTask = null;
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        idTimeView.setText("0" + minute + ":" + second);
                    } else {
                        idTimeView.setText("0" + minute + ":0" + second);
                    }
                }
            } else {
                if (second == 0) {
                    second = 59;
                    minute--;
                    if (minute >= 10) {
                        idTimeView.setText(minute + ":" + second);
                    } else {
                        idTimeView.setText("0" + minute + ":" + second);
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        if (minute >= 10) {
                            idTimeView.setText(minute + ":" + second);
                        } else {
                            idTimeView.setText("0" + minute + ":" + second);
                        }
                    } else {
                        if (minute >= 10) {
                            idTimeView.setText(minute + ":0" + second);
                        } else {
                            idTimeView.setText("0" + minute + ":0" + second);
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        LogUtils.i(tag, "log---------->onDestroy!");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask = null;
        }
        minute = -1;
        second = -1;
        roard_List.clear();
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus

        DefaultUtils.getSharedClear(context, DefaultUtils.ISFIRSTMOCK);
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

    }

    List<RoardAnswer_Event> roard_List = new ArrayList<>();
    int YesNum = 0; //正确数量
    int NoNum = 0; // 错误数量

    @Subscribe
    public void onEventMainThread(RoardAnswer_Event event) {

        String ID = event.getID();
        boolean state = event.isState(); // 正确错误状态

        roard_List.add(event);

        if (state) { // 正确
            YesNum++;
            idYesMockTxt.setText(YesNum + "");
        }
        if (!state) { // 错误
            NoNum++;
            idNoMockTxt.setText(NoNum + "");
        }


    }


    Handler handler5 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                MockViewpager.setCurrentItem((int) msg.obj);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm); // 这句代码必须实现

        }

        @Override
        public Fragment getItem(int position) {

            Mock_Fragment fragment = new Mock_Fragment();
            Bundle bundle = new Bundle();
            bundle.putInt("pageNum", position + 1);
            bundle.putInt("Type", Type);
            fragment.setArguments(bundle);
            return fragment;
        }


        @Override
        public int getCount() {
            return ZJ_Count;
        }


    }

    Handler handler2 = new Handler(new Handler.Callback() {

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

                        if (!mList.get(0).getRecordOption().equals("")) {
                            EventBus.getDefault().post(new BooleanEvent(false));
                            setModelShared(false);
                        } else {
                            EventBus.getDefault().post(new BooleanEvent(true));
                            setModelShared(true);
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


    public void setModelShared(boolean flag) {

        SharedPreferences shared = getSharedPreferences(DefaultUtils.QUSMODEL, MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor edit = shared.edit();
        edit.putBoolean(DefaultUtils.QUSMODELFLAG, flag);
        edit.commit();
    }


}
