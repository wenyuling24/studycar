package com.kunzhuo.xuechelang.view.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kunzhuo.xuechelang.AndroidApplication;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.BannerBean;
import com.kunzhuo.xuechelang.view.adapter.BannerPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 轮播图片
 *
 * @author SB
 */
public class ImageViewPagerFragment extends BaseFragment2 {

    public final static String TAG = "ImageViewPagerFragment";
    private final static int SCHEDULE_PERIOD = 5;// 间隔时间

    @BindView(R.id.ll_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.ll_dots)
    LinearLayout ll_dots;

    private List<ImageView> dotsList;
    private BannerPagerAdapter mAdapter;

    private ScheduledExecutorService scheduledExecutorService;
    protected Context mContext;


    @Override
    protected int setLayoutId() {
        return R.layout.banner;
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

        mContext = getActivity();

        mAdapter = new BannerPagerAdapter(mContext, null);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mViewPager.setOnPageChangeListener(new MyPageChangeListener());
        mViewPager.setFocusable(true);
        mViewPager.setFocusableInTouchMode(true);

    }

    /**
     * 添加圆点
     */
    private void addDotsView(int num) {

        dotsList = new ArrayList<ImageView>();
        ll_dots.removeAllViews();

        int paddingValue = dp2px(2);

        for (int i = 0; i < num; i++) {

            ImageView imgdot = new ImageView(mContext);

            if (i == 0) {
                imgdot.setImageResource(R.drawable.dot_focused);
            } else {
                imgdot.setImageResource(R.drawable.dot_normal);
            }
            dotsList.add(imgdot);

            imgdot.setPadding(paddingValue, paddingValue, paddingValue,
                    paddingValue);
            ll_dots.addView(imgdot);

        }
    }

    private int currentItem = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mViewPager.setCurrentItem(currentItem);
        }
    };

    public void sendImgList(ArrayList<BannerBean> imgList) {
        if (imgList == null || imgList.size() == 0) {
            return;
        }

        mAdapter.setData(imgList);

        mAdapter.notifyDataSetChanged();

        addDotsView(imgList.size());


    }

    private void startAd() {
        currentItem = 0;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1,
                SCHEDULE_PERIOD, TimeUnit.SECONDS);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        startAd();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 当Activity不可见的时候停止切换
        scheduledExecutorService.shutdown();
        handler.removeCallbacksAndMessages(null);
    }

    private class ScrollTask implements Runnable {

        @Override
        public void run() {
            synchronized (mViewPager) {
                int size = dotsList.size();
                if (size != 0) {
                    currentItem = (currentItem + 1) % size;
                    handler.sendEmptyMessage(100);
                }
            }
        }
    }

    private class MyPageChangeListener implements OnPageChangeListener {
        int prePostion = 0;

        @Override
        public void onPageScrollStateChanged(int position) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

            mViewPager.getParent().requestDisallowInterceptTouchEvent(true);
        }

        @Override
        public void onPageSelected(int position) {

            if (dotsList == null || dotsList.size() == 0) {
                return;
            }
            position = position % dotsList.size();

            dotsList.get(position).setImageResource(R.drawable.dot_focused);
            dotsList.get(prePostion).setImageResource(R.drawable.dot_normal);

            prePostion = position;

            mViewPager.setCurrentItem(position);
        }
    }

    public int dp2px(float dp) {
        float scale = AndroidApplication.getInstance().getResources()
                .getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
