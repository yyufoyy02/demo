package com.property.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.property.activity.R;
import com.property.model.MaintenanceModel;
import com.vk.simpleutil.adapter.XSimpleRecyclerAdapter;
import com.vk.simpleutil.adapter.XSimpleViewHolder;
import com.vk.simpleutil.library.XSimpleImage;
import com.vk.simpleutil.library.XSimpleResources;
import com.vk.simpleutil.library.XSimpleTime;

import java.util.List;

/**
 * Created by Administrator on 2015/11/15.
 */
public class MaintenancePeriodsAdapter extends XSimpleRecyclerAdapter<MaintenanceModel> {
    public MaintenancePeriodsAdapter(Context context, List<MaintenanceModel> mData) {
        super(context, mData, R.layout.maintenanceperiods_item);
    }

    @Override
    public void convert(View convertView, MaintenanceModel item, int position) {
        TextView status = XSimpleViewHolder.get(convertView, R.id.tv_maintenanceperiods_status);
        ImageView icon = XSimpleViewHolder.get(convertView, R.id.iv_maintenanceperiods_icon);
        TextView name = XSimpleViewHolder.get(convertView, R.id.tv_maintenanceperiods_name);
        TextView submit = XSimpleViewHolder.get(convertView, R.id.tv_maintenanceperiods_submit);
        TextView time = XSimpleViewHolder.get(convertView, R.id.tv_maintenanceperiods_time);
        TextView timeTitle = XSimpleViewHolder.get(convertView, R.id.tv_maintenanceperiods_time_title);
        TextView tvFault = XSimpleViewHolder.get(convertView, R.id.tv_maintenanceperiods_fault);
        TextView tvFaulttype = XSimpleViewHolder.get(convertView, R.id.tv_maintenanceperiods_faulttype);
        tvFault.setText(item.getReg_code());
        tvFaulttype.setText(item.getRule());
        XSimpleImage.getInstance().displayImage("drawable://" + R.drawable.icon_book, icon);
        name.setText(item.getCustomer());
        time.setText(XSimpleTime.getFormatTimeFromTimestamp((long) item.getTime(), "yyyy-MM-dd"));
        if (item.getM_status() == 2) {
            status.setText("已完成");
            submit.setText("查看");
            timeTitle.setText("完成时间");
            submit.setBackgroundResource(R.drawable.corners_blue_radius6);
            status.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
            time.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
            timeTitle.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
            name.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
        } else {
            status.setText("正在进行");
            submit.setText("继续维保");
            timeTitle.setText("维保时间");
            submit.setBackgroundResource(R.drawable.corners_green_radius6);
            status.setTextColor(XSimpleResources.getColor(android.R.color.holo_red_light));
            time.setTextColor(XSimpleResources.getColor(android.R.color.holo_red_light));
            timeTitle.setTextColor(XSimpleResources.getColor(R.color.text_color_333));
            name.setTextColor(XSimpleResources.getColor(R.color.text_color_555));

        }
    }
}
