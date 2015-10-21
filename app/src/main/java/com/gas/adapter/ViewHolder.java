package com.property.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
    private int mPosition;
   private final SparseArray<View> mViews;
   private View mConvertView;
   private ViewHolder(Context context,ViewGroup parent,int layoutId,int position){
	   this.mViews = new SparseArray<View>();  
       mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,  
               false);  
       mConvertView.setTag(this);
       this.mPosition = position;
   }
   public static ViewHolder get(Context context, View convertView,  
           ViewGroup parent, int layoutId, int position){
	   if(convertView == null){
		   return new ViewHolder(context, parent, layoutId, position);
	   }
	   
	   return (ViewHolder)convertView.getTag();
	   
   }
   public <T extends View> T getView(int viewId){
	   View view =mViews.get(viewId);
	   if(view  == null){
		   view = mConvertView.findViewById(viewId);
		   mViews.put(viewId, view);
	   }
	return (T)view;   
   }
   public View getConvertView()  
   {  
       return mConvertView;  
   }
    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text)
    {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId)
    {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }
    public ViewHolder setCircularImgByUrl (int viewId, String url)
    {
        ImageView view = getView(viewId);
       // ImageLoaderOperate.getInstance(mContext).loaderUserImage(PengResUtil.getPicUrlBySimName(url), view);

        return this;
    }
    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bm)
    {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }
    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageById(int viewId, int id)
    {
        ImageView view = getView(viewId);
        view.setBackgroundResource(id);
        return this;
    }
    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageByUrl(int viewId, String url)
    {
        ImageView view = getView(viewId);
       // ImageLoaderOperate.getInstance(mContext).loaderImagenormal(url, view);
        return this;
    }

    public int getPosition()
    {
        return mPosition;
    }
}
