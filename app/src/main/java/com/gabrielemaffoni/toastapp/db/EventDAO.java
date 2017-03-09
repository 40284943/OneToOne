package com.gabrielemaffoni.toastapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.gabrielemaffoni.toastapp.Event;
import com.gabrielemaffoni.toastapp.to.EventObj;
import com.gabrielemaffoni.toastapp.to.Friend;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class EventDAO extends EventDBDAO {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private static final String WHERE_ID_EQUALS = DataBaseHelper.E_EVENT_ID_COL + " =?";

    public EventDAO(Context context){
       super(context);
   }


   public long save(EventObj event){
       ContentValues values = new ContentValues();
       values.put(DataBaseHelper.E_USER_INVITED_ID_COL, event.getReceiver().getUserId());
       values.put(DataBaseHelper.E_WHEN_COL, formatter.format(event.getWhen()));
       values.put(DataBaseHelper.E_HOUR_COL, event.getHour());
       values.put(DataBaseHelper.E_MINUTE_COL, event.getMinute());
       values.put(DataBaseHelper.E_LOCATION_COL, event.getLocation());
       values.put(DataBaseHelper.E_ACTIVE_COL, event.getActive());
       values.put(DataBaseHelper.E_TYPE_COL, event.getType());

       return db.insert(DataBaseHelper.EVENTS_TABLE,null,values);
   }


    public long update(EventObj event){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.E_USER_INVITED_ID_COL, event.getReceiver().getUserId());
        values.put(DataBaseHelper.E_WHEN_COL, formatter.format(event.getWhen()));
        values.put(DataBaseHelper.E_HOUR_COL, event.getHour());
        values.put(DataBaseHelper.E_MINUTE_COL, event.getMinute());
        values.put(DataBaseHelper.E_LOCATION_COL, event.getLocation());
        values.put(DataBaseHelper.E_ACTIVE_COL, event.getActive());
        values.put(DataBaseHelper.E_TYPE_COL, event.getType());

        long result = db.update(DataBaseHelper.EVENTS_TABLE,values, WHERE_ID_EQUALS, new String[] {String.valueOf(event.getId_event())});
        Log.d("Update Result:","="+result);
        return result;
    }


    public int deleteEvent(EventObj event){
        return db.delete(DataBaseHelper.EVENTS_TABLE,WHERE_ID_EQUALS,new String[]{event.getId_event()+ ""});
    }

    public EventObj getActiveEvent(){
        EventObj eventObj = new EventObj();
        Cursor c = db.query(DataBaseHelper.EVENTS_TABLE, new String[]{
                DataBaseHelper.E_EVENT_ID_COL,
                DataBaseHelper.E_USER_INVITED_ID_COL,
                DataBaseHelper.E_WHEN_COL,
                DataBaseHelper.E_HOUR_COL,
                DataBaseHelper.E_MINUTE_COL,
                DataBaseHelper.E_LOCATION_COL,
                DataBaseHelper.E_ACTIVE_COL,
                DataBaseHelper.E_TYPE_COL
        },null, null, null, null, null);

        while (c.moveToNext()){
           if (c.getInt(6)!= 0){
               EventObj event = new EventObj();
               event.setId_event(c.getInt(0));
               event.setReceiverById(c.getInt(1));
               event.setWhen(Date.valueOf(Double.toString(c.getDouble(2))));//FIXME find the right code
               event.setHour(c.getInt(3));
               event.setMinute(c.getInt(4));
               event.setLocation(c.getString(5));
               event.setActive(c.getInt(6));
               event.setType(c.getInt(7));
               eventObj = event;
           }

        }
        return eventObj;
    }



    public void loadEvent(EventObj event){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.E_USER_INVITED_ID_COL, event.getReceiver().getUserId());
        values.put(DataBaseHelper.E_WHEN_COL, formatter.format(event.getWhen()));
        values.put(DataBaseHelper.E_HOUR_COL, event.getHour());
        values.put(DataBaseHelper.E_MINUTE_COL, event.getMinute());
        values.put(DataBaseHelper.E_LOCATION_COL, event.getLocation());
        values.put(DataBaseHelper.E_ACTIVE_COL, event.getActive());
        values.put(DataBaseHelper.E_TYPE_COL, event.getType());

        db.insert(DataBaseHelper.EVENTS_TABLE,null,values);
    }
}
