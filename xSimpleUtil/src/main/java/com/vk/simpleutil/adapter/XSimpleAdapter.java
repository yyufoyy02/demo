package com.vk.simpleutil.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class XSimpleAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    public Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;

    public XSimpleAdapter(Context context, List<T> mData, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mData;
        this.mItemLayoutId = itemLayoutId;
        if (mDatas == null)
            mDatas = new ArrayList<T>();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(mItemLayoutId, null);
        }
        if (position == 0) {
            if (getCount() == 1) {
                itemPosition(convertView, PositionType.one);
            } else {
                itemPosition(convertView, PositionType.index);
            }
        } else if (position == getCount() - 1) {
            itemPosition(convertView, PositionType.last);
        } else {
            itemPosition(convertView, PositionType.normal);
        }
        return convert(convertView, getItem(position), position);

    }

    public abstract View convert(View convertView, T item, int position);

    public enum PositionType {
        index, normal, last, one
    }

    public void itemPosition(View convertView, PositionType type) {

    }

}
