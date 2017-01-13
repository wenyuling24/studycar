package com.kunzhuo.xuechelang.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.widget.NoScrollViewPager;
import com.kunzhuo.xuechelang.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/29 0029.
 */
public class StudentMain_Fragmnet extends BaseFragment {

    @BindView(R.id.index_tabs)
    PagerSlidingTabStrip indexTabs;
    @BindView(R.id.viewpager_study)
    NoScrollViewPager viewpagerStudy;

    private Context context;
    private int selectPosition = 0;

    @Override
    protected int setLayoutId() {
        return R.layout.stumain_layout;
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

        context = getActivity();

        Intent intent = getActivity().getIntent();
        int page = intent.getIntExtra("index", 0);// 第一个参数是取值的key,第二个参数是默认值

        setFragmentViewPager();
        viewpagerStudy.setCurrentItem(page);
        viewpagerStudy.setOffscreenPageLimit(5);
        viewpagerStudy.setNoScroll(true);// 禁止滑动


    }

    private Fragment getFragment(int selectPosition) {
        this.selectPosition = selectPosition;

        Fragment select = null;

        switch (selectPosition) {
            case 0:
                select = new FirstSubject_Fragment();
                break;
            case 1:

                select = new SecondSubject_Fragment();

                break;
            case 2:

                select = new ThreeSubject_Fragment();

                break;
            case 3:

                select = new FourSubject_Fragment();

                break;
            case 4:

                select = new PartnerStudy_Fragment();

                break;

            default:
                select = new FirstSubject_Fragment();
        }

        return select;
    }


    public void setFragmentViewPager() {

        viewpagerStudy.setAdapter(new MyFragmentPagerAdapter(
                getChildFragmentManager()));

        indexTabs.setViewPager(viewpagerStudy);

        indexTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                selectPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private String[] titles = {"科目一", "科目二", "科目三", "科目四", "找教练"};

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm); // 这句代码必须实现
        }

        // 返回当前项
        @Override
        public Fragment getItem(int position) {

            return getFragment(position);
        }

        // 返回总的数量
        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

    }

}
