package com.gabrielemaffoni.toastapp.to;

import com.gabrielemaffoni.toastapp.R;
import com.google.android.gms.location.places.Place;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.gabrielemaffoni.toastapp.utils.Static.*;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class Event {

    private Friend receiver;
    private GregorianCalendar when;
    private int active;
    private int type;
    private String location_name;
    private String address;
    private double lat;
    private double lon;
    private String senderID;

    public Event() {

    }

    public Event(Friend receiver, GregorianCalendar when, int active, int type, String location_name, String address, double lat, double lon, String senderID) {
        this.receiver = receiver;
        this.when = when;
        this.active = active;
        this.type = type;
        this.location_name = location_name;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.senderID = senderID;
    }

    public Event(Friend receiver, GregorianCalendar when, int active, int type, String location_name, String address, double lat, double lon) {
        this.receiver = receiver;
        this.when = when;
        this.active = active;
        this.type = type;
        this.location_name = location_name;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }



    public Event(Friend receiver) {
        this.receiver = receiver;
    }

    public static int findRightImageResource(int type) {
        //Check which event it is
        int imageResource = 0;

        switch (type) {
            case BEER:
                imageResource = R.drawable.ic_beer;
                break;
            case COCKTAIL:
                imageResource = R.drawable.ic_cocktail;
                break;
            case LUNCH:
                imageResource = R.drawable.ic_lunch;
                break;
            case COFFEE:
                imageResource = R.drawable.ic_coffee;
                break;
        }

        return imageResource;
    }

    public static String setTodayOrTomorrow(GregorianCalendar date) {
        String finalDate = "";
        Calendar today = Calendar.getInstance();
        GregorianCalendar todayCal = new GregorianCalendar(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        );


        if (date.equals(todayCal)) {
            finalDate = "Today";
        } else {
            finalDate = "Tomorrow";
        }

        return finalDate;
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

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void copyEvent(Event newEvent) {
        newEvent.setReceiver(this.receiver);
        newEvent.setLat(this.lat);
        newEvent.setLon(this.lon);
        newEvent.setWhen(this.when);
        newEvent.setType(this.type);
        newEvent.setLocation_name(this.location_name);
        newEvent.setAddress(this.address);
    }

    public void copyToEventExceptFriend(Event newEvent) {
        newEvent.setLat(this.getLat());
        newEvent.setLon(this.getLon());
        newEvent.setWhen(this.getWhen());
        newEvent.setType(this.getType());
        newEvent.setLocation_name(this.getLocation_name());
        newEvent.setAddress(this.getAddress());
        newEvent.setActive(this.getActive());
        newEvent.setSenderID(this.getSenderID());
    }

    @Override
    public String toString() {
        return "Event{" +
                "receiver=" + receiver +
                ", when=" + when +
                ", active=" + active +

                ", type=" + type +
                ", location_name='" + location_name + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
