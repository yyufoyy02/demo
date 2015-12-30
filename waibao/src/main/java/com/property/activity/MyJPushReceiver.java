package com.property.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.property.BaseApplication;
import com.property.utils.AppRoute;
import com.vk.simpleutil.library.XSimpleLogger;
import com.vk.simpleutil.library.XSimpleText;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class MyJPushReceiver extends BroadcastReceiver {

    private NotificationManager manager;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == manager)
            manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle bundle = intent.getExtras();
        XSimpleLogger.Log().e("JPushID:" + JPushInterface.getRegistrationID(BaseApplication.mContext));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            XSimpleLogger.Log().e("JPush用户注册成功");
            XSimpleLogger.Log().e("JPushID:" + JPushInterface.getRegistrationID(BaseApplication.mContext));

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            XSimpleLogger.Log().e("接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            XSimpleLogger.Log().e("接受到推送下来的通知");
            if (BaseApplication.soundPool != null)
                BaseApplication.soundPool.play(1, 1, 1, 0, 0, 1);
//            addNotification(context, intent);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            XSimpleLogger.Log().e("用户点击打开了通知");

            openNotification(context, bundle);

        } else {
            XSimpleLogger.Log().d("Unhandled intent - " + intent.getAction());
        }
    }


    private void addNotification(Context mContext, Intent intent) {

        if (intent == null || intent.getExtras() == null)
            return;
        Notification notification = new Notification();
        notification.icon = R.mipmap.icon_lt;
        notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.sound = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.sound);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(mContext, intent.getExtras().getString(JPushInterface.EXTRA_TITLE), intent.getExtras().getString(JPushInterface.EXTRA_MESSAGE), pendingIntent);
        manager.cancel(0);
        manager.notify(0, notification);
    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

        String myValue = "";
        try {

            JSONObject extrasJson = new JSONObject(extras);
            XSimpleLogger.Log().e(extrasJson.toString());
            myValue = extrasJson.optString("app_route");
            if (XSimpleText.isEmpty(myValue))
                myValue = extrasJson.optString("txt");
        } catch (Exception e) {
            XSimpleLogger.Log().e("Unexpected: extras is not a valid json", e);
            return;
        }

        AppRoute.setActivityNewTask(true);
        AppRoute.goActivity(context, myValue);
    }

}
