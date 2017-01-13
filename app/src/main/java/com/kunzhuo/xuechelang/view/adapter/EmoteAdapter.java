package com.kunzhuo.xuechelang.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kunzhuo.xuechelang.AndroidApplication;
import com.kunzhuo.xuechelang.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 表情Adapter
 *
 * @author Administrator
 */
public class EmoteAdapter extends BaseArrayListAdapter {

    public static final int APP_PAGE_SIZE = 27;
    List<String> datas;
    Context context;
    private List<String> mList;
    int page;

    public EmoteAdapter(Context context, List<String> datas, int page) {
        super(context, datas);
        this.context = context;
        this.datas = datas;
        this.page = page;
        mList = new ArrayList<String>();
        int i = page * APP_PAGE_SIZE;
        int iEnd = i + APP_PAGE_SIZE;
        while ((i < datas.size()) && (i < iEnd)) {
            mList.add(datas.get(i));
            i++;
            if (i == 27) {
                mList.add(27, "删除");
            }
            if (i == 54) {
                mList.add(27, "删除");
            }
            if (i == 81) {
                mList.add(27, "删除");
            }
            if (i == 108) {
                mList.add("删除");
            }
        }
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_emote, null);
            holder = new ViewHolder();
            holder.mIvImage = (ImageView) convertView
                    .findViewById(R.id.emote_item_iv_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 27) {
            holder.mIvImage.setImageResource(R.drawable.icon_imgdelete);

        } else if (page == 3 && position == 19) {
            holder.mIvImage.setImageResource(R.drawable.icon_imgdelete);
        } else {
            String name = (String) getItem(position);
            int id = AndroidApplication.mEmoticonsId.get(name);

            holder.mIvImage.setImageResource(id);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView mIvImage;
    }
}