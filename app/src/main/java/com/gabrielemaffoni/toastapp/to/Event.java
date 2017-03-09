package com.gabrielemaffoni.toastapp.to;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class Event {

    public static final int BEER = 1;
    public static final int COCKTAIL = 2;
    public static final int COFFEE = 3;
    public static final int LUNCH = 4;
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
    private int id_event;
    private Friend receiver;

    private Date when;
    private int active;
    private int hour;
    private int minute;
    private int type;
    private String location;

    public Event() {
        super();
    }

    private Event(Parcel in) {
        super();
        this.id_event = in.readInt();
        this.receiver = in.readParcelable(Friend.class.getClassLoader());
        this.when = new Date(in.readLong());
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



    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
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



}
