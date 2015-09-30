package com.gas.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.gas.entity.User;

/**
 * Created by Heart on 2015/8/8.
 */
public class UserWorker extends ITableWorker {

    public static final String TB_NAME = "user"; // 表名

    public static final String LOC_ID = "loc_id";
    public static final String ID = "id";
    public static final String DEPOT_ID = "depot_id";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";
    public static final String QQ = "qq";
    public static final String AREAID = "areaid";
    public static final String CARID = "carid";
    public static final String IN_TIME = "in_time";
    public static final String LAST_LOGIN_TIME = "last_login_time";
    public static final String CREATE_TIME = "create_time";
    public static final String UPDATE_TIME = "update_time";
    public static final String AREA = "area";
    public static final String DEPOT = "depot";
    public static final String POSITION = "position";
    public static final String ISLOGIN = "isLogin";
    public static final String[] selectors = {
            ID, DEPOT_ID, NAME, PHONE, PASSWORD, QQ, AREAID, CARID, IN_TIME, LAST_LOGIN_TIME, CREATE_TIME,UPDATE_TIME,ISLOGIN,AREA,DEPOT,POSITION
    };
    public static final String deleteAll = "DELETE FROM TB_NAME";
    public static final String tableSql = "CREATE TABLE IF NOT EXISTS " + TB_NAME + " ( " +
            LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ID + " INTEGER," +
            DEPOT_ID + " INTEGER," +
            NAME + " TEXT," +
            PHONE + " TEXT," +
            PASSWORD + " TEXT," +
            QQ + " TEXT," +
            AREA + " TEXT," +
            DEPOT + " TEXT," +
            POSITION + " TEXT," +
            AREAID + " TEXT," +
            CARID + " TEXT," +
            IN_TIME + " TEXT," +
            LAST_LOGIN_TIME + " TEXT," +
            CREATE_TIME + " TEXT," +
            ISLOGIN +" INTEGER,"+
            UPDATE_TIME + " TEXT);";

    public UserWorker(Context context) {
        super( context , LOC_ID , TB_NAME );
    }

    public long addUser(User user){
        ContentValues values = new ContentValues( );
        values.put(ID,user.getId());
        values.put(DEPOT_ID,user.getDepot_id());
        values.put(NAME,user.getName());
        values.put(PHONE,user.getPhone());
        values.put(PASSWORD,user.getPassword());
        values.put(QQ,user.getQq());
        values.put(AREAID,user.getAreaid());
        values.put(CARID,user.getCarid());
        values.put(IN_TIME,user.getIn_time());

        values.put(AREA,user.getArea());
        values.put(DEPOT,user.getDepot());
        values.put(POSITION,user.getPosition());

        values.put(LAST_LOGIN_TIME,user.getLast_login_time());
        values.put(CREATE_TIME,user.getCreate_time());
        values.put(UPDATE_TIME,user.getUpdate_time());
        values.put(ISLOGIN,user.getIsLogin());
        return onInsert(values);
    }
    public User getUser(){
        Cursor cursor = onSelect(selectors,null);
        User user = new User();
        if(cursor.moveToFirst()){
            user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            user.setDepot_id(cursor.getInt(cursor.getColumnIndex(DEPOT_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(PHONE)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
            user.setQq(cursor.getString(cursor.getColumnIndex(QQ)));
            user.setAreaid(cursor.getString(cursor.getColumnIndex(AREAID)));
            user.setCarid(cursor.getString(cursor.getColumnIndex(CARID)));
            user.setIn_time(cursor.getString(cursor.getColumnIndex(IN_TIME)));
            user.setLast_login_time(cursor.getString(cursor.getColumnIndex(LAST_LOGIN_TIME)));
            user.setCreate_time(cursor.getString(cursor.getColumnIndex(CREATE_TIME)));
            user.setUpdate_time(cursor.getString(cursor.getColumnIndex(UPDATE_TIME)));
            user.setIsLogin(cursor.getInt(cursor.getColumnIndex(ISLOGIN)));
            user.setArea(cursor.getString(cursor.getColumnIndex(AREA)));
            user.setDepot(cursor.getString(cursor.getColumnIndex(DEPOT)));
            user.setPosition(cursor.getString(cursor.getColumnIndex(POSITION)));
        }
         return user;
    }
    public int removeAll( long uid )
    {
        String where = ID + " = " + uid;
        return delete( where );
    }
    public void DelUser(int userId){

    }
}
