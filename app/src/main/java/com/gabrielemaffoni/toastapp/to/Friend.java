package com.gabrielemaffoni.toastapp.to;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class Friend extends User {


    public String userId;
    public String userName;
    public String userSurname;
    public int userProfilePic;

    public Friend() {
        super();
    }


    public Friend(String name, String surname) {
        super(name, surname);
    }

    /*public static ArrayList<Friend> createListOfFriends() {
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
    }*/

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