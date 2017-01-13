package com.kunzhuo.xuechelang.view.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.BannerBean;
import com.kunzhuo.xuechelang.model.CarouselList_Bean;
import com.kunzhuo.xuechelang.model.NoviceList_Bean;
import com.kunzhuo.xuechelang.model.Video_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.NormalUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.activity.Login_Activity;
import com.kunzhuo.xuechelang.view.activity.Peilian_Activity;
import com.kunzhuo.xuechelang.view.activity.PlayVideo_Activity;
import com.kunzhuo.xuechelang.view.activity.Strategy_Activity;
import com.kunzhuo.xuechelang.view.activity.TextDrive_Activity;
import com.kunzhuo.xuechelang.view.activity.TrainingAndTest_Activity;
import com.kunzhuo.xuechelang.view.activity.VoiceMoni_Activity;
import com.kunzhuo.xuechelang.view.activity.WebView_Activity;
import com.kunzhuo.xuechelang.view.adapter.Novice_Adapter;
import com.kunzhuo.xuechelang.view.adapter.VideoSecond_Adapter1;
import com.kunzhuo.xuechelang.view.adapter.VideoSecond_Adapter2;
import com.kunzhuo.xuechelang.widget.MyGridView;
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
 * Created by Administrator on 2016/9/22 0022.
 */
public class ThreeSubject_Fragment extends BaseFragment implements PullDownScrollView.RefreshListener {

    private static final String tag = "SecondSubject_Fragment";
    @BindView(R.id.id_ruleLayout)
    RelativeLayout idRuleLayout;
    @BindView(R.id.id_examBefore)
    RelativeLayout idExamBefore;
    @BindView(R.id.id_heGe)
    RelativeLayout idHeGe;
    @BindView(R.id.id_xueChe)
    RelativeLayout idXueChe;
    @BindView(R.id.id_xeSPImg)
    ImageView idXeSPImg;
    @BindView(R.id.id_xunLian)
    RelativeLayout idXunLian;
    @BindView(R.id.id_kcImg)
    ImageView idKcImg;
    @BindView(R.id.id_kaoChang)
    RelativeLayout idKaoChang;
    @BindView(R.id.second_gridView)
    MyGridView secondGridView;
    @BindView(R.id.scrollview)
    MyScrollView scrollview;
    @BindView(R.id.refresh_root)
    PullDownScrollView refreshRoot;
    @BindView(R.id.id_ruleName)
    TextView idRuleName;
    @BindView(R.id.id_topTxt)
    TextView idTopTxt;
    @BindView(R.id.id_videoPosition)
    TextView idVideoPosition;
    @BindView(R.id.second_gridCourseView)
    MyGridView secondGridCourseView;
    @BindView(R.id.first_videoCourselistview)
    MyListView firstVideoCourselistview;
    @BindView(R.id.id_Novicelistview)
    MyListView firstNovicelistview;
    @BindView(R.id.second_gridView2)
    MyGridView secondGridView2;
    @BindView(R.id.id_Novicelistview2)
    MyListView idNovicelistview2;
    @BindView(R.id.second_gridCourseView2)
    MyGridView secondGridCourseView2;
    @BindView(R.id.id_wraplayout)
    RelativeLayout idWraplayout;
    @BindView(R.id.id_examBeforeTxt)
    TextView idExamBeforeTxt;
    @BindView(R.id.id_heGeTxt)
    TextView idHeGeTxt;
    @BindView(R.id.id_shixueImg)
    ImageView idShixueImg;
    @BindView(R.id.id_shijiaImg)
    ImageView idShijiaImg;

    private ImageViewPagerFragment fragmentBanner;
    private SharedPreferences shared;
    private SharedPreferences sharedTime;
    private ProgressDialog dialog;
    private String Uid = "";
    private VideoSecond_Adapter1 adapter;
    private VideoSecond_Adapter1 adapter_2;
    private VideoSecond_Adapter1 adapter2;
    private VideoSecond_Adapter1 adapter2_2;
    private VideoSecond_Adapter2 adapter3;
    private Novice_Adapter novice_Adapter;
    private Novice_Adapter novice_Adapter_2;

