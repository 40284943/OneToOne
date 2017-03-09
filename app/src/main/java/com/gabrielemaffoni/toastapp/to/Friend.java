package com.gabrielemaffoni.toastapp.to;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Blob;
import java.util.ArrayList;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class Friend implements Parcelable{
    private int id;
    private String name;
    private String surname;
    private String profilePic;

    public Friend() {
        super();
    }

    public Friend(int id, String name, String surname) {
        super();
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Friend(String name, String surname){
        this.name = name;
        this.surname = surname;
    }

    public Friend(Parcel in){
        super();
        this.id = in.readInt();
        this.name = in.readString();
        this.surname = in.readString();
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

    public int getUserId() {
        return id;
    }

    public void setUserId(int id) {
        this.id = id;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeInt(getUserId());
        parcel.writeString(getName());
        parcel.writeString(getSurname());
    }

    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>(){
        public Friend createFromParcel(Parcel in){
            return new Friend(in);
        }

        public Friend[] newArray(int size){
            return new Friend[size];
        }

    };

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if(this==obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        Friend other = (Friend) obj;
        if (id != other.id){
            return false;
        }
        return true;
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