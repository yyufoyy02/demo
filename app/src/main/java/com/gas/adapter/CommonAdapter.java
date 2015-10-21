package com.property.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<T> mDatas;
	private final int mItemLayoutId;
	public CommonAdapter(Context context,List<T> mDatas, int itemLayoutId) {
		// TODO Auto-generated constructor stub
	    mInflater = LayoutInflater.from(context);
	    this.mContext = context;
	    this.mDatas = mDatas;
	    this.mItemLayoutId= itemLayoutId;
	}

	public void addItem(T item){
		mDatas.add(item);
		notifyDataSetChanged();
	}

	public void addItems(List<T> items){
		mDatas.addAll(items);
		notifyDataSetChanged();
	}

	public void replaceItems(List<T> items){
		mDatas.clear();
		mDatas.addAll(items);
		notifyDataSetChanged();
	}

	public void removeDate(){
		mDatas.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public T getItem(int arg0) {
		// TODO Auto-generated method stub
		return mDatas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	    final ViewHolder viewHolder = getViewHolder(position, convertView,  
                parent);  
        convert(viewHolder, getItem(position));  
        return viewHolder.getConvertView(); 
	}

	public abstract void convert(ViewHolder helper,T item);
	
	private ViewHolder getViewHolder(int position,View convertView,ViewGroup parent){
		return ViewHolder.get(mContext, convertView, parent,mItemLayoutId , position);
	}
}
