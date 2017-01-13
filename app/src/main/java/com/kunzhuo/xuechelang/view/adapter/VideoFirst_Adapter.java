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
 * Created by Administrator on 2016/9/21 0021.
 */
public class VideoFirst_Adapter extends MyBaseAdapter<Video_Bean> {

    private Holder mHolder;

    public VideoFirst_Adapter(Context mContext, List<Video_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.video_item1, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final Video_Bean bean = mList.get(position);
        mHolder.videoName.setText(bean.getVtitle());
        mHolder.idVideoPlayNum.setText(bean.getPlaynumber() + "次");

        if (!bean.getVpid().equals("")) {
            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getVpid(), mHolder.idVideoImg);
        }

        return convertView;
    }

    class Holder {

        @BindView(R.id.id_videoImg)
        ImageView idVideoImg;
        @BindView(R.id.video_Name)
        TextView videoName;
        @BindView(R.id.id_videoPlayNum)
        TextView idVideoPlayNum;
        @BindView(R.id.id_videoScangNum)
        TextView idVideoScangNum;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}
