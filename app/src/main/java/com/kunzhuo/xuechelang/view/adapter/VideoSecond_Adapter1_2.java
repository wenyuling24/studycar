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
public class VideoSecond_Adapter1_2 extends MyBaseAdapter<Video_Bean> {

    private Holder mHolder;

    public VideoSecond_Adapter1_2(Context mContext, List<Video_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.video_item4, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final Video_Bean bean = mList.get(position);
        mHolder.idVideo2Name.setText(bean.getVtitle());

        if (!bean.getVpid().equals("")) {
            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getVpid(), mHolder.idVideo2Img);
        }

        return convertView;
    }

    class Holder {

        @BindView(R.id.id_video2Img)
        ImageView idVideo2Img;
        @BindView(R.id.id_video2Name)
        TextView idVideo2Name;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}