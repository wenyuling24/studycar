package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.UsedCarPic_Bean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class CarPic_Adapter extends MyBaseAdapter<UsedCarPic_Bean> {


    private Holder mHolder;

    public CarPic_Adapter(Context mContext, List<UsedCarPic_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.carlic_item, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final UsedCarPic_Bean bean = mList.get(position);

        if (!bean.getCar_src().equals("")) {

            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getCar_src(), mHolder.idCarDesImg);
        }


        return convertView;
    }

    class Holder {

        @BindView(R.id.id_carDesImg)
        ImageView idCarDesImg;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}
