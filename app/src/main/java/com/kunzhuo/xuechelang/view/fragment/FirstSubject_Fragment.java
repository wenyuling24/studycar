package com.kunzhuo.xuechelang.view.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.BannerBean;
import com.kunzhuo.xuechelang.model.CarouselList_Bean;
import com.kunzhuo.xuechelang.model.NoviceList_Bean;
import com.kunzhuo.xuechelang.model.OneSubjectCount_Bean;
import com.kunzhuo.xuechelang.model.Video_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.NormalUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.activity.ChartsList_Activity;
import com.kunzhuo.xuechelang.view.activity.Login_Activity;
import com.kunzhuo.xuechelang.view.activity.MockExam_Activity;
import com.kunzhuo.xuechelang.view.activity.MockRecord_Activity;
import com.kunzhuo.xuechelang.view.activity.Peilian_Activity;
import com.kunzhuo.xuechelang.view.activity.PlayVideo_Activity;
import com.kunzhuo.xuechelang.view.activity.ProblemMain_Activity;
import com.kunzhuo.xuechelang.view.activity.QuestionBank_Activity;
import com.kunzhuo.xuechelang.view.activity.TextDrive_Activity;
import com.kunzhuo.xuechelang.view.activity.WebView_Activity;
import com.kunzhuo.xuechelang.view.adapter.VideoFirst_Adapter;
import com.kunzhuo.xuechelang.widget.MyListView;
import com.kunzhuo.xuechelang.widget.MyScrollView;
import com.kunzhuo.xuechelang.widget.PullDownElasticImp;
import com.kunzhuo.xuechelang.widget.PullDownScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by waaa on 2016/9/9.
 * 科目一
 */
public class FirstSubject_Fragment extends BaseFragment implements PullDownScrollView.RefreshListener {

    private static final String tag = "FirstSubject_Fragment";
    @BindView(R.id.id_orderNum)
    TextView idOrderNum;
    @BindView(R.id.id_orderexercise)
    LinearLayout idOrderexercise;
    @BindView(R.id.id_randomNum)
    TextView idRandomNum;
    @BindView(R.id.id_randomexercise)
    LinearLayout idRandomexercise;
    @BindView(R.id.id_specialNum)
    TextView idSpecialNum;
    @BindView(R.id.id_specialexercise)
    LinearLayout idSpecialexercise;
    @BindView(R.id.id_notDoneNum)
    TextView idNotDoneNum;
    @BindView(R.id.id_notDone)
    LinearLayout idNotDone;
    @BindView(R.id.id_moNiexercise)
    LinearLayout idMoNiexercise;
    @BindView(R.id.id_jiqiaoexercise)
    LinearLayout idJiqiaoexercise;
    @BindView(R.id.id_bang)
    LinearLayout idBang;
    @BindView(R.id.id_Record)
    LinearLayout idRecord;
    @BindView(R.id.selfculture_Img)
    ImageView selfcultureImg;
    @BindView(R.id.id_selfculture)
    RelativeLayout idSelfculture;
    @BindView(R.id.id_errorNum)
    TextView idErrorNum;
    @BindView(R.id.id_myError)
    LinearLayout idMyError;
    @BindView(R.id.id_soucangNum)
    TextView idSoucangNum;
    @BindView(R.id.id_myCollect)
    LinearLayout idMyCollect;
    @BindView(R.id.id_roadNum)
    TextView idRoadNum;
    @BindView(R.id.id_roadSign)
    LinearLayout idRoadSign;
    @BindView(R.id.id_lawNum)
    TextView idLawNum;
    @BindView(R.id.id_Law)
    LinearLayout idLaw;
    @BindView(R.id.id_SharedBtn)
    TextView idSharedBtn;
    @BindView(R.id.sc)
    LinearLayout sc;
    @BindView(R.id.scrollview)
    MyScrollView scrollview;
    @BindView(R.id.refresh_root)
    PullDownScrollView refreshRoot;
    @BindView(R.id.first_videolistview)
    MyListView firstListview;
    @BindView(R.id.id_topTxt)
    TextView idTopTxt;
    @BindView(R.id.id_wraplayout)
    RelativeLayout idWraplayout;
    @BindView(R.id.id_shixueImg)
    ImageView idShixueImg;
    @BindView(R.id.id_shijiaImg)
    ImageView idShijiaImg;
    @BindView(R.id.id_Sanjiao)
    ImageView idSanjiao;


    private ImageViewPagerFragment fragmentBanner;
    private SharedPreferences sharedTime;
    private ProgressDialog dialog;
    private String Uid = "";
    private VideoFirst_Adapter adapter;
    private boolean isShowShiFlag = false;

    @Override
    protected int setLayoutId() {
        return R.layout.firstsubject_layout;
    }

