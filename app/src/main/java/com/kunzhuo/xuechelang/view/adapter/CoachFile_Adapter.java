package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.CoachFile_Bean;
import com.kunzhuo.xuechelang.model.MapUserPic_Bean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5 0005.
 */
public class CoachFile_Adapter extends MyBaseAdapter<CoachFile_Bean> {

    private Holder mHolder;

    public CoachFile_Adapter(Context mContext, List<CoachFile_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.pic_item2, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final CoachFile_Bean bean = mList.get(position);

        if (!bean.getPic().equals("")) {
            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getPic(), mHolder.idVideo3Img2);
        } else {
            ImageLoader.getInstance().displayImage("file://" + bean.getHttpImg() + bean.getPic(), mHolder.idVideo3Img2);
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