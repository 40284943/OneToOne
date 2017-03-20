package com.gabrielemaffoni.toastapp.objects;

/**
 * Represents a single user.
 *
 * @version 1.2
 * @author 40284943
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

    public User(String userId, String userName, String userSurname, String userEmail, int userProfilePic) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userEmail = userEmail;
        this.userProfilePic = userProfilePic;
    }

    /**
     * @param userId         His/her ID assigned by the Database in upload
     * @param userName       The name of the user
     * @param userSurname    The surname of the user
     * @param userEmail      The email and username to access to the app and to find him/her
     * @param userPassword   The password he/she access to the app with
     * @param userProfilePic The ID of the avatar they chose
     */
    public User(String userId, String userName, String userSurname, String userEmail, String userPassword, int userProfilePic) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userProfilePic = userProfilePic;
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
