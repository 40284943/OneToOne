package com.gabrielemaffoni.toastapp.to;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.gabrielemaffoni.toastapp.db.UserDAO;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class EventObj implements Parcelable {

    public static final int BEER = 1;
    public static final int COCKTAIL = 2;
    public static final int COFFEE = 3;
    public static final int LUNCH = 4;

    private int id_event;
    private Friend receiver;
    private UserDAO supportReceiver;
    private int when;
    private int active;
    private int hour;
    private int minute;
    private int type;
    private String location;

    public EventObj(){super();}

    private EventObj(Parcel in){
        super();
        this.id_event = in.readInt();
        this.receiver = in.readParcelable(Friend.class.getClassLoader());
        this.when = in.readInt();
        this.active = in.readInt();
        this.hour = in.readInt();
        this.minute = in.readInt();
        this.location = in.readString();
        this.type = in.readInt();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public Friend getReceiver() {
        return receiver;
    }

    public void setReceiver(Friend friend){
        this.receiver = friend;
    }



    public void setReceiverById(int receiverId) {

        Friend friend = supportReceiver.getSingleFriendById(receiverId);
        this.receiver = friend;

    }

    public int getWhen() {
        return when;
    }

    public void setWhen(int when) {
        this.when = when;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + id_event;
        return result;
    }

    @Override
    public boolean equals(Object object){
        if (this == object){
            return true;
        }
        if (object == null){
            return false;
        }
        if (getClass() != object.getClass()){
            return false;
        }
        EventObj other = (EventObj) object;
        if (id_event != other.id_event){
            return false;
        }
        return true;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeInt(getId_event());
        parcel.writeParcelable(getReceiver(), flags);
        parcel.writeInt(getWhen());
        parcel.writeInt(getHour());
        parcel.writeInt(getMinute());
        parcel.writeInt(getActive());
        parcel.writeInt(getType());
    }

    public static final Parcelable.Creator<EventObj> CREATOR = new Parcelable.Creator<EventObj>(){
        public EventObj createFromParcel(Parcel in){
            return new EventObj(in);
        }

        public EventObj[] newArray(int size){
            return new EventObj[size];
        }
    };

}
