package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.MapCommentList_Bean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class MapComment_Adapter extends MyBaseAdapter<MapCommentList_Bean> {


    private Holder mHolder;

    public MapComment_Adapter(Context mContext, List<MapCommentList_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.comment_layout, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final MapCommentList_Bean bean = mList.get(position);

        mHolder.idComName.setText(bean.getName());
        mHolder.idComTxt.setText(bean.getComment());

        mHolder.idComTime.setText(bean.getCtime());

        if (!bean.getHead_portrait().equals("")) {

            ImageLoader.getInstance().displayImage(bean.getHead_portrait(), mHolder.idComImg);
        }

        return convertView;
    }

    class Holder {

        @BindView(R.id.id_comImg)
        ImageView idComImg;
        @BindView(R.id.id_comName)
        TextView idComName;
        @BindView(R.id.id_comTxt)
        TextView idComTxt;
        @BindView(R.id.id_comTime)
        TextView idComTime;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}