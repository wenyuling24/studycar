package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.MyPeiPrice_Bean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/1 0001.
 * 陪练商品单价adapter
 */
public class MyPeiPrice_Adapter extends MyBaseAdapter<MyPeiPrice_Bean> {


    private Holder mHolder;

    public MyPeiPrice_Adapter(Context mContext, List<MyPeiPrice_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mypei_item, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final MyPeiPrice_Bean bean = mList.get(position);

        mHolder.typeKe1.setText(bean.getPtitle());

        return convertView;
    }

    class Holder {

        @BindView(R.id.type_Ke1)
        TextView typeKe1;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}