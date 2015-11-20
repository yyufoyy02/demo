package com.property.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.property.activity.R;
import com.property.model.PlanModel;
import com.vk.simpleutil.adapter.XSimpleRecyclerAdapter;
import com.vk.simpleutil.adapter.XSimpleViewHolder;
import com.vk.simpleutil.library.XSimpleImage;
import com.vk.simpleutil.library.XSimpleResources;
import com.vk.simpleutil.library.XSimpleText;
import com.vk.simpleutil.library.XSimpleTime;

import java.util.List;

public class PlanAdapter extends XSimpleRecyclerAdapter<PlanModel> {
    public PlanAdapter(Context context, List<PlanModel> mData) {
        super(context, mData, R.layout.plan_item);
    }

    int postions = -1;

    public int getScanPostion() {
        return postions;
    }

    @Override
    public void convert(View convertView, final PlanModel item, final int position) {
        TextView status = XSimpleViewHolder.get(convertView, R.id.tv_planitem_status);
        TextView address = XSimpleViewHolder.get(convertView, R.id.tv_planitem_address);
        ImageView icon = XSimpleViewHolder.get(convertView, R.id.iv_planitem_icon);
        TextView name = XSimpleViewHolder.get(convertView, R.id.tv_planitem_name);
        TextView time = XSimpleViewHolder.get(convertView, R.id.tv_planitem_time);
        TextView timeTitle = XSimpleViewHolder.get(convertView, R.id.tv_planitem_time_title);
        TextView tvPeriods = XSimpleViewHolder.get(convertView, R.id.tv_planitem_periods);
        TextView tvPeriodsNums = XSimpleViewHolder.get(convertView, R.id.tv_planitem_periods_nums);
        address.setText("地址：" + item.getCustomer_address());
        XSimpleImage.getInstance().displayImage("drawable://" + R.drawable.icon_book, icon);
        name.setText(item.getCustomer_name());
        time.setText(XSimpleTime.getFormatTimeFromTimestamp((long) item.getPlan_time(), "yyyy-MM-dd"));
        tvPeriods.setTextColor(XSimpleResources.getColor(R.color.text_color_555));
        tvPeriods.setText(XSimpleText.setColorText(item.getPlan_name(), 2,
                item.getPlan_name().length(), XSimpleResources.getColor(android.R.color.holo_red_light)));
        tvPeriodsNums.setText(item.getOk_count() + "/" + item.getLifts_count());
        timeTitle.setText("计划时间");
        if (item.getStatus() == 2) {
            status.setText("已完成");
            status.setTextColor(XSimpleResources.getColor(android.R.color.holo_blue_light));
            time.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
            timeTitle.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
            name.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
            tvPeriods.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
        } else {
            status.setText("正在进行");
            status.setTextColor(XSimpleResources.getColor(android.R.color.holo_red_light));
            time.setTextColor(XSimpleResources.getColor(android.R.color.holo_red_light));
            timeTitle.setTextColor(XSimpleResources.getColor(R.color.text_color_333));
            name.setTextColor(XSimpleResources.getColor(R.color.text_color_555));
        }


    }
}
