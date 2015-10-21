package com.property.connector.protocol;

import com.property.conf.Config;
import com.property.connector.ConnectorManage;
import com.property.connector.HttpCallBack;
import com.property.entity.User;

import java.util.LinkedHashMap;

/**
 * Created by Heart on 2015/7/31.
 */
public class BusinessHttpProtocol {
    public static long Post(String url,
                            LinkedHashMap<String, Object> map, HttpCallBack callback) {
        return ConnectorManage.getInstance().Post(url, map, callback);
    }


    //打卡
    public static long ClockIn(HttpCallBack callback,String driver_id,String latitude,String longitude,String address,String time,int status){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put( "address" , address);
        entity.put( "time" ,time);
        entity.put( "longitude" , longitude);
        entity.put( "latitude" ,latitude);
        entity.put( "driver_id" ,driver_id);
        entity.put("status",status+"");
        return Post(Config.clockInUrl, entity, callback);
    }

    //获取考勤信息
    public static long checkingInfo(HttpCallBack callback,String id){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",id);
        return Post(Config.check, entity, callback);
    }

    //获取考勤信息
    public static long checkSearch(HttpCallBack callback,String id,int type){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",id);
        entity.put("type",type+"");
        return Post(Config.checkSearch, entity, callback);
    }

    //派送历史订单
    public static  long deliveryHisOrder(HttpCallBack callback,User user,int Id,String status ){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",user.getId()+"");
        entity.put("id",Id+"");
        entity.put("status", status);
        return Post(Config.deliveryHistoryOrder, entity, callback);
    }

    //拒单
    public static long rejectDeliverOrder(HttpCallBack callback,int orderId,int userId){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",userId+"");
        entity.put("id",orderId+"");
        return Post(Config.rejectDeliverOrder, entity, callback);
    }

    //接单
    public static long getDeliverOrder(HttpCallBack callback,int orderId,int userId){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",userId+"");
        entity.put("id",orderId+"");
        return Post(Config.getDeliverOrder, entity, callback);
    }

    public static long finishOrder(HttpCallBack callback,int Id,int orderId){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",Id+"");
        entity.put("id",orderId+"");
        return Post(Config.finishOrder, entity, callback);
    }
    public static long onDeliverOrder(HttpCallBack callback,User user,int Id,String status){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",user.getId()+"");
        entity.put("id",Id+"");
        entity.put("status", status);
        return Post(Config.onDeliverOrder, entity, callback);
    }
    public static long newDeliverOrder(HttpCallBack callback,User user,int Id,String status){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",user.getId()+"");
        entity.put("id",Id+"");
        entity.put("status", status);
        return Post(Config.newDeliverOrder, entity, callback);
    }

    public static long getOrderDetails(HttpCallBack callback, int id){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("id",id+"");
        return Post(Config.deliverOrderlist, entity, callback);
    }


    //派送历史订单
    public static  long repairOrderHistory(HttpCallBack callback,User user,int Id,String status ){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",user.getId()+"");
        entity.put("id",Id+"");
        entity.put("status", status);
        return Post(Config.repairOrderHistory, entity, callback);
    }

    public static long newRepairOrder(HttpCallBack callback,User user,int Id,String status){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",user.getId()+"");
        entity.put("id",Id+"");
        entity.put("status", status);
        return Post(Config.newRepairOrder, entity, callback);
    }
    //派送维修
    public static long onRepairOrder(HttpCallBack callback,User user,int Id,String status){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",user.getId()+"");
        entity.put("id",Id+"");
        entity.put("status", status);
        return Post(Config.onRepairOrder, entity, callback);
    }
    //维修 接单
    public static long getRepairOrder(HttpCallBack callback,int orderId,int userId){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",userId+"");
        entity.put("id",orderId+"");
        return Post(Config.getRepairOrder, entity, callback);
    }
    //拒单
    public static long rejectRepairOrder(HttpCallBack callback,int orderId,int userId){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",userId+"");
        entity.put("id",orderId+"");
        return Post(Config.rejectRepairOrder, entity, callback);
    }
    //满气出库
    public static long gasBottleOut(HttpCallBack callback,int userId,int orderId,String code){

        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",userId+"");
        entity.put("order_id",orderId+"");
        entity.put("code",code);
        return Post(Config.gasBottleOut, entity, callback);
    }


  //空气入库
    public static long gasBottleIn(HttpCallBack callback,int userId,String code){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",userId+"");
        entity.put("code",code+"");
        return Post(Config.gasBottleIn, entity, callback);
    }

    //完成抢修
    public static long finishRepariOrder(HttpCallBack callback,int userId,int orderId,String total_cost){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",userId+"");
        entity.put("id",orderId+"");
        entity.put("total_cost",total_cost);
        return Post(Config.finishRepariOrder, entity, callback);
    }


    //煤气瓶信息查询
    public static long searchBottle(HttpCallBack callback,String code){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("code",code);
        return Post(Config.searchBottle, entity, callback);
    }
    //煤气瓶操作日志
    public static long searchBottleLog(HttpCallBack callback,String code){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("code",code);
        return Post(Config.bottleLog, entity, callback);
    }

    //满气入库
    public static long  bottleFullIn(HttpCallBack callback,String code,String user_id){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("code",code);
        entity.put("driver_id",user_id);
        return Post(Config.bottleFullin, entity, callback);
    }















    public static long getCar(HttpCallBack callback,String driver_id,String depot_id){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",driver_id+"");
        entity.put("depot_id",depot_id+"");
        return Post(Config.getCar, entity, callback);
    }



   public static long wrapCar(HttpCallBack callback,String driver_id,String car_id){
       LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
       entity.put("driver_id",driver_id+"");
       entity.put("car_id",car_id+"");
       return Post(Config.wrapCar, entity, callback);
   }

    public static long unwrapCar(HttpCallBack callback,String driver_id){
        LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",driver_id+"");
        return Post(Config.unwrapCar, entity, callback);
    }

    public static long siteCar(HttpCallBack callback,String car_id,int lat,int lng){
        LinkedHashMap< String , Object > entity  = new LinkedHashMap< String , Object >( );
        entity.put("car_id",car_id+"");
        entity.put("lat",lat/1E6+"");
        entity.put("lng",lng/1E6+"");
        return Post(Config.siteCar, entity, callback);
    }

    public static long carOil(HttpCallBack callback,int driver_id,double oil,double oil_cost,long add_date){
        LinkedHashMap< String , Object > entity  = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",driver_id+"");
        entity.put("oil",String.format("%.2f", oil));
        entity.put("oil_cost",String.format("%.2f", oil_cost));
        entity.put("add_date",add_date+"");
        return Post(Config.carOil, entity, callback);
    }

    public static long oilLog(HttpCallBack callback,int driver_id){
        LinkedHashMap< String , Object > entity  = new LinkedHashMap< String , Object >( );
        entity.put("driver_id",driver_id+"");
        return Post(Config.oilLog, entity, callback);
    }
}
