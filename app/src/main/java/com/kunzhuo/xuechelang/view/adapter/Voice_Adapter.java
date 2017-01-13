package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.VoiceMoni_Bean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public class Voice_Adapter extends MyBaseAdapter<VoiceMoni_Bean> {

    private Holder mHolder;

    public Voice_Adapter(Context mContext, List<VoiceMoni_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.voice_item, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final VoiceMoni_Bean bean = mList.get(position);

        String VoiceImg = "http://xueche555.com/Toolkit/Images/Voice/"; // 声音图片文件前缀
        String VoiceMp3 = "http://xueche555.com/Toolkit/Voice/"; // 声音文件前缀

        if (!bean.getVoiceImg().equals("")) {

            ImageLoader.getInstance().displayImage(bean.getVoiceImg(), mHolder.idVoiceImg);
        }

        return convertView;
    }

    class Holder {

        @BindView(R.id.id_voiceImg)
        ImageView idVoiceImg;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}