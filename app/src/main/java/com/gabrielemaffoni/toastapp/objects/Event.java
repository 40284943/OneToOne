package com.gabrielemaffoni.toastapp.objects;


import com.gabrielemaffoni.toastapp.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;


import static com.gabrielemaffoni.toastapp.utils.Static.BEER;
import static com.gabrielemaffoni.toastapp.utils.Static.COCKTAIL;
import static com.gabrielemaffoni.toastapp.utils.Static.COFFEE;
import static com.gabrielemaffoni.toastapp.utils.Static.EACTIVE;
import static com.gabrielemaffoni.toastapp.utils.Static.EADDRESS;
import static com.gabrielemaffoni.toastapp.utils.Static.EDATE;
import static com.gabrielemaffoni.toastapp.utils.Static.EHOUR;
import static com.gabrielemaffoni.toastapp.utils.Static.ELAT;
import static com.gabrielemaffoni.toastapp.utils.Static.ELOCATION;
import static com.gabrielemaffoni.toastapp.utils.Static.ELON;
import static com.gabrielemaffoni.toastapp.utils.Static.EMINUTE;
import static com.gabrielemaffoni.toastapp.utils.Static.EMONTH;
import static com.gabrielemaffoni.toastapp.utils.Static.ERECEIVER;
import static com.gabrielemaffoni.toastapp.utils.Static.ESENDERID;
import static com.gabrielemaffoni.toastapp.utils.Static.ETYPE;
import static com.gabrielemaffoni.toastapp.utils.Static.EWHEN;
import static com.gabrielemaffoni.toastapp.utils.Static.EYEAR;
import static com.gabrielemaffoni.toastapp.utils.Static.LUNCH;

/**
 * A class that represents the single event a person is invited to.
 *
 * @author 40284943
 * @version 1.4
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
    private HashMap<String, Integer> time;

    /**
     * !IMPORTANT empty constructor
     */
    public Event() {

    }

    /**
     * The constructor of a single event takes itself parameters that are useful in the end for the saving in the DB.
     *
     * @param receiver      Who is invited to the event
     * @param when          The day and time of the event
     * @param active        This parameter takes three different values = "1", "3" and "0". When someone invites someone else, the event is saved as "3" in the database. Once he accepts or refuses the event is saved as "1" or "0" in the database.
     * @param type          It can take four different values: "BEER" "COCKTAIL" "LUNCH" and "COFFEE" (1,2,3,4).
     * @param location_name The name of the location where is the meeting. Can be null.
     * @param address       The address of the location where is the meeting. Can be null.
     * @param lat           The latitude of the location where is the meeting. Can be null.
     * @param lon           The longitude of the location where is the meeting. Can be null.
     * @param senderID      Who is inviting. It's useful after to find it into the database.
     */
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


    /**
     * This method helps the app to find the right image inside the database.
     *
     * @param type the type of the event
     * @return the ID of the drawable resource
     */

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

    /**
     * This method is useful mostly for the UX: shows the write "TODAY" or "TOMORROW" into the cardview.
     *
     * @param date The date of the event
     * @return A string where is written "TODAY" or "TOMORROW" in the card.
     */

    public static String setTodayOrTomorrow(GregorianCalendar date) {
        String finalDate;
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

    /**
     * This is a custom method created to copy one event to another without having any issue.
     *
     * @param newEvent The event where I have to copy the event caller
     */

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

    public HashMap<String, Object> convertEventToHashMap(Friend friendWhoSearched, String currentUserId) {

        HashMap<String, Object> eventConverted = new HashMap<>();
        eventConverted.put(EACTIVE, this.getActive());
        eventConverted.put(EADDRESS, this.getAddress());
        eventConverted.put(ELAT, this.getLat());
        eventConverted.put(ELOCATION, this.getLocation_name());
        eventConverted.put(ELON, this.getLon());
        eventConverted.put(ERECEIVER, friendWhoSearched);
        eventConverted.put(ESENDERID, currentUserId);
        eventConverted.put(ETYPE, this.getType());
        HashMap<String, Integer> timeEvent = new HashMap<>();
        timeEvent.put(EHOUR, this.getWhen().get(Calendar.HOUR_OF_DAY));
        timeEvent.put(EDATE, this.getWhen().get(Calendar.DAY_OF_MONTH));
        timeEvent.put(EMINUTE, this.getWhen().get(Calendar.MINUTE));
        timeEvent.put(EYEAR, this.getWhen().get(Calendar.YEAR));
        timeEvent.put(EMONTH, this.getWhen().get(Calendar.MONTH));
        eventConverted.put(EWHEN, timeEvent);

        return eventConverted;
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
