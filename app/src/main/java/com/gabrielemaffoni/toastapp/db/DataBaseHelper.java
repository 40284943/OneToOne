package com.gabrielemaffoni.toastapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 *
 * Inspired by: androidopentutorials.com/android-sqlite-join-multiple-tables-examples/
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String USERS_TABLE = "users";
    public static final String EVENTS_TABLE = "events";
//USERS TABLE KEYS
    public static final String U_ID_COL = "id";
    public static final String U_NAME_COL = "name";
    public static final String U_SURNAME_COL = "surname";
    public static final String U_PROFILE_PIC_COL = "image";
    //EVENT TABLE KEYS
    public static final String E_USER_INVITED_ID_COL = "id_invited";
    public static final String E_EVENT_ID_COL = "id_event";
    public static final String E_WHEN_COL = "when";
    public static final String E_HOUR_COL = "hour";
    public static final String E_MINUTE_COL = "minute";
    public static final String E_LOCATION_COL = "location";
    public static final String E_ACTIVE_COL = "active";
    public static final String E_TYPE_COL = "type";
    public static final String CREATE_USERS_TABLE = "CREATE TABLE "
            + USERS_TABLE + "(" + U_ID_COL + " INTEGER PRIMARY KEY, "
            + U_NAME_COL + " TEXT, "
            + U_SURNAME_COL + " TEXT, "
            + U_PROFILE_PIC_COL + " INTEGER" + ")";
    public static final String CREATE_EVENTS_TABLE = "CREATE TABLE "
            + EVENTS_TABLE + "(" + E_EVENT_ID_COL + " INTEGER, "
            + E_WHEN_COL + " DATE, "
            + E_HOUR_COL + " NUMERIC, "
            + E_MINUTE_COL + " NUMERIC, "
            + E_LOCATION_COL + " TEXT, "
            + E_USER_INVITED_ID_COL + " INT, "
            + E_ACTIVE_COL + " INTEGER NOT NULL ON CONFLICT REPLACE DEFAULT 0, "
            + E_TYPE_COL + " INTEGER, "+
            "PRIMARY KEY (" + E_EVENT_ID_COL +", "+ E_ACTIVE_COL +"),"
            +"FOREIGN KEY(" + E_USER_INVITED_ID_COL + ") REFERENCES "+ USERS_TABLE +"(" + U_ID_COL + ") )";
    private static final String DB_NAME = "toastapp";
    private static final int DB_VERSION = 1;
    private static DataBaseHelper instance;

    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DataBaseHelper getHelper(Context context){
        if (instance == null){
           instance = new DataBaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onOpen(SQLiteDatabase db){
     super.onOpen(db);
        if(!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldversion, int newVersion){

    }
}

