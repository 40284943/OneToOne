package com.gabrielemaffoni.toastapp.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gabrielemaffoni.toastapp.R;
import com.gabrielemaffoni.toastapp.to.Friend;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class FriendsAdapter extends ArrayAdapter<Friend> {
            private Context context;
            private ArrayList<Friend> friendArrayList;


    public FriendsAdapter(Context context, ArrayList<Friend> friendArrayList)
    {
        super(context,0,friendArrayList);
        this.friendArrayList = friendArrayList;
    }

    @Override
    public int getCount(){
        return friendArrayList.size();
    }

    @Override
    public Friend getItem(int position){
        return friendArrayList.get(position);
    }

    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Friend friend = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_friend_grid,parent,false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView surname = (TextView) convertView.findViewById(R.id.surname);


        name.setText(friend.getUserName());
        surname.setText(friend.getUserSurname());

        return convertView;
    }


}
