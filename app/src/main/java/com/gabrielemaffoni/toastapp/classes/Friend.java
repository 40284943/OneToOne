package com.gabrielemaffoni.toastapp.classes;

import java.util.ArrayList;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class Friend {
    private String name;
    private String surname;

    public Friend() {
    }

    public Friend(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

}