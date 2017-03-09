package com.gabrielemaffoni.toastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import com.gabrielemaffoni.toastapp.to.Friend;
import com.gabrielemaffoni.toastapp.classes.FriendsAdapter;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        ArrayList<Friend> friendArrayList;


        FriendsAdapter adapter = new FriendsAdapter(this,Friend.createListOfFriends());

        GridView grid = (GridView) findViewById(R.id.friends_list);

        grid.setAdapter(adapter);
        grid.setVerticalSpacing(60);
        grid.setNumColumns(3);
        grid.setColumnWidth(40);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent singleEvent = new Intent(getApplicationContext(), Event.class);
                singleEvent.putExtra("Profile picture",Friend.createListOfFriends().get(position).getName());
                startActivity(singleEvent);
            }
        });
    }
}
