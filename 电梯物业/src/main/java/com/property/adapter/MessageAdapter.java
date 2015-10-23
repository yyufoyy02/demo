package com.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.property.activity.MessageActivity;
import com.property.activity.R;
import com.property.model.MessageModel;
import com.property.ui.codeScan.CaptureActivity;
import com.vk.simpleutil.adapter.XSimpleAdapter;
import com.vk.simpleutil.adapter.XSimpleViewHolder;
import com.vk.simpleutil.library.XSimpleImage;
import com.vk.simpleutil.library.XSimpleResources;
import com.vk.simpleutil.library.XSimpleTime;

import java.util.List;

public class MessageAdapter extends XSimpleAdapter<MessageModel> {
    public MessageAdapter(Context context, List<MessageModel> mData) {
        super(context, mData, R.layout.message_item);
    }

    @Override
    public View convert(View convertView, MessageModel item, int position) {
        TextView status = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_status);
        TextView address = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_address);
        ImageView icon = XSimpleViewHolder.get(convertView, R.id.iv_messageitem_icon);
        TextView name = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_name);
        TextView submit = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_submit);
        TextView time = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_time);
        TextView timeTitle = XSimpleViewHolder.get(convertView, R.id.tv_messageitem_time_title);

        address.setText("地址：" + item.getAddress());
        XSimpleImage.getInstance().displayImage(item.getIcon().getUrl(), icon);
        name.setText(item.getName());
        time.setText(XSimpleTime.getFormatTimeFromTimestamp((long) item.getTime(), "yyyy-MM-dd"));
        if (item.getMessageType() == 0) {
            submit.setText("抢修");
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CaptureActivity.launchActivity((Activity) mContext, MessageActivity.REQUEST_CODE_SCANLE);
                }
            });
            timeTitle.setText("抢修");
        } else if (item.getMessageType() == 1) {
            submit.setText("维保");
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CaptureActivity.launchActivity((Activity) mContext, MessageActivity.REQUEST_CODE_SCANLE);
                }
            });
            timeTitle.setText("维保时间");
        }

        if (item.getStatus() == 0) {
            status.setText("等待处理");
            status.setTextColor(XSimpleResources.getColor(android.R.color.holo_red_light));
            submit.setVisibility(View.VISIBLE);
            time.setTextColor(XSimpleResources.getColor(android.R.color.holo_red_light));
            timeTitle.setTextColor(XSimpleResources.getColor(R.color.text_color_333));
        } else if (item.getStatus() == 1) {
            status.setText("已完成");
            status.setTextColor(XSimpleResources.getColor(android.R.color.holo_blue_light));
            submit.setVisibility(View.GONE);
            time.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
            timeTitle.setTextColor(XSimpleResources.getColor(R.color.text_color_999));
        }
        return convertView;
    }
}
