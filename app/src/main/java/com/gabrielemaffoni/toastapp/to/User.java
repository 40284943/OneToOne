package com.gabrielemaffoni.toastapp.to;

import com.gabrielemaffoni.toastapp.R;

/**
 * Created by gabrielemaffoni on 09/03/2017.
 */

public class User {


    public String userId;
    public String userName;
    public String userSurname;
    public String userEmail;
    public String userPassword;
    public int userProfilePic;

    public User() {
    }

    public User(String userId, String userName, String userSurname, int userProfilePic) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userProfilePic = userProfilePic;
    }

    public User(String userName, String userSurname) {
        this.userName = userName;
        this.userSurname = userSurname;
    }

    public User(String userId, String userName, String userSurname) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
    }

    public User(String userName, String userSurname, int userProfilePic) {
        this.userName = userName;
        this.userSurname = userSurname;
        this.userProfilePic = userProfilePic;
    }

    public User(String userId, String userName, String userSurname, String userEmail, String userPassword, int userProfilePic) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userProfilePic = userProfilePic;
    }

    public User(String userId, String userName, String userSurname, String userEmail, String userPassword) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public User(String userName, String userSurname, String userEmail, String userPassword) {
        this.userName = userName;
        this.userSurname = userSurname;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
