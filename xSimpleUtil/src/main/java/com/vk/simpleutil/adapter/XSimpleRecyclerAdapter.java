package com.vk.simpleutil.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.vk.simpleutil.adapter.recycler.RecyclerArrayAdapter;
import com.vk.simpleutil.library.R;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mcxiaoke
 * Date: 15-06-16
 * Time: 下午4:51
 */
public abstract class XSimpleRecyclerAdapter<T> extends RecyclerArrayAdapter implements View.OnClickListener, View.OnLongClickListener {

    public abstract void convert(View convertView, T item, int position);

    private static final int HEADER_VIEW_TYPE = -1000;
    private static final int FOOTER_VIEW_TYPE = -2000;


    private final List<View> mHeaders = new ArrayList<>();
    private final List<View> mFooters = new ArrayList<>();
    protected final int mItemLayoutId;
    OnItemClickListener mItemClickListener;
    OnItemLongClickListener mItemLongClickListener;

    public XSimpleRecyclerAdapter(Context context, List<T> mData, int itemLayoutId) {
        super(context, mData);
        this.mItemLayoutId = itemLayoutId;
    }


    /**
     * Adds a header view.
     */
    public void addHeader(@NonNull View view) {
        if (view == null) {
            throw new IllegalArgumentException("You can't have a null header!");
        }
        mHeaders.add(view);
    }

    /**
     * Adds a footer view.
     */
    public void addFooter(@NonNull View view) {
        if (view == null) {
            throw new IllegalArgumentException("You can't have a null footer!");
        }
        mFooters.add(view);
    }

    /**
     * Toggles the visibility of the header views.
     */
    public void setHeaderVisibility(boolean shouldShow) {
        for (View header : mHeaders) {
            header.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Toggles the visibility of the footer views.
     */
    public void setFooterVisibility(boolean shouldShow) {
        for (View footer : mFooters) {
            footer.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * @return the number of headers.
     */
    public int getHeaderCount() {
        return mHeaders.size();
    }

    /**
     * @return the number of footers.
     */
    public int getFooterCount() {
        return mFooters.size();
    }

    /**
     * Gets the indicated header, or null if it doesn't exist.
     */
    public View getHeader(int i) {
        return i < mHeaders.size() ? mHeaders.get(i) : null;
    }

    /**
     * Gets the indicated footer, or null if it doesn't exist.
     */
    public View getFooter(int i) {
        return i < mFooters.size() ? mFooters.get(i) : null;
    }

    private boolean isHeader(int viewType) {
        return viewType >= HEADER_VIEW_TYPE && viewType < (HEADER_VIEW_TYPE + mHeaders.size());
    }

    private boolean isFooter(int viewType) {
        return viewType >= FOOTER_VIEW_TYPE && viewType < (FOOTER_VIEW_TYPE + mFooters.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (isHeader(viewType)) {
            int whichHeader = Math.abs(viewType - HEADER_VIEW_TYPE);
            View headerView = mHeaders.get(whichHeader);
            return new RecyclerView.ViewHolder(headerView) {
            };
        } else if (isFooter(viewType)) {
            int whichFooter = Math.abs(viewType - FOOTER_VIEW_TYPE);
            View footerView = mFooters.get(whichFooter);
            return new RecyclerView.ViewHolder(footerView) {
            };

        } else {
            if (onCreateSubViewHolder(viewGroup, viewType) != null)
                return onCreateSubViewHolder(viewGroup, viewType);
            return new MyHolder(getInflater().inflate(mItemLayoutId, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < mHeaders.size()) {
            // Headers don't need anything special

        } else if (position < mHeaders.size() + super.getItemCount()) {
            // This is a real position, not a header or footer. Bind it.
            holder.itemView.setTag(R.string.recycler_key, position - mHeaders.size());
            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);
            if (getItemSubViewType(position - mHeaders.size()) != 0) {
                onBindSubViewHolder(holder.itemView, (T) getItem(position - mHeaders.size()), position - mHeaders.size());
            } else {
                convert(holder.itemView, (T) getItem(position - mHeaders.size()), position - mHeaders.size());
            }
        } else {
            // Footers don't need anything special
        }
    }

    @Override
    public int getItemCount() {
        return mHeaders.size() + super.getItemCount() + mFooters.size();
    }

    public int getItemSubViewType(int position) {
        return 0;
    }

    public RecyclerView.ViewHolder onCreateSubViewHolder(ViewGroup viewGroup, int viewType) {
        return null;
    }

    public void onBindSubViewHolder(View convertView, T item, int position) {
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mHeaders.size()) {
            return HEADER_VIEW_TYPE + position;

        } else if (position < (mHeaders.size() + super.getItemCount())) {
            if (getItemSubViewType(position - mHeaders.size()) != 0)
                return getItemSubViewType(position - mHeaders.size());
            return super.getItemViewType(position - mHeaders.size());
        } else {
            return FOOTER_VIEW_TYPE + position - mHeaders.size() - super.getItemCount();
        }
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null)
            mItemClickListener.onItemClick(v, (Integer) v.getTag(R.string.recycler_key));

    }

    @Override
    public boolean onLongClick(View v) {
        if (mItemLongClickListener != null)
            return mItemLongClickListener.onItemLongClick(v, (Integer) v.getTag(R.string.recycler_key));
        return false;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }
}

