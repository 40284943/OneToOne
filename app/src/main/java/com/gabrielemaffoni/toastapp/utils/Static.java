package com.gabrielemaffoni.toastapp.utils;

import com.gabrielemaffoni.toastapp.R;

public class Static {
    //AVATARS
    public final static int AVATAR_STANDARD = R.drawable.ic_user;
    public final static int AVATAR1 = R.drawable.ic_avatar_02;
    public final static int AVATAR2 = R.drawable.ic_avatar_03;
    public final static int AVATAR3 = R.drawable.ic_avatar_04;
    public final static int AVATAR4 = R.drawable.ic_avatar_05;

    //TYPES OF NIGHT
    public static final int BEER = 1;
    public static final int COCKTAIL = 2;
    public static final int COFFEE = 3;
    public static final int LUNCH = 4;

    //JSON NAMES FOR EVENT AND ITS CHILD

    public final static String EACTIVE = "active";
    public final static String EADDRESS = "address";
    public final static String ELAT = "lat";
    public final static String ELON = "lon";
    public final static String ELOCATION = "location_name";
    public final static String ETYPE = "type";
    public final static String ESENDERID = "senderID";


    //RECEIVER CHILD
    public final static String ERECEIVER = "receiver";
    public final static String ERUID = "userId";
    public final static String ERUNAME = "userName";
    public final static String ERUPRPIC = "userProfilePic";
    public final static String ERUSURNAME = "userSurname";


    //WHEN CHILD
    public final static String EWHEN = "when";

    public final static String EHOUR = "hours";
    public final static String EMINUTE = "minutes";
    public final static String EDATE = "date";
    public final static String EMONTH = "month";
    public final static String EYEAR = "year";
    public final static String ETIMEMILLISEC = "time";
    public final static String ETIMEZONEOFFSET = "timezoneOffset";
    public final static String ETIME = "time";


    //JSON NAMES FOR USER
    public final static String UID = "userId";
    public final static String UNAME = "userName";
    public final static String USURNAME = "userSurname";

    public final static String UPROFPIC = "userProfilePic";
    public final static String UEMAIL = "userEmail";


    //JSON NAMES FOR THE GENERAL CHILDS
    public final static String UDB = "users";
    public final static String FRIENDSDB = "friends";
    public final static String EVENTSDB = "events";


    public static final int MAYBE = 3;
    public static final int ACCEPTED = 1;
    public static final int REFUSED = 0;

    public static boolean IS_PRESSED = false;

    public static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    public static String TAG = "Places";



}
