package com.gabrielemaffoni.toastapp.to;

import com.google.android.gms.location.places.Place;

import java.util.GregorianCalendar;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class Event {

    private Friend receiver;
    private GregorianCalendar when;
    private int active;
    private int hour;
    private int minute;
    private int type;
    private String location_name;
    private String address;
    private double lat;
    private double lon;


    public Event() {

    }

    public Event(Friend receiver, GregorianCalendar when, int active, int hour, int minute, int type, String location_name, String address, double lat, double lon) {
        this.receiver = receiver;
        this.when = when;
        this.active = active;
        this.hour = hour;
        this.minute = minute;
        this.type = type;
        this.location_name = location_name;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }

    public Event(Friend receiver, GregorianCalendar when, int active, int type, Place place, int avatar) {

        this.receiver = receiver;
        this.when = when;
        this.active = active;
        this.type = type;

    }

    public Event(Friend receiver) {
        this.receiver = receiver;
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

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
