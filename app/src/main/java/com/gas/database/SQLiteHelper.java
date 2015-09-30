package com.gas.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Heart on 2015/8/8.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "DongZ.db";
    private static SQLiteHelper db;

    private static final int VERSION = 1;
    private static final String TABLE_NAME_USER = "user";

    private SQLiteHelper( Context context )
    {
        super( context , DATABASE_NAME , null , VERSION );

    }

    public static SQLiteHelper getInstance( Context context )
    {
        if ( db == null )
        {
            db = new SQLiteHelper( context );
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(UserWorker.tableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized void close( )
    {
        super.close( );
        db = null;
    }
//    private static class DBOpenHelper extends SQLiteOpenHelper {
//
//        public DBOpenHelper(Context context) {
//            super(context, DATABASE_NAME, null, VERSION);
//        }
//
//        private static void createTable(SQLiteDatabase db) {
//            db.execSQL("CREATE TABLE " + TABLE_NAME_USER + " ( " +
//                            TableColumns.UserColumns._ID + " INTEGER PRIMARY KEY," +
//                            TableColumns.UserColumns.DEPOT_ID + " INTEGER, " +
//                            TableColumns.UserColumns.PHONE + " TEXT, " +
//                            TableColumns.UserColumns.PASSWORD + " TEXT, " +
//                            TableColumns.UserColumns.QQ + " TEXT, " +
//                            TableColumns.UserColumns.AREAID + " TEXT, " +
//                            TableColumns.UserColumns.CARID + " TEXT, " +
//                            TableColumns.UserColumns.IN_TIME + " TEXT, " +
//                            TableColumns.UserColumns.LAST_LOGIN_TIME + " TEXT, " +
//                            TableColumns.UserColumns.CREATE_TIME + " TEXT, " +
//                            TableColumns.UserColumns.UPDATE_TIME + " TEXT);"
//            );
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            onUpgrade(db, 0, VERSION);
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            for (int version = oldVersion + 1; version <= newVersion; version++) {
//                upgradeTo(db, version);
//            }
//        }
//        private void upgradeTo(SQLiteDatabase db, int version) {
//            switch (version) {
//                case 1:
//                    createTable(db);
//                    break;
//                default:
//                    throw new IllegalStateException("Don't know how to upgrade to " + version);
//            }
//        }
//    }
}
