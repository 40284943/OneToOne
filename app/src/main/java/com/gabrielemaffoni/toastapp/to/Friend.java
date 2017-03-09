package com.gabrielemaffoni.toastapp.to;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Blob;
import java.util.ArrayList;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class Friend {
    public String userId;
    public String userName;
    public String userSurname;
    public int userProfilePic;

    public Friend() {

    }

    public Friend(String id, String name, String surname) {

        this.userId = id;
        this.userName = name;
        this.userSurname = surname;
    }

    public Friend(String id, String name, String userSurname, int userProfilePic) {
        this.userId = id;
        this.userName = name;
        this.userSurname = userSurname;
        this.userProfilePic = userProfilePic;
    }

    public Friend(String name, String surname){
        this.userName = name;
        this.userSurname = surname;
    }

    public static ArrayList<Friend> createListOfFriends() {
        ArrayList<Friend> list = new ArrayList<>();
        int i = 0;
        String[] names = new String[]{
                "Gabriele",
                "Silvia",
                "Ugo",
                "Davide",
                "Andrea",
                "Francesco",
                "Simone",
                "Stefano",
                "Giulio",
                "Sofia",
                "Maria",
                "Nada"
        };

        String[] surnames = new String[]{
                "Maffoni",
                "Fumi",
                "Gatteschi",
                "Nembrini",
                "Novellino",
                "Bianchi",
                "Rossi",
                "Verdi",
                "Gialli",
                "Balduzzi",
                "Smith",
                "Kendall"
        };

        for (String name : names
                ) {

            Friend friend = new Friend(name, surnames[i]);
            list.add(friend);
            i++;
        }

        return list;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public int getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(int userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

}