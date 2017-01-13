package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.Video_Bean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/8 0008.
 */
public class VideoSecond_Adapter2_2 extends MyBaseAdapter<Video_Bean> {

    private Holder mHolder;

    public VideoSecond_Adapter2_2(Context mContext, List<Video_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.video_item3_2, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final Video_Bean bean = mList.get(position);
        if (!bean.getVpid().equals("")) {
            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getVpid(), mHolder.idVideo3Img2);
        }


        return convertView;
    }

    class Holder {

        @BindView(R.id.id_video3Img_2)
        ImageView idVideo3Img2;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}