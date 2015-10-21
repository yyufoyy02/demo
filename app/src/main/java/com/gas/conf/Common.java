package com.property.conf;

import com.property.entity.User;

/**
 * Created by Heart on 2015/8/1
 * 保存共公变量.
 */
public class Common {
    private static class SingleInstance{
        public static Common INSTANCE = new Common();
    }
    public static Common getInstance(){
        return SingleInstance.INSTANCE;
    }
    public User user = new User();
    /** 服务器与本地的时间差值 ，校正方式为：获取当前时间+这个差值 **/
    public long serverToClientTime = 0;
    public int windowWidth = 0; // 屏幕宽度
    public int windowHeight = 0; // 屏幕高度

    public static int deliveryCount = 0;   //送气未接订单 数量

    public static int deliveryAccept = 0 ;  //送气管理员推送订单数量

    public static int repairCount = 0;   //维修未接订单 数量

    public static int repairAccept = 0 ;  //维修管理员推送订单数量

    public static int order_type = -1;
    public static int must_get = -1;

}
