package com.gabrielemaffoni.toastapp.to;

import java.util.ArrayList;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class Friend extends User {



    public Friend() {
        super();
    }


    public Friend(String name, String surname) {
        super(name, surname);
    }

    public Friend(String name, String surname, int profilePic) {
        super(name, surname, profilePic);
    }

    public Friend(String userId, String name, String surname) {
        super(userId, name, surname);
    }

    public Friend(String userId, String userName, String userSurname, int userProfilePic) {
        super(userId, userName, userSurname, userProfilePic);
    }
}