package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.AnswerList_Bean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by waaa on 2016/8/31.
 */
public class Answer_Adapter extends MyBaseAdapter<AnswerList_Bean> {

    private Holder mHolder;

    public Answer_Adapter(Context mContext, List<AnswerList_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.answer_item, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final AnswerList_Bean bean = mList.get(position);

        mHolder.chapterMsg.setText(bean.getTitle());
        mHolder.chapterNum.setText(bean.getZJ_Count() + "");

        return convertView;
    }

    class Holder {

        @BindView(R.id.chapter_Img)
        ImageView chapterImg;
        @BindView(R.id.chapter_Msg)
        TextView chapterMsg;
        @BindView(R.id.chapter_Num)
        TextView chapterNum;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}
