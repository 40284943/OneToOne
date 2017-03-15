package com.gabrielemaffoni.toastapp.to;

import java.util.ArrayList;

import static com.gabrielemaffoni.toastapp.utils.Static.*;

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

    public void convertAvatar(int friendAvatarDB) {
        switch (friendAvatarDB) {
            case 0:
                this.setUserProfilePic(AVATAR_STANDARD);
                break;
            case 1:
                this.setUserProfilePic(AVATAR1);
                break;
            case 2:
                this.setUserProfilePic(AVATAR2);
                break;
            case 3:
                this.setUserProfilePic(AVATAR3);
                break;
            case 4:
                this.setUserProfilePic(AVATAR4);
                break;
            default:
                this.setUserProfilePic(AVATAR_STANDARD);
        }
    }

}