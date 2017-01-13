package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.MapUserPic_Bean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/12 0012.
 */
public class MapUserPic_Adapter_2 extends MyBaseAdapter<MapUserPic_Bean> {

    private Holder mHolder;

    public MapUserPic_Adapter_2(Context mContext, List<MapUserPic_Bean> mList) {
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

        final MapUserPic_Bean bean = mList.get(position);

        if (!bean.getPicSrc().equals("")) {
            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getPicSrc(), mHolder.idVideo3Img2);
        } else {
            ImageLoader.getInstance().displayImage("file://" + bean.getHttpImg() + bean.getPicSrc(), mHolder.idVideo3Img2);
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