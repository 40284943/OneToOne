package com.gabrielemaffoni.toastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import com.gabrielemaffoni.toastapp.to.Friend;
import com.gabrielemaffoni.toastapp.classes.FriendsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    public static final String USER_DB_NAME = "users";
    public static final String USER_ID = "user_id";
    final ArrayList<Friend> friendArrayList = new ArrayList<>();
    DatabaseReference friendsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        friendsDatabase = FirebaseDatabase.getInstance().getReference().child(USER_DB_NAME);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_home);
        final GridView grid = (GridView) findViewById(R.id.friends_list);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().toLowerCase().equals("add user")) {
                    Intent activity = new Intent(getApplicationContext(), AddUser.class);
                    startActivity(activity);
                }
                return true;
            }
        });

        friendsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendArrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Friend friend = postSnapshot.getValue(Friend.class);
                    friendArrayList.add(friend);
                }
                FriendsAdapter adapter = new FriendsAdapter(getApplicationContext(), friendArrayList);
                grid.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        grid.setVerticalSpacing(60);
        grid.setNumColumns(3);
        grid.setColumnWidth(40);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent singleEvent = new Intent(getApplicationContext(), EventActivity.class);
                singleEvent.putExtra("Profile picture", Friend.createListOfFriends().get(position).getUserName());
                startActivity(singleEvent);
            }
        });
    }


}
