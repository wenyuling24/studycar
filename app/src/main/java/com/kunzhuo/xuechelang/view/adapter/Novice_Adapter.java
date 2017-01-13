package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.NoviceList_Bean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class Novice_Adapter extends MyBaseAdapter<NoviceList_Bean> {

    private Holder mHolder;

    public Novice_Adapter(Context mContext, List<NoviceList_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.novicelist_item, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final NoviceList_Bean bean = mList.get(position);

        mHolder.idNoviceTitle.setText(bean.getItitle());
        mHolder.idNoviceAbs.setText(bean.getAbstract());

        if(!bean.getHead_portrait().equals("")){

            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getHead_portrait(), mHolder.idNoviceImg);
        }

        return convertView;
    }

    class Holder {

        @BindView(R.id.id_NoviceImg)
        ImageView idNoviceImg;
        @BindView(R.id.id_NoviceTitle)
        TextView idNoviceTitle;
        @BindView(R.id.id_NoviceAbs)
        TextView idNoviceAbs;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}