    @Override
    protected int setLayoutId() {
        return R.layout.secondsubject_layout;
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

    @Override
    protected void upDetoryViews() {

    }

    @Override
    public void onResume() {
        super.onResume();

//        setTime();

        httpRepost();
    }

    public void initViews() {

        idRuleName.setText("科三考规");
        idExamBeforeTxt.setText("语音模拟");
        idHeGeTxt.setText("灯光操作");

        idVideoPosition.setText("科目三视频");


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

        adapter = new VideoSecond_Adapter1(getActivity(), null);
        secondGridView.setAdapter(adapter);

        adapter_2 = new VideoSecond_Adapter1(getActivity(), null);
        secondGridView2.setAdapter(adapter_2);

        adapter2 = new VideoSecond_Adapter1(getActivity(), null);
        secondGridCourseView.setAdapter(adapter2);

        adapter2_2 = new VideoSecond_Adapter1(getActivity(), null);
        secondGridCourseView2.setAdapter(adapter2_2);

        adapter3 = new VideoSecond_Adapter2(getActivity(), null);
        firstVideoCourselistview.setAdapter(adapter3);

        novice_Adapter = new Novice_Adapter(getActivity(), null);
        firstNovicelistview.setAdapter(novice_Adapter);

        novice_Adapter_2 = new Novice_Adapter(getActivity(), null);
        idNovicelistview2.setAdapter(novice_Adapter_2);

        firstNovicelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NoviceList_Bean bean = (NoviceList_Bean) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), WebView_Activity.class);
                intent.putExtra("Title", bean.getItitle());
                intent.putExtra("IID", bean.getIID());
                startActivity(intent);
            }
        });

        idNovicelistview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NoviceList_Bean bean = (NoviceList_Bean) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), WebView_Activity.class);
                intent.putExtra("Title", bean.getItitle());
                intent.putExtra("IID", bean.getIID());
                startActivity(intent);
            }
        });


        secondGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 5);
                startActivity(intent);
            }
        });

        secondGridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 5);
                startActivity(intent);
            }
        });

        secondGridCourseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 3);
                startActivity(intent);
            }
        });

        secondGridCourseView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 4);
                startActivity(intent);
            }
        });

        firstVideoCourselistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 3);
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
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }


    /**
     * 注册信息1
     */
    public void regsterBoast() {

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mReceiver, mFilter);

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

    @OnClick({R.id.id_xunLian, R.id.id_kaoChang, R.id.id_ruleLayout, R.id.id_examBefore, R.id.id_heGe, R.id.id_xueChe, R.id.id_shixueImg, R.id.id_shijiaImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_xunLian:
                Intent intent = new Intent(getActivity(), TrainingAndTest_Activity.class);
                intent.putExtra("index", 0);
                intent.putExtra("Video_Type", 3);
                startActivity(intent);
                break;
            case R.id.id_kaoChang:
                Intent intent2 = new Intent(getActivity(), TrainingAndTest_Activity.class);
                intent2.putExtra("index", 1);
                intent2.putExtra("Video_Type", 3);
                startActivity(intent2);
                break;
            case R.id.id_ruleLayout:
                Intent intentrule = new Intent(getActivity(), WebView_Activity.class);
                intentrule.putExtra("Title", "科三考规");
                intentrule.putExtra("IID", "13E3F9AB-F7D7-4F4F-BF17-CCF334FA3D03");
                startActivity(intentrule);
                break;
            case R.id.id_examBefore:

                Intent intentVoice = new Intent(getActivity(), VoiceMoni_Activity.class);
                intentVoice.putExtra("TypeThe", 0);
                startActivity(intentVoice);

                break;
            case R.id.id_heGe:

                Intent intentLight = new Intent(getActivity(), VoiceMoni_Activity.class);
                intentLight.putExtra("TypeThe", 1);
                startActivity(intentLight);

                break;
            case R.id.id_xueChe:
                Intent intentStra = new Intent(getContext(), Strategy_Activity.class);
                intentStra.putExtra("Type", 1);
                startActivity(intentStra);
                break;

            case R.id.id_shixueImg: // 陪练

                if (!Uid.equals("")) {
                    Intent intent4 = new Intent(getActivity(), Peilian_Activity.class);

                    startActivity(intent4);
                } else {
                    ToastUtil.show(getActivity(), "您还未登录");
                    Intent intentLogin = new Intent(getActivity(), Login_Activity.class);
                    startActivity(intentLogin);
                }

                break;
            case R.id.id_shijiaImg: // 试驾

                Intent intentSJ = new Intent(getActivity(), TextDrive_Activity.class);

                startActivity(intentSJ);


                break;
        }
    }

    public void httpRepost() {
        Resquest.getCarouselList(handler, 3);
        Resquest.getVideoList(handler2, 6, NormalUtils.setRandom(10), 3);
        Resquest.getVideoList(handler2_2, 6, NormalUtils.setRandom(29), 3);
        Resquest.getVideoList(handler3, 3, 1, 3);
        Resquest.getVideoList(handler3_2, 3, 2, 3);
        Resquest.getVideoList(handler4, 4, 1, 2);
        Resquest.getNoviceList(handler5, 1, NormalUtils.setRandom(48), 1);
        Resquest.getNoviceList(handler6, 1, NormalUtils.setRandom(24), 2);
        Resquest.getNoviceList(handler6_2, 0, NormalUtils.setRandom(24), 2);
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

    Handler handler2_2 = new Handler(new Handler.Callback() {

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
                        adapter_2.setData(mList);
                        adapter_2.notifyDataSetChanged();

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
                        adapter2.setData(mList);
                        adapter2.notifyDataSetChanged();

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

    Handler handler3_2 = new Handler(new Handler.Callback() {

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
                        adapter2_2.setData(mList);
                        adapter2_2.notifyDataSetChanged();


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
                        Type type = new TypeToken<List<Video_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<Video_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        adapter3.setData(mList);
                        adapter3.notifyDataSetChanged();

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

    Handler handler5 = new Handler(new Handler.Callback() {

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

    Handler handler6 = new Handler(new Handler.Callback() {

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
                        novice_Adapter.setData(mList);
                        novice_Adapter.notifyDataSetChanged();

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

    Handler handler6_2 = new Handler(new Handler.Callback() {

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
                        novice_Adapter_2.setData(mList);
                        novice_Adapter_2.notifyDataSetChanged();

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
//        List<String> temptryTolearnList = Arrays.asList(bean.getTryTolearn()); // 试学轮播图片
//        List<String> tempSparringList = Arrays.asList(bean.getSparring()); // 陪练
        String http = bean.getHttp();


        for (int i = 0; i < tempBannerList.size(); i++) {
            BannerBean bannerBean = new BannerBean();
            bannerBean.setId(i);
            bannerBean.setImage(http + tempBannerList.get(i));
            list.add(bannerBean);
        }
        fragmentBanner.sendImgList(list);
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


    Timer timer;
    private boolean isShowShiFlag = false;

    Handler handler7 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    isShowShiFlag = true;
                    idWraplayout.setVisibility(View.GONE);
                    timer.cancel();
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
                handler7.sendMessage(msg);

//                ToastUtil.show(getActivity(), msg.what + "秒");

            }

        }, 1000, 5 * 1000);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
