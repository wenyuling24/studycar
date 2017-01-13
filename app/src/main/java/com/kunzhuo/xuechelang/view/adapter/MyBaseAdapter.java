package com.kunzhuo.xuechelang.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author zhangQ
 * @date 2015-12-17
 * @description listadapter封装父类
 * */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> mList;
	protected LayoutInflater mInflater;

	public MyBaseAdapter(Context mContext, List<T> mList) {
		this.mContext = mContext;
		this.mList = mList;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setData(List<T> mList) {
		this.mList = mList;
	}

	public void addData(List<T> mList) {
		this.mList.addAll(mList);
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
