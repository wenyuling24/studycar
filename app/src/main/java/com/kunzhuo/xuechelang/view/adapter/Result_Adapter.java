package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.Answer_Bean;
import com.kunzhuo.xuechelang.model.TitleList_KM_Bean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kunzhuo.xuechelang.R.drawable.pass;

/**
 * Created by waaa on 2016/9/2.
 * 四个答案adapter
 */
public class Result_Adapter extends MyBaseAdapter<Answer_Bean> {

    private Holder mHolder;
    private TitleList_KM_Bean title_Bean;
    private Context mContext;
    private Answer_Bean bean;

    public Result_Adapter(Context mContext, List<Answer_Bean> mList) {
        super(mContext, mList);
    }

    public Result_Adapter(Context mContext, List<Answer_Bean> mList, TitleList_KM_Bean title_Bean) {
        super(mContext, mList);
        this.title_Bean = title_Bean;
        this.mContext = mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.result_item, parent, false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        bean  = mList.get(position);

        mHolder.idResultChoose.setText(bean.getA_option());
        mHolder.idResultChoose.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        mHolder.idResultMsg.setText(bean.getA_value());

        return convertView;
    }

    class Holder {

        @BindView(R.id.id_resultlay)
        RelativeLayout idResultlay;
        @BindView(R.id.id_resultChoose)
        TextView idResultChoose;
        @BindView(R.id.id_judgeflag)
        ImageView idJudgeflag;
        @BindView(R.id.id_resultMsg)
        TextView idResultMsg;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
