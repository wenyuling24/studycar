package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.Comment_Bean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class Comment_Adapter extends MyBaseAdapter<Comment_Bean> {


    private Holder mHolder;

    public Comment_Adapter(Context mContext, List<Comment_Bean> mList) {
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

        final Comment_Bean bean = mList.get(position);

        if (bean.getUnickname().equals("")) {

            if (bean.getUaccount().length() == 11) {
                mHolder.idComName.setText(bean.getUaccount().subSequence(0, 3)
                        + "****" + bean.getUaccount().subSequence(7, 11));
            } else {
                mHolder.idComName.setText(bean.getUaccount());
            }

        } else {
            mHolder.idComName.setText(bean.getUnickname());
        }

        mHolder.idComTxt.setText(bean.getContent());

        mHolder.idComTime.setText(bean.getCtime());

        if (!bean.getPic().equals("")) {

            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getPic(), mHolder.idComImg);
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