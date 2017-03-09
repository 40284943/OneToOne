package com.gabrielemaffoni.toastapp.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class EventDBDAO {
    protected SQLiteDatabase db;
    private DataBaseHelper dataBaseHelper;
    private Context context;

    public EventDBDAO(Context context){
        this.context = context;
        dataBaseHelper = DataBaseHelper.getHelper(context);
        open();
    }

    public void open() throws SQLException {
        if (dataBaseHelper == null){
            dataBaseHelper = DataBaseHelper.getHelper(context);
        }
        db = dataBaseHelper.getWritableDatabase();
    }

    public void close(){
        dataBaseHelper.close();
        db = null;
    }
}
