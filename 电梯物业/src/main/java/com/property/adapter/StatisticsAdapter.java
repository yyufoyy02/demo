package com.property.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.property.activity.R;
import com.property.model.StatisticsModel;
import com.vk.simpleutil.adapter.XSimpleAdapter;
import com.vk.simpleutil.adapter.XSimpleViewHolder;
import com.vk.simpleutil.library.XSimpleImage;
import com.vk.simpleutil.library.XSimpleText;

import java.util.List;

/**
 * Created by Administrator on 2015/10/25.
 */
public class StatisticsAdapter extends XSimpleAdapter<StatisticsModel> {
    public StatisticsAdapter(Context context, List<StatisticsModel> mData) {
        super(context, mData, R.layout.statistics_item);
    }

    @Override
    public View convert(View convertView, StatisticsModel item, int position) {
        TextView title = XSimpleViewHolder.get(convertView, R.id.tv_statistics_title);
        ImageView icon = XSimpleViewHolder.get(convertView, R.id.iv_statistics_icon);
        TextView tvOnemonth = XSimpleViewHolder.get(convertView, R.id.tv_statistics_onemonth);
        TextView tvThreemonth = XSimpleViewHolder.get(convertView, R.id.tv_statistics_threemonth);
        TextView tvOneyear = XSimpleViewHolder.get(convertView, R.id.tv_statistics_oneyear);
        XSimpleImage.getInstance().displayImage(item.getIcon().getUrl(), icon);
        title.setText(item.getTitle());
        tvOnemonth.setText(item.getThis_month() + "次");
        tvThreemonth.setText(item.getThree_month() + "次");
        tvOneyear.setText(item.getThis_year() + "次");
        if (!XSimpleText.isEmpty(item.color)) {
            tvOnemonth.setTextColor(Color.parseColor(item.getColor()));
            tvThreemonth.setTextColor(Color.parseColor(item.getColor()));
            tvOneyear.setTextColor(Color.parseColor(item.getColor()));
        }
        return convertView;
    }
}
