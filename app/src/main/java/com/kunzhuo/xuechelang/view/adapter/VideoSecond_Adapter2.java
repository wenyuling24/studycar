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
 * Created by Administrator on 2016/9/23 0023.
 */
public class VideoSecond_Adapter2 extends MyBaseAdapter<Video_Bean> {

    private Holder mHolder;

    public VideoSecond_Adapter2(Context mContext, List<Video_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.video_item3, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final Video_Bean bean = mList.get(position);
        mHolder.idVideo3Name.setText(bean.getVtitle());
        if (!bean.getVpid().equals("")) {
            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getVpid(), mHolder.idVideo3Img);
        }

        mHolder.idVideo3Msg.setText(bean.getVcontent());
        mHolder.idVideo3PlayNum.setText(bean.getPlaynumber() + "次");
        mHolder.idVideo3ComNum.setText(bean.getRowNum() + "");

        return convertView;
    }

    class Holder {

        @BindView(R.id.id_video3Img)
        ImageView idVideo3Img;
        @BindView(R.id.id_video3Name)
        TextView idVideo3Name;
        @BindView(R.id.id_video3Msg)
        TextView idVideo3Msg;
        @BindView(R.id.id_video3PlayNum)
        TextView idVideo3PlayNum;
        @BindView(R.id.id_video3ComNum)
        TextView idVideo3ComNum;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}