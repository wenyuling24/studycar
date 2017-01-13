package com.kunzhuo.xuechelang.view.fragment;

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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.BannerBean;
import com.kunzhuo.xuechelang.model.CarouselList_Bean;
import com.kunzhuo.xuechelang.model.VideoType_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.NormalUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.widget.PagerSlidingTabStrip;
import com.kunzhuo.xuechelang.widget.PullDownElasticImp;
import com.kunzhuo.xuechelang.widget.PullDownScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/10/8 0008.
 */
public class ExamRoom_Fragment extends BaseFragment implements PullDownScrollView.RefreshListener {

    private static final String tag = "Train_Frament";

    @BindView(R.id.train_Tabs)
    PagerSlidingTabStrip mTabs;
    @BindView(R.id.train_Viewpager)
    ViewPager mViewPager;
    @BindView(R.id.refresh_root)
    PullDownScrollView refreshRoot;

    private SharedPreferences sharedTime;
    private ImageViewPagerFragment fragmentBanner;
    private int Type = 2;
    private int Category = 2;

    @Override
    protected int setLayoutId() {
        return R.layout.train_fragment;
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

    }

    private void initViews() {

        Bundle bundle = getArguments();
        Type = bundle.getInt("Video_Type", 2);
        Category = bundle.getInt("Video_Category", 2);

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

        fragmentBanner = new ImageViewPagerFragment();

        FragmentTransaction trans2 = getChildFragmentManager()
                .beginTransaction();
        trans2.add(R.id.fragment_banner, fragmentBanner);
        trans2.commit();

        httpRepost();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
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

    private void httpRepost() {
        Resquest.getCarouselList(handler, 1);
        Resquest.getVideoType(handler2, Type, Category);
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    System.out.println(jsonObject.toString() + "  首页轮播获取的json");
                    try {
                        java.lang.reflect.Type type = new TypeToken<List<CarouselList_Bean>>() {
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
                        Type type = new TypeToken<List<VideoType_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<VideoType_Bean> mList = mapper.transformCollection(jsonObject, "List");

                        mViewPager.setAdapter(new MyFragmentPagerAdapter(
                                getChildFragmentManager(), mList));

                        mTabs.setViewPager(mViewPager);


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


    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        List<VideoType_Bean> mList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<VideoType_Bean> list) {
            super(fm); // 这句代码必须实现
            mList = list;
        }

        @Override
        public Fragment getItem(int position) {
            TrainDetail_Fragment fragment = new TrainDetail_Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("ID", mList.get(position).getPid());
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mList.get(position).getPname();
        }

    }
}