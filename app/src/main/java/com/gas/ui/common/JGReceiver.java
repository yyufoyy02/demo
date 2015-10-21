package com.property.ui.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.property.conf.Common;
import com.property.epiboly.MainActivity;
import com.property.epiboly.R;
import com.property.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Heart on 2015/7/19.
 */
public class JGReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private NotificationManager manager;
    private int notificationId = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == manager) {
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        Utils.log(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Utils.log(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Utils.log(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context);
            addNotification(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Utils.log(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Utils.log(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            //processCustomMessage(context, bundle);
            processCustomMessage(context);
            addNotification(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Utils.log(TAG, "[MyReceiver] 用户点击打开了通知");

//            //打开自定义的Activity
//            Intent i = new Intent(context, MainActivity.class);
//            i.putExtras(bundle);
//            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Utils.log(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Utils.log(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context) {
        if (MainActivity.isForeground) {
            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainActivity.KEY_MESSAGE, true);
            context.sendBroadcast(msgIntent);
        }
    }

    private void addNotification(Context mContext, Bundle bundle) {
        //manager.cancel(0);

        /**
         * order_type 1为送气；2为抢修； must_get 1为系统派送（不可拒绝）；0反之
         */
        try {
            JSONObject jsontemp = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));

            JSONObject json = new JSONObject(jsontemp.getString("txt"));
            int order_type = json.optInt("order_type");
            int must_get=json.optInt("must_get");

            Notification notification = new Notification();
            notification.icon = R.drawable.indicator_arrow;
            notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.sound = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.sound); ;
            Intent intent = new Intent(mContext, MainActivity.class);
            Bundle bundle1 = new Bundle();
            intent.putExtra("text","s");
            intent.putExtra("order_type",order_type);
            intent.putExtra("must_get",must_get);

            bundle1.putInt("order_type", order_type);
            bundle1.putInt("must_get", must_get);
            intent.putExtras(bundle1);


            StringBuffer strBuffer = new StringBuffer();
            strBuffer.append("您有");
            if(order_type == 1){
                if(must_get == 1){
                    Common.deliveryAccept =Common.deliveryAccept+1;
                    strBuffer.append(Common.deliveryAccept +"条管理员指派送气订单");
                    notificationId =1;
                }else{
                    Common.deliveryCount =Common.deliveryCount+1;
                    strBuffer.append(Common.deliveryCount +"条未接送气订单");notificationId =2;
                }
            }else if(order_type ==2){
                if(must_get == 1){
                    Common.repairAccept  =Common.repairAccept+1;

                    strBuffer.append(Common.repairAccept +"条管理员指派维修订单");
                    notificationId =3;
                }else{
                    Common.repairCount =
                            Common.repairCount+1;
                    strBuffer.append(Common.repairCount +"条未接维修订单");
                    notificationId =4;
                }
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setLatestEventInfo(mContext, "林田燃气", strBuffer.toString(), pendingIntent);
            manager.cancel(notificationId);
            manager.notify(notificationId, notification);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  manager.notify(1,notification);
    }
}
