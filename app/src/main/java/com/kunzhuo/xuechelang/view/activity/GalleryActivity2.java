package com.kunzhuo.xuechelang.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.CoachFile_Bean;
import com.kunzhuo.xuechelang.model.MapUserPic_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.eventutils.FirstEvent;
import com.kunzhuo.xuechelang.widget.viewpagerzoom.PhotoView;
import com.kunzhuo.xuechelang.widget.viewpagerzoom.ViewPagerFixed;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/5 0005.
 */
public class GalleryActivity2 extends Activity {

    private Context context;
    private ImageView back_btn;
    private ProgressDialog dialog;


    // 获取前一个activity传过来的position
    private int position;
    private int ImgType = 0;
    private String ID = "";
    private int PicCount = 4;
    // 当前的位置
    private int location = 0;

    private ArrayList<View> listViews = null;
    private ViewPagerFixed pager;
    private MyPageAdapter adapter;
    private ImageView idHuishou;
    private ArrayList<CoachFile_Bean> imgList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.plugin_camera_gallery);// 切屏到主界面
        context = this;

        back_btn = (ImageView) findViewById(R.id.back_btn);
        idHuishou = (ImageView) findViewById(R.id.id_huishou);
        back_btn.setOnClickListener(new GallerySendListener());


        ImgType = getIntent().getIntExtra("ImgType", 0);
        position = getIntent().getIntExtra("position", 0);

        // 为发送按钮设置文字
        pager = (ViewPagerFixed) findViewById(R.id.gallery01);
        pager.setOnPageChangeListener(pageChangeListener);

        if (ImgType == 1) {
            idHuishou.setVisibility(View.GONE);
            idHuishou.setOnClickListener(new DelListener());
            imgList = (ArrayList<CoachFile_Bean>) getIntent().getSerializableExtra("imgList");

            for (int i = 0; i < imgList.size(); i++) {
                initListViews(imgList.get(i).getHttpImg() + imgList.get(i).getPic());
            }

            adapter = new MyPageAdapter(listViews);
            pager.setAdapter(adapter);
            pager.setPageMargin((int) getResources().getDimensionPixelOffset(
                    R.dimen.ui_10_dip));
            pager.setCurrentItem(position);

        } else {

            idHuishou.setVisibility(View.VISIBLE);
            idHuishou.setOnClickListener(new DelListener());
            imgList = (ArrayList<CoachFile_Bean>) getIntent().getSerializableExtra("imgList");

            for (int i = 0; i < imgList.size(); i++) {
                initListViews(imgList.get(i).getHttpImg() + imgList.get(i).getPic());
            }

            adapter = new MyPageAdapter(listViews);
            pager.setAdapter(adapter);
            pager.setPageMargin((int) getResources().getDimensionPixelOffset(
                    R.dimen.ui_10_dip));
            pager.setCurrentItem(position);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            location = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void initListViews(String url) {
        if (listViews == null)
            listViews = new ArrayList<>();
        PhotoView img = new PhotoView(this);
        img.setBackgroundColor(0xff000000);

//        if (url != null) {
        ImageLoader.getInstance().displayImage(url, img);

//        } else {
//            ImageLoader.getInstance().displayImage("file://" + path, img);
//
//        }

        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        listViews.add(img);
    }


    // 完成按钮的监听
    private class GallerySendListener implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }

    }

    // 删除按钮添加的监听器
    private class DelListener implements View.OnClickListener {

        public void onClick(View v) {
            if (listViews.size() == 1) {
                finish();
            } else {
                if (ImgType == 0) {

                } else {
                    if (ImgType == 2) {
                        imgList.remove(location);
                        pager.removeAllViews();
                        listViews.remove(location);
                        adapter.setListViews(listViews);
                        adapter.notifyDataSetChanged();

                        EventBus.getDefault().post(
                                new FirstEvent(position)); // 移除一个
                    }


                }
            }
        }
    }

    /**
     * 监听返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();

        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        listViews = null;

        System.gc();

    }

    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;

        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }
}