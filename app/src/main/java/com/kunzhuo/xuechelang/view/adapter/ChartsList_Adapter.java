package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.ChartsList_Bean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by waaa on 2016/9/20.
 */
public class ChartsList_Adapter extends MyBaseAdapter<ChartsList_Bean> {

    private Holder mHolder;

    public ChartsList_Adapter(Context mContext, List<ChartsList_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.chartslist_item, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final ChartsList_Bean bean = mList.get(position);
        mHolder.idChartsNum.setText(bean.getPxSort() + "");

        if (bean.getWeChat().equals("0")) {
            if (!bean.getPicSrc().equals("")) {
                ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getPicSrc(), mHolder.idChartsImg);
            }
        }
        if (bean.getWeChat().equals("1")) {
            if (!bean.getWx_portrait().equals("")) {
                ImageLoader.getInstance().displayImage(bean.getWx_portrait(), mHolder.idChartsImg);
            }
        }

        String Unickname = bean.getUnickname();
        String Uaccount = bean.getUaccount();

        if (Unickname.equals("")) {
            mHolder.idChartsNick.setText(Uaccount.subSequence(0, 3)
                    + "****" + Uaccount.subSequence(7, 11));
        } else {
            mHolder.idChartsNick.setText(Unickname);
        }

        mHolder.idChartsPoint.setText(bean.getFraction() + " 分");
        mHolder.idChartsDate.setText(bean.getRanDate());

        int Time = bean.getExamTime();
        if (Time % 60 < 10) { // XX:0X
            mHolder.idChartsUseTime.setText(Time / 60 + "分" + "0" + Time % 60 + "秒");
        } else {
            mHolder.idChartsUseTime.setText(Time / 60 + "分" + Time % 60 + "秒");
        }

        return convertView;
    }

    class Holder {

        @BindView(R.id.id_chartsNum)
        TextView idChartsNum;
        @BindView(R.id.id_chartsImg)
        ImageView idChartsImg;
        @BindView(R.id.id_chartsNick)
        TextView idChartsNick;
        @BindView(R.id.id_chartsPoint)
        TextView idChartsPoint;
        @BindView(R.id.id_chartsUseTime)
        TextView idChartsUseTime;
        @BindView(R.id.id_chartsDate)
        TextView idChartsDate;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}