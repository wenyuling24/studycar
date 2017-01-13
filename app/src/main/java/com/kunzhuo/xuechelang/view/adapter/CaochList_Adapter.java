package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.CoachList_Bean;
import com.kunzhuo.xuechelang.widget.CustomImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
public class CaochList_Adapter extends MyBaseAdapter<CoachList_Bean> {


    private Holder mHolder;

    public CaochList_Adapter(Context mContext, List<CoachList_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.coachlist_item, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final CoachList_Bean bean = mList.get(position);

        mHolder.idCoachName.setText("姓名: " + bean.getUname());
        mHolder.idCoachArea.setText("所属区域: " + bean.getPname());

        if (!bean.getPic().equals("")) {

            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getPic(), mHolder.idCoachImg);
        }

        if(!bean.getSeniority().equals("")){
            mHolder.idCoachYear.setVisibility(View.VISIBLE);
            mHolder.idCoachYear.setText("教龄: " + bean.getSeniority() + "年");
        }else{
            mHolder.idCoachYear.setVisibility(View.GONE);
        }


        return convertView;
    }

    class Holder {

        @BindView(R.id.id_coachImg)
        CustomImageView idCoachImg;
        @BindView(R.id.id_coachName)
        TextView idCoachName;
        @BindView(R.id.id_coachArea)
        TextView idCoachArea;
        @BindView(R.id.id_coachRen)
        ImageView idCoachRen;
        @BindView(R.id.id_coachYear)
        TextView idCoachYear;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}