package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.MapUserPic_Bean;
import com.kunzhuo.xuechelang.model.Video_Bean;
import com.kunzhuo.xuechelang.utils.bitmap.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/12 0012.
 */
public class MapUserPic_Adapter extends MyBaseAdapter<MapUserPic_Bean> {

    private Holder mHolder;

    public MapUserPic_Adapter(Context mContext, List<MapUserPic_Bean> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.pic_item, parent,
                    false);
            mHolder = new Holder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final MapUserPic_Bean bean = mList.get(position);

        // 获取屏幕宽度
//        WindowManager wm = (WindowManager) mContext
//                .getSystemService(Context.WINDOW_SERVICE);
//
//        int screenWidth = wm.getDefaultDisplay().getWidth();
//
//        Bitmap bitmap = BitmapUtil.loadRmoteImage(bean.getHttpImg() + bean.getPicSrc());
//
//        int height = bitmap.getHeight();
//
//        int width = bitmap.getWidth();
//
//        if (width != 0) {
//
//            double height2 = screenWidth * height / width;
//
//            int height3 = (int) height2 + 1;
//
//            ViewGroup.LayoutParams lp = mHolder.idVideo3Img2.getLayoutParams();
//
//            lp.width = screenWidth;
//            // lp.height = LayoutParams.WRAP_CONTENT;
//            lp.height = height3;
//
//            mHolder.idVideo3Img2.setLayoutParams(lp);
//            mHolder.idVideo3Img2.setMaxWidth(screenWidth);
//            mHolder.idVideo3Img2.setMaxHeight(screenWidth * 5);
//        }


        if (!bean.getPicSrc().equals("")) {
            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getPicSrc(), mHolder.idVideo3Img2);
        }

        return convertView;
    }

    class Holder {

        @BindView(R.id.id_video3Img_2)
        ImageView idVideo3Img2;

        public Holder(View v) {
            ButterKnife.bind(this, v);
        }
    }


}