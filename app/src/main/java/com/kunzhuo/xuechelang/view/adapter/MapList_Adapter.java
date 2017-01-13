package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.CoachShows_Bean;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.view.activity.CoachDetails_Activity;
import com.kunzhuo.xuechelang.view.activity.UpCoachMsg_Activity;
import com.kunzhuo.xuechelang.widget.CustomImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/11 0011.
 */
public class MapList_Adapter extends MyBaseAdapter<CoachShows_Bean> {


    private Holder mHolder;
    private Context context;
    private String openid = "";

    public MapList_Adapter(Context mContext, List<CoachShows_Bean> mList) {
        super(mContext, mList);
        context = mContext;
        openid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.OPEN_ID);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.maplist_item, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final CoachShows_Bean bean = mList.get(position);
        mHolder.idMaplistName.setText(bean.getName());
        mHolder.idMaplistAddress.setText(bean.getSite_address());
        mHolder.idMaplistPhoneNum.setText(bean.getTelephone());
        mHolder.idMaplistDistance.setText(bean.getMapSort() + "km");
        mHolder.idMaplistType.setText(bean.getCoachType());


        if (!bean.getHead_portrait().equals("")) {

            if (bean.getHead_portrait().indexOf("http://wx.qlogo.cn/mmopen") != -1) {

                ImageLoader.getInstance().displayImage(bean.getHead_portrait(), mHolder.idMaplistImg);

            } else
                ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getHead_portrait(), mHolder.idMaplistImg);
        }

        final String phoneNumber = bean.getTelephone();

        if (bean.getOpenid().equals(openid)) {
            mHolder.idMaplistPhone.setImageResource(R.drawable.icon_editcoach);

            mHolder.idMaplistPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, UpCoachMsg_Activity.class);
                    intent.putExtra("LocationType", 0);
                    intent.putExtra("ID", bean.getID());
                    intent.putExtra("Name", bean.getName());
                    context.startActivity(intent);

                }
            });

        } else {

            mHolder.idMaplistPhone.setImageResource(R.drawable.icon_call);

            mHolder.idMaplistPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                            + phoneNumber));

                    context.startActivity(intent);

                }
            });
        }


        mHolder.idMaplistMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, CoachDetails_Activity.class);
                intent.putExtra("ID", bean.getID());
                intent.putExtra("Name", bean.getName());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class Holder {

        @BindView(R.id.id_maplistImg)
        CustomImageView idMaplistImg;
        @BindView(R.id.id_maplistName)
        TextView idMaplistName;
        @BindView(R.id.id_maplistAddress)
        TextView idMaplistAddress;
        @BindView(R.id.id_maplistPhoneNum)
        TextView idMaplistPhoneNum;
        @BindView(R.id.id_maplistMarkerIcon)
        ImageView idMaplistMarkerIcon;
        @BindView(R.id.id_maplistDistance)
        TextView idMaplistDistance;
        @BindView(R.id.id_maplistMoreBtn)
        TextView idMaplistMoreBtn;
        @BindView(R.id.id_maplistPhone)
        ImageView idMaplistPhone;
        @BindView(R.id.id_maplistType)
        TextView idMaplistType;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}
