package com.kunzhuo.xuechelang.view.adapter;

import java.util.List;

import com.kunzhuo.xuechelang.model.BannerBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.support.v4.view.PagerAdapter;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * 广告轮播图片适配
 *
 * @author Administrator
 */
public class BannerPagerAdapter extends PagerAdapter {

    private List<BannerBean> mList;
    private Context mContext;

    public BannerPagerAdapter(Context context, List<BannerBean> list) {
        this.mList = list;
        this.mContext = context;
    }

    public void setData(List<BannerBean> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgView = new ImageView(mContext);
        String url = mList.get(position).getImage();
        int id = mList.get(position).getId();

        if (url != null && !url.equals("")) {
            ImageLoader.getInstance().displayImage(url, imgView);
            imgView.setScaleType(ScaleType.CENTER_CROP);

        } else {

            imgView.setImageBitmap(BitmapFactory.decodeResource(
                    mContext.getResources(), id));
            imgView.setScaleType(ScaleType.CENTER_CROP);

            if (position == 2) {

                imgView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }

        container.addView(imgView);
        return imgView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
        container.removeView((View) obj);
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }
}