package com.gabrielemaffoni.toastapp.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gabrielemaffoni.toastapp.R;
import com.gabrielemaffoni.toastapp.objects.Friend;

import java.util.ArrayList;


/**
 *  This adapter inflates the list of friends a user has in the database.
 *
 *  @author 40284943
 *  @version 1.5
 */

public class FriendsAdapter extends ArrayAdapter<Friend> {
    private Context context;
    private ArrayList<Friend> friendArrayList;


    public FriendsAdapter(Context context, ArrayList<Friend> friendArrayList) {
        super(context, 0, friendArrayList);
        this.friendArrayList = friendArrayList;
    }

    @Override
    public int getCount() {
        return friendArrayList.size();
    }

    @Override
    public Friend getItem(int position) {
        return friendArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Friend friend = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_friend_grid, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView surname = (TextView) convertView.findViewById(R.id.surname);
        TextView userId = (TextView) convertView.findViewById(R.id.userIdHidden);
        ImageView avatar = (ImageView) convertView.findViewById(R.id.profile_pic);


        name.setText(friend.getUserName());
        surname.setText(friend.getUserSurname());
        userId.setText(friend.getUserId());
        friend.convertAvatar(friend.getUserProfilePic());
        avatar.setImageResource(friend.getUserProfilePic());


        return convertView;
    }


}
