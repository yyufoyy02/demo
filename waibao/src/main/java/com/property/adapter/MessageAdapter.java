package com.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.property.activity.DetailCompleteActivity;
import com.property.activity.MessageActivity;
import com.property.activity.R;
import com.property.model.MessageModel;
import com.property.ui.codeScan.CaptureActivity;
import com.vk.simpleutil.adapter.XSimpleRecyclerAdapter;
import com.vk.simpleutil.adapter.XSimpleViewHolder;
import com.vk.simpleutil.library.XSimpleImage;
import com.vk.simpleutil.library.XSimpleResources;
import com.vk.simpleutil.library.XSimpleText;
import com.vk.simpleutil.library.XSimpleTime;

import java.util.List;

public class MessageAdapter extends XSimpleRecyclerAdapter<MessageModel> {
    public MessageAdapter(Context context, List<MessageModel> mData) {
        super(context, mData, R.layout.message_item);
    }

    int postions = -1;

    public int getScanPostion() {
        return postions;
    }

    @Override
    public void convert(View convertView, final MessageModel item, final int position) {
        TextView status = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_status);
        TextView address = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_address);
        ImageView icon = XSimpleViewHolder.get(convertView, R.id.iv_messageitem_icon);
        TextView name = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_name);
        TextView submit = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_submit);
        TextView time = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_time);
        TextView timeTitle = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_time_title);
        TextView tvPeriods = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_periods);
        TextView tvPeriodsNums = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_periods_nums);

        address.setText("地址：" + item.getAddress());
        XSimpleImage.getInstance().displayImage(item.getIcon().getUrl(), icon);
        name.setText(item.getName());
        time.setText(XSimpleTime.getFormatTimeFromTimestamp((long) item.getTime(), "yyyy-MM-dd"));

        if (item.getMessage_type() == 0) {

            tvPeriods.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
            tvPeriodsNums.setVisibility(View.GONE);


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
                        getContext().startActivity(new Intent(getContext(), DetailCompleteActivity.class).putExtra("id", item.getFault_id()));
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
        } else if (item.getMessage_type() == 1) {
            submit.setText("维保");
            tvPeriods.setVisibility(View.VISIBLE);
            tvPeriods.setTextColor(XSimpleResources.getColor(R.color.text_color_555));

            tvPeriodsNums.setVisibility(View.VISIBLE);
            tvPeriodsNums.setText(item.getPeriods() + "/" + item.getMax_periods());
            submit.setVisibility(View.GONE);
//            submit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    postions = position;
//                    CaptureActivity.launchActivity((Activity) getContext(), MessageActivity.REQUEST_CODE_SCANLE);
//                }
//            });
            timeTitle.setText("维保时间");
            if (item.getStatus() == 2) {
                status.setText("维保完成");
                status.setTextColor(XSimpleResources.getColor(android.R.color.holo_blue_light));
                submit.setVisibility(View.GONE);
                time.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
                timeTitle.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
                name.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
                tvPeriods.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
                tvPeriods.setText("(第" + item.getPeriods() + "期维保计划)");
            } else {
                if (item.getStatus() == 0) {
                    status.setText("待维保");
                } else if (item.getStatus() == 1) {
                    status.setText("维保中");
                } else if (item.getStatus() == 3) {
                    status.setText("过期");
                }
                tvPeriods.setText(XSimpleText.setColorText("(第" + item.getPeriods() + "期维保计划)", 2,
                        (item.getPeriods()+"").length(), android.R.color.holo_red_light));
                status.setTextColor(XSimpleResources.getColor(android.R.color.holo_red_light));
                submit.setVisibility(View.VISIBLE);
                time.setTextColor(XSimpleResources.getColor(android.R.color.holo_red_light));
                timeTitle.setTextColor(XSimpleResources.getColor(R.color.text_color_333));
                name.setTextColor(XSimpleResources.getColor(R.color.text_color_555));
            }
        }


    }
}
