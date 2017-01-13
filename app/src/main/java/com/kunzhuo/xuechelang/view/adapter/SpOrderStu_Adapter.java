package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.SpOrdersStu_Bean;
import com.kunzhuo.xuechelang.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/7 0007.
 */
public class SpOrderStu_Adapter extends MyBaseAdapter<SpOrdersStu_Bean> {

    private Holder mHolder;
    private Context context;

    public SpOrderStu_Adapter(Context mContext, List<SpOrdersStu_Bean> mList) {
        super(mContext, mList);
        this.context = mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sporder_item, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final SpOrdersStu_Bean bean = mList.get(position);

        mHolder.idSpOrderBtn.setVisibility(View.GONE);
        mHolder.idSpOrderRelayout.setVisibility(View.VISIBLE);

        mHolder.idSpOrderMoney.setText("￥ " + bean.getProMoney());
        mHolder.idSpOrderTime.setText(bean.getLearnTime());
        mHolder.idDpOrderTitle.setText("陪练名称: " + bean.getProName());
        mHolder.idDpOrderType.setText("有无证件: " + bean.getProType());
        mHolder.idDpOrderPeiPlace.setText("陪练地点: " + bean.getOrdAddress());
        if (!bean.getMg_address().equals("")) {
            mHolder.idDpOrderJiePlace.setText("接送地点: " + bean.getMg_address());
        }
        mHolder.idDpOrderName.setText("联 系 人: " + bean.getUname());
        mHolder.idDpOrderPhone.setText("联系电话: " + bean.getUaccount());


        if (bean.getWhether() == 1) {

            mHolder.idSpOrderImg.setImageResource(R.drawable.icon_true);
            mHolder.idSpOrdeFalse.setText("预约成功");

        } else if (bean.getWhether() == 0) {

            mHolder.idSpOrderImg.setImageResource(R.drawable.icon_error);
            mHolder.idSpOrdeFalse.setText("预约失效");

        }


        return convertView;
    }

    class Holder {

        @BindView(R.id.id_spOrderMoney)
        TextView idSpOrderMoney;
        @BindView(R.id.id_spOrderTime)
        TextView idSpOrderTime;
        @BindView(R.id.id_dpOrderTitle)
        TextView idDpOrderTitle;
        @BindView(R.id.id_dpOrderType)
        TextView idDpOrderType;
        @BindView(R.id.id_dpOrderPeiPlace)
        TextView idDpOrderPeiPlace;
        @BindView(R.id.id_dpOrderJiePlace)
        TextView idDpOrderJiePlace;
        @BindView(R.id.id_dpOrderName)
        TextView idDpOrderName;
        @BindView(R.id.id_dpOrderPhone)
        TextView idDpOrderPhone;
        @BindView(R.id.id_spOrderImg)
        ImageView idSpOrderImg;
        @BindView(R.id.id_spOrderBtn)
        TextView idSpOrderBtn;
        @BindView(R.id.id_spOrdeFalse)
        TextView idSpOrdeFalse;
        @BindView(R.id.id_spOrderRelayout)
        RelativeLayout idSpOrderRelayout;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}