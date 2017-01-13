package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.Peilian_Bean;
import com.kunzhuo.xuechelang.model.UserdCar_Bean;
import com.kunzhuo.xuechelang.widget.CustomImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class UserdCar_Adapter extends MyBaseAdapter<UserdCar_Bean> {

    private Holder mHolder;

    public UserdCar_Adapter(Context mContext, List<UserdCar_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.peilian_item, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final UserdCar_Bean bean = mList.get(position);

        if (!bean.getCar_cover().equals("")) {

            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getCar_cover(), mHolder.idPeiImg);
        }

        mHolder.idPeiName.setText(bean.getCar_title());

        mHolder.idPeiMsg.setVisibility(View.GONE);

        mHolder.idPeiPrice.setText("￥ " + bean.getCar_PriceX() + " 万");


        return convertView;
    }

    class Holder {

        @BindView(R.id.id_PeiImg)
        CustomImageView idPeiImg;
        @BindView(R.id.id_PeiName)
        TextView idPeiName;
        @BindView(R.id.id_PeiMsg)
        TextView idPeiMsg;
        @BindView(R.id.id_videoexp)
        RelativeLayout idVideoexp;
        @BindView(R.id.id_PeiPrice)
        TextView idPeiPrice;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}
