package com.gabrielemaffoni.toastapp.to;

import com.google.android.gms.location.places.Place;

import java.util.GregorianCalendar;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class Event {


    private int id_event;
    private Friend receiver;

    private GregorianCalendar when;
    private int active;
    private int hour;
    private int minute;
    private int type;
    private String location;
    private Place place;


    public Event() {

    }

    public Event(int id_event, Friend receiver, GregorianCalendar when, int active, int type, Place place, int avatar) {
        this.id_event = id_event;
        this.receiver = receiver;
        this.when = when;
        this.active = active;
        this.type = type;
        this.place = place;
    }

    public Event(Friend receiver) {
        this.receiver = receiver;
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

    public void setReceiver(Friend receiver) {
        this.receiver = receiver;
    }

    public GregorianCalendar getWhen() {
        return when;
    }

    public void setWhen(GregorianCalendar when) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
