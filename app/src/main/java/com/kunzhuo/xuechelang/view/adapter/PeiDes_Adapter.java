package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.Peilian_Bean;
import com.kunzhuo.xuechelang.widget.CustomImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/31 0031.
 */
public class PeiDes_Adapter extends MyBaseAdapter<Peilian_Bean> {

    private Holder mHolder;

    public PeiDes_Adapter(Context mContext, List<Peilian_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.peilian_item2, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final Peilian_Bean bean = mList.get(position);

        if (!bean.getProCover().equals("")) {

            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getProCover(), mHolder.idPeiImg);
        }

        mHolder.idPeiName.setText(bean.getProName());
        mHolder.idPeiPrice.setText("￥ " + bean.getProMoney());

        return convertView;
    }

    class Holder {

        @BindView(R.id.id_PeiImg)
        ImageView idPeiImg;
        @BindView(R.id.id_PeiName)
        TextView idPeiName;
        @BindView(R.id.id_PeiPrice)
        TextView idPeiPrice;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}