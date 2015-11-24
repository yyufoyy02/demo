package com.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.property.activity.MessageActivity;
import com.property.activity.R;
import com.property.activity.fault.FaultDetailCompleteActivity;
import com.property.model.FaultModel;
import com.property.ui.codeScan.CaptureActivity;
import com.vk.simpleutil.adapter.XSimpleRecyclerAdapter;
import com.vk.simpleutil.adapter.XSimpleViewHolder;
import com.vk.simpleutil.library.XSimpleImage;
import com.vk.simpleutil.library.XSimpleResources;
import com.vk.simpleutil.library.XSimpleTime;

import java.util.List;


public class FaultAdapter extends XSimpleRecyclerAdapter<FaultModel> {
    public FaultAdapter(Context context, List<FaultModel> mData) {
        super(context, mData, R.layout.fault_item);
    }

    int postions = -1;

    public int getScanPostion() {
        return postions;
    }

    @Override
    public void convert(View convertView, final FaultModel item, final int position) {
        TextView status = XSimpleViewHolder.get(convertView, R.id.tv_faultitem_status);
        TextView address = XSimpleViewHolder.get(convertView, R.id.tv_faultitem_address);
        ImageView icon = XSimpleViewHolder.get(convertView, R.id.iv_faultitem_icon);
        TextView name = XSimpleViewHolder.get(convertView, R.id.tv_faultitem_name);
        TextView submit = XSimpleViewHolder.get(convertView, R.id.tv_faultitem_submit);
        TextView time = XSimpleViewHolder.get(convertView, R.id.tv_faultitem_time);
        TextView timeTitle = XSimpleViewHolder.get(convertView, R.id.tv_faultitem_time_title);

        address.setText("地址：" + item.getAddress());
        XSimpleImage.getInstance().displayImage("drawable://" + R.drawable.icon_setting, icon);
        name.setText("市政府电梯" + item.getElevetor_number() + "号维保");
        time.setText(XSimpleTime.getFormatTimeFromTimestamp(1444492800, "yyyy-MM-dd"));
        if (item.getStatus() == 4) {
            status.setText("已完成");
            timeTitle.setText("完成时间");
            status.setTextColor(XSimpleResources.getColor(android.R.color.holo_blue_light));
            time.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
            timeTitle.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
            name.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
            submit.setText("查看");
            submit.setBackgroundResource(R.drawable.corners_green_radius6);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postions = position;
                    getContext().startActivity(new Intent(getContext(), FaultDetailCompleteActivity.class).putExtra("id", item.getId()));
                }
            });
        } else {
            if (item.getStatus() == 2) {
                status.setText("等待处理");
            } else {
                status.setText("已经签到正在处理");
            }
            timeTitle.setText("时间");
            status.setTextColor(XSimpleResources.getColor(android.R.color.holo_red_light));
            time.setTextColor(XSimpleResources.getColor(android.R.color.holo_red_light));
            timeTitle.setTextColor(XSimpleResources.getColor(R.color.text_color_333));
            name.setTextColor(XSimpleResources.getColor(R.color.text_color_333));
            submit.setText("抢修");
            submit.setBackgroundResource(R.drawable.corners_blue_radius6);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postions = position;
                    CaptureActivity.launchActivity((Activity) getContext(), MessageActivity.REQUEST_CODE_SCANLE);

                }
            });
        }
    }
}
