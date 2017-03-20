package com.gabrielemaffoni.toastapp.objects;

import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR1;
import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR2;
import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR3;
import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR4;
import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR_STANDARD;

/**
 * A class that rapresents just a single friend and not the whole users.
 * It will be important for further developings of the app.
 *
 * @author 40284943
 * @version 1.3
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

    /**
     * @param userId         The ID of the friend
     * @param userName       The name of the friend
     * @param userSurname    The surname of the friends
     * @param userProfilePic The path to the avatar
     */
    public Friend(String userId, String userName, String userSurname, int userProfilePic) {
        super(userId, userName, userSurname, userProfilePic);
    }

    /**
     * This method helps to convert to the right avatar what's written in the database.
     * In this way we are not going to have any problem in case of change of ID.
     *
     * @param friendAvatarDB The number written in the database
     */

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