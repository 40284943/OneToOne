package com.gabrielemaffoni.toastapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gabrielemaffoni.toastapp.to.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class UserDAO extends EventDBDAO {
  private static final String WHERE_ID_EQUALS = DataBaseHelper.U_ID_COL + " =?";

    public UserDAO(Context context){
        super(context);
    }

    public long save(Friend friend){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.U_NAME_COL, friend.getName());
        values.put(DataBaseHelper.U_SURNAME_COL,friend.getSurname());
        values.put(DataBaseHelper.U_PROFILE_PIC_COL,friend.getProfilePic());

        return db.insert(DataBaseHelper.USERS_TABLE,null,values);
    }

    public long update(Friend friend){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.U_NAME_COL, friend.getName());
        values.put(DataBaseHelper.U_SURNAME_COL, friend.getSurname());
        values.put(DataBaseHelper.U_PROFILE_PIC_COL,friend.getProfilePic());

        long result = db.update(DataBaseHelper.USERS_TABLE,values, WHERE_ID_EQUALS, new String[] {String.valueOf(friend.getUserId())});
        Log.d("Update Result:","="+result);
        return result;
    }

    public int deleteUser(Friend friend){
        return db.delete(DataBaseHelper.USERS_TABLE,WHERE_ID_EQUALS,new String[]{friend.getUserId()+ ""});
    }

    public List<Friend> getFriends(){
        List<Friend> friendList = new ArrayList<>();
        Cursor c = db.query(DataBaseHelper.USERS_TABLE, new String[]{
                DataBaseHelper.U_ID_COL,
                DataBaseHelper.U_NAME_COL,
                DataBaseHelper.U_SURNAME_COL,
                DataBaseHelper.U_PROFILE_PIC_COL
        }, null, null, null, null, null);

        while (c.moveToNext()){
            Friend friend = new Friend();
            friend.setUserId(c.getInt(0));
            friend.setName(c.getString(1));
            friend.setSurname(c.getString(2));
            friend.setProfilePic(c.getString(3));
            friendList.add(friend);
        }

        return friendList;
    }

    public final Friend getSingleFriendById(int id){
        Friend friend = new Friend();
        Cursor c = db.query(DataBaseHelper.USERS_TABLE, new String[]{
                DataBaseHelper.U_ID_COL,
                DataBaseHelper.U_NAME_COL,
                DataBaseHelper.U_SURNAME_COL,
                DataBaseHelper.U_PROFILE_PIC_COL
        }, null, null, null, null, null);

        while (c.moveToNext()){
            if(c.getInt(0) == id){
                friend.setUserId(c.getInt(0));
                friend.setName(c.getString(1));
                friend.setSurname(c.getString(2));
                friend.setProfilePic(c.getString(3));
                break;
            }

        }

        return friend;

    }

    public void loadUser(Friend friend){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.U_NAME_COL, friend.getName());
        values.put(DataBaseHelper.U_SURNAME_COL, friend.getSurname());
       values.put(DataBaseHelper.U_PROFILE_PIC_COL, friend.getProfilePic());

        db.insert(DataBaseHelper.USERS_TABLE,null, values);
    }


}
