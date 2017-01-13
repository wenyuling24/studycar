package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.MockRecord_Bean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by waaa on 2016/9/19.
 */
public class MockRecord_Adapter extends MyBaseAdapter<MockRecord_Bean> {


    private Holder mHolder;

    public MockRecord_Adapter(Context mContext, List<MockRecord_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.record_item, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final MockRecord_Bean bean = mList.get(position);
        mHolder.recordNum.setText(bean.getFraction() + "");
        mHolder.recordUpTime.setText(bean.getRanDate());

        int Time = bean.getExamTime();

        if (Time / 60 < 10) { // XX:XX
            if (Time % 60 < 10) { // XX:0X
                mHolder.recordTime.setText("0" + Time / 60 + ":" + "0" + Time % 60);
            } else {
                mHolder.recordTime.setText("0" + Time / 60 + ":" + Time % 60);
            }
        } else {
            if (Time % 60 < 10) { // XX:0X
                mHolder.recordTime.setText(Time / 60 + ":" + "0" + Time % 60);
            } else {
                mHolder.recordTime.setText(Time / 60 + ":" + Time % 60);
            }
        }


        return convertView;
    }

    class Holder {

        @BindView(R.id.record_Num)
        TextView recordNum;
        @BindView(R.id.record_Time)
        TextView recordTime;
        @BindView(R.id.record_UpTime)
        TextView recordUpTime;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}