    @Override
    protected void setDefaultViews() {
        initViews();
        regsterBoast();
    }

    @Override
    protected void requestData() {
    }

    @Override
    protected void updateViews() {
    }

    public void initViews() {

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("客官稍候, 正在玩命加载中...");
        Uid = DefaultUtils.getShared(getActivity(), DefaultUtils.USER, DefaultUtils.USER_ID);

        // 每次打开， 从文件拿到跟新时间
        sharedTime = getActivity().getSharedPreferences("updateTimeInfo",
                Context.MODE_WORLD_WRITEABLE);

        refreshRoot.setRefreshListener(this);
        refreshRoot.setPullDownElastic(new PullDownElasticImp(
                getActivity()));
        String lastUpdateTimeFromFile = NormalUtils
                .getLastUpdateTime(sharedTime.getString("blog_update_time",
                        "首次更新"));
        refreshRoot.finishRefresh(lastUpdateTimeFromFile);

        adapter = new VideoFirst_Adapter(getActivity(), null);
        firstListview.setAdapter(adapter);

        firstListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 5);
                startActivity(intent);
            }
        });

        fragmentBanner = new ImageViewPagerFragment();

        FragmentTransaction trans2 = getChildFragmentManager()
                .beginTransaction();
        trans2.add(R.id.fragment_banner, fragmentBanner);
        trans2.commit();

        idWraplayout.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();

//        setTime();
        httpRepost();
    }

    @OnClick({R.id.id_orderexercise, R.id.id_randomexercise, R.id.id_specialexercise, R.id.id_notDone,
            R.id.id_moNiexercise, R.id.id_jiqiaoexercise, R.id.id_bang, R.id.id_Record,
            R.id.id_selfculture, R.id.id_myError, R.id.id_myCollect, R.id.id_roadSign, R.id.id_Law,
            R.id.id_SharedBtn, R.id.id_shixueImg, R.id.id_shijiaImg, R.id.id_Sanjiao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_orderexercise:
                Intent intent1 = new Intent(getActivity(), ProblemMain_Activity.class);
                intent1.putExtra("ChapterID", "");
                intent1.putExtra("RecType", 1);
                intent1.putExtra("Number", fromTextView(idOrderNum));
                intent1.putExtra("T_Subject", "");
                intent1.putExtra("ZJ_Count", 10000);
                intent1.putExtra("Type", 1);
                startActivity(intent1);
                break;
            case R.id.id_randomexercise:
                Intent intent2 = new Intent(getActivity(), ProblemMain_Activity.class);
                intent2.putExtra("ChapterID", "");
                intent2.putExtra("RecType", 2);
                intent2.putExtra("Number", fromTextView(idRandomNum));
                intent2.putExtra("T_Subject", "");
                intent2.putExtra("ZJ_Count", 10000);
                intent2.putExtra("Type", 1);
                startActivity(intent2);
                break;
            case R.id.id_specialexercise:
                Intent intent3 = new Intent(getActivity(), QuestionBank_Activity.class);
                intent3.putExtra("ChapterListType", 1);
                startActivity(intent3);
                break;
            case R.id.id_notDone:

                break;
            case R.id.id_moNiexercise:
                if (!Uid.equals("")) {
                    Intent intent = new Intent(getActivity(), MockExam_Activity.class);
                    intent.putExtra("Type", 1);
                    intent.putExtra("ZJ_Count", 100);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), Login_Activity.class);
                    startActivity(intent);
                }
                break;
            case R.id.id_jiqiaoexercise:
                break;
            case R.id.id_bang:

                Intent intentbang = new Intent(getActivity(), ChartsList_Activity.class);
                intentbang.putExtra("Type", 1);
                startActivity(intentbang);

                break;
            case R.id.id_Record:

                if (!Uid.equals("")) {

                    Intent intent = new Intent(getActivity(), MockRecord_Activity.class);
                    intent.putExtra("Type", 1);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), Login_Activity.class);
                    startActivity(intent);
                }
                break;
            case R.id.id_selfculture:

                Intent intentself = new Intent(getContext(), WebView_Activity.class);

                intentself.putExtra("Url", "http://www.cqxpxt.com/");
                intentself.putExtra("Title", "西陪学堂");

                startActivity(intentself);

                break;
            case R.id.id_myError:
                if (!Uid.equals("")) {

                    if (fromTextView(idErrorNum) != 0) {
                        Intent intent4 = new Intent(getActivity(), ProblemMain_Activity.class);
                        intent4.putExtra("ChapterID", "");
                        intent4.putExtra("RecType", 98);
//                        intent4.putExtra("Number", fromTextView(idErrorNum));
                        intent4.putExtra("T_Subject", "");
                        intent4.putExtra("ZJ_Count", fromTextView(idErrorNum));
                        intent4.putExtra("Type", 1);
                        startActivity(intent4);
                    } else {
                        ToastUtil.show(getActivity(), "您暂时还没有错题");
                    }

                } else {
                    Intent intent = new Intent(getActivity(), Login_Activity.class);
                    startActivity(intent);
                }
                break;
            case R.id.id_myCollect:

                if (!Uid.equals("")) {

                    if (fromTextView(idSoucangNum) != 0) {
                        Intent intent5 = new Intent(getActivity(), ProblemMain_Activity.class);
                        intent5.putExtra("ChapterID", "");
                        intent5.putExtra("RecType", 99);
//                        intent5.putExtra("Number", fromTextView(idSoucangNum));
                        intent5.putExtra("T_Subject", "");
                        intent5.putExtra("ZJ_Count", fromTextView(idSoucangNum));
                        intent5.putExtra("Type", 1);
                        startActivity(intent5);
                    } else {
                        ToastUtil.show(getActivity(), "您还没有收藏题目");
                    }

                } else {
                    Intent intent = new Intent(getActivity(), Login_Activity.class);
                    startActivity(intent);
                }

                break;
            case R.id.id_roadSign: // 交通标志

                Intent intent6 = new Intent(getActivity(), ProblemMain_Activity.class);
                intent6.putExtra("ChapterID", "");
//                intent6.putExtra("Number", fromTextView(idRoadNum));
                intent6.putExtra("T_Subject", "6DA02B26-7669-4696-BC48-0C52F6109716");
                intent6.putExtra("ZJ_Count", fromTextView(idRoadNum));
                intent6.putExtra("Type", 0);
                startActivity(intent6);


                break;
            case R.id.id_Law: // 法律法规

                Intent intent7 = new Intent(getActivity(), ProblemMain_Activity.class);
                intent7.putExtra("ChapterID", "");
//                intent7.putExtra("Number", fromTextView(idLawNum));
                intent7.putExtra("T_Subject", "94EB635F-CF8C-40AF-918A-F9C21EB05207");
                intent7.putExtra("ZJ_Count", fromTextView(idLawNum));
                intent7.putExtra("Type", 0);
                startActivity(intent7);

                break;
            case R.id.id_SharedBtn:
                showSharedPop();
                break;
            case R.id.id_shixueImg: // 陪练

                if (!Uid.equals("")) {
                    Intent intent4 = new Intent(getActivity(), Peilian_Activity.class);

                    startActivity(intent4);
                } else {
                    ToastUtil.show(getActivity(), "您还未登录");
                    Intent intent = new Intent(getActivity(), Login_Activity.class);
                    startActivity(intent);
                }

                break;
            case R.id.id_shijiaImg: // 试驾

                Intent intentSJ = new Intent(getActivity(), TextDrive_Activity.class);

                startActivity(intentSJ);


                break;
            case R.id.id_Sanjiao: // 下拉广告

                if (isShowShiFlag) {

                    idWraplayout.setVisibility(View.VISIBLE);
                    setTime();
                }
                break;
        }
    }


    @Override
    protected void upDetoryViews() {
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
        handler6.removeCallbacks(null);
    }

    @Override
    public void onRefresh(PullDownScrollView view) {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                String curTime = NormalUtils.getCurTime();
                setUpdateTime(curTime);
                String updateTime = NormalUtils.getLastUpdateTime(curTime);
                refreshRoot.finishRefresh("上次刷新时间: " + updateTime);
                httpRepost();
            }
        }, 2000);

    }

    /**
     * 在文件中写入刷新时间
     */
    public void setUpdateTime(String curTime) {
        SharedPreferences.Editor editor = sharedTime.edit();
        editor.putString("blog_update_time", curTime);
        editor.commit();
    }

    public void httpRepost() {
        Resquest.getCarouselList(handler, 1);
        Resquest.getOneSubjectCount(handler2, 1, Uid);
        Resquest.getVideoList(handler3, 6, NormalUtils.setRandom(29), 3);
        Resquest.getNoviceList(handler4, 1, 3, 1);
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    System.out.println(jsonObject.toString() + "  首页轮播获取的json");
                    try {
                        Type type = new TypeToken<List<CarouselList_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<CarouselList_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        setBannerList(mList.get(0));


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
                        Type type = new TypeToken<List<OneSubjectCount_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<OneSubjectCount_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        setOneSubjectCount_Bean(mList.get(0));

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

    Handler handler3 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<Video_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<Video_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        adapter.setData(mList);
                        adapter.notifyDataSetChanged();

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
                    try {
                        Type type = new TypeToken<List<NoviceList_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<NoviceList_Bean> mList = mapper.transformCollection(jsonObject, "List");

                        setTodayTop(mList.get(0));

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

    private void setTodayTop(final NoviceList_Bean bean) {

        idTopTxt.setText(bean.getItitle());
        idTopTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebView_Activity.class);
                intent.putExtra("Title", bean.getItitle());
                intent.putExtra("IID", bean.getIID());
                startActivity(intent);

            }
        });

    }

    /**
     * 设置首页轮播图片
     *
     * @param bean
     */
    private void setBannerList(CarouselList_Bean bean) {

        ArrayList<BannerBean> list = new ArrayList<>();
        List<String> tempBannerList = Arrays.asList(bean.getBanner().split(",")); // 首页轮播图片
        List<String> temptryTolearnList = Arrays.asList(bean.getTryTolearn()); // 试学轮播图片
        List<String> tempSparringList = Arrays.asList(bean.getSparring()); // 陪练
        String http = bean.getHttp();


        for (int i = 0; i < tempBannerList.size(); i++) {
            BannerBean bannerBean = new BannerBean();
            bannerBean.setId(i);
            bannerBean.setImage(http + tempBannerList.get(i));
            list.add(bannerBean);
        }
        fragmentBanner.sendImgList(list);
    }

    private void setOneSubjectCount_Bean(OneSubjectCount_Bean bean) {

        idOrderNum.setText(bean.getOrder() + "");
        idRandomNum.setText(bean.getRandom() + "");
        idSpecialNum.setText(bean.getSpecial() + "");
        idNotDoneNum.setText(bean.getSubject() - judgeNum(bean.getOrder(), bean.getRandom(), bean.getSpecial()) + "");
        idErrorNum.setText(bean.getError() + "");
        idSoucangNum.setText(bean.getColnTo() + "");
        idRoadNum.setText(bean.getTrafficSign() + "");
        idLawNum.setText(bean.getRegulations() + "");

    }

    private int fromTextView(TextView view) {

        int num = Integer.parseInt(view.getText().toString());

        return num;

    }

    private int judgeNum(int a, int b, int c) {
        int array[] = {a, b, c};
        for (int i = 0; i < array.length; i++) {

            for (int j = i + 1; j < array.length; j++) {

                if (array[i] < array[j]) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }

        return array[2];
    }


    /**
     * 注册信息1
     */
    public void regsterBoast() {

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mReceiver, mFilter);

    }

    /**
     * 展示是否有网络
     *
     * @author Administrator
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d(tag, "网络状态已经改变");
                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    System.out.println("getAcivity当前网络名称：" + name);
                    // doSomething()

                    httpRepost();

                } else {
                    System.out.println("getAcivity没有可用网络");
                    ToastUtil.show(getActivity(), "网络异常");

                }
            }
        }
    };

    PopupWindow popupWindow;

    private void showSharedPop() {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.shared_layout, null);

        popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout weiChat = (LinearLayout) view.findViewById(R.id.id_weichat);
        LinearLayout friendsCycle = (LinearLayout) view.findViewById(R.id.id_friendslayout);
        LinearLayout qq = (LinearLayout) view.findViewById(R.id.id_qqlayout);
        LinearLayout sina = (LinearLayout) view.findViewById(R.id.id_sinalayout);

        view.findViewById(R.id.dis_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setTouchable(true);// 可点击
        popupWindow.setOutsideTouchable(true);// 设置外部可点击,点击取消
        popupWindow.setFocusable(true);// 设置可聚焦

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // popupWindow.dismiss();
                return true;
            }
        });

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 返回false,事件才能下发
                return false;
            }
        });

        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getActivity().getWindow()
                .getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });

        ColorDrawable dw = new ColorDrawable(0xb0000000);

        popupWindow.setBackgroundDrawable(dw);

        popupWindow.showAtLocation(sc, Gravity.BOTTOM, 0, 0);

    }

    Timer timer;

    Handler handler6 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (!isShowShiFlag) {
                        isShowShiFlag = true;
                        idWraplayout.setVisibility(View.GONE);
                        timer.cancel();
                    }
                    break;
            }

            return true;
        }
    });

    private void setTime() {

        timer = new Timer();

        // 定义计划任务，根据参数的不同可以完成以下种类的工作：在固定时间执行某任务，在固定时间开始重复执行某任务，
        // 重复时间间隔可控，在延迟多久后执行某任务，在延迟多久后重复执行某任务，重复时间间隔可控
        timer.schedule(new TimerTask() {

            int i = 2;

            // TimerTask 是个抽象类,实现的是Runable类
            @Override
            public void run() {

                Log.i("yao", Thread.currentThread().getName());

                //定义一个消息传过去
                Message msg = new Message();
                msg.what = i--;
                handler6.sendMessage(msg);

//                ToastUtil.show(getActivity(), msg.what + "秒");

            }

        }, 1000, 5 * 1000);

    }

}
