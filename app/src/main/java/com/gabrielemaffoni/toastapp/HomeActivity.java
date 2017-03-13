package com.gabrielemaffoni.toastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.gabrielemaffoni.toastapp.classes.FriendsAdapter;
import com.gabrielemaffoni.toastapp.to.Friend;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.gabrielemaffoni.toastapp.utils.Static.*;

//TODO add notifications (check onChildChanged - or similar) (!IMPORTANT)
//TODO Add personal settings(!IMPORTANT)
//TODO Add possibility to delete friend (!IMPORTANT)
//TODO Add possibility to change avatar (< IMPORTANT)
public class HomeActivity extends AppCompatActivity {


    final ArrayList<Friend> friendArrayList = new ArrayList<>();
    DatabaseReference friendsDatabase;
    private FirebaseAuth firebaseAuth;
    private String cUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Check if the user is logged in
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            Intent startApp = new Intent(this, Login.class);
            startActivity(startApp);
        }

        cUID = firebaseAuth.getCurrentUser().getUid();
        friendsDatabase = FirebaseDatabase.getInstance().getReference().child(FRIENDSDB).child(cUID);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_home);
        final GridView grid = (GridView) findViewById(R.id.friends_list);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getTitle().equals(ADD_USER)) {
                    Intent activity = new Intent(getApplicationContext(), SearchUser.class);
                    startActivity(activity);
                } else if (item.getTitle().equals(LOGOUT)) {
                    firebaseAuth.signOut();
                    finish();
                    Toast.makeText(getApplicationContext(), "You logged out", Toast.LENGTH_SHORT).show();
                    Intent signIn = new Intent(getApplicationContext(), Login.class);
                    startActivity(signIn);

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
                    friend.setUserId(postSnapshot.getKey());

                    friendArrayList.add(friend);
                }
                FriendsAdapter adapter = new FriendsAdapter(getApplicationContext(), friendArrayList);
                setGridDesignData(grid, adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();

        friendsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendArrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Friend friend = postSnapshot.getValue(Friend.class);
                    friend.setUserId(postSnapshot.getKey());

                    friendArrayList.add(friend);
                }

                FriendsAdapter adapter = new FriendsAdapter(getApplicationContext(), friendArrayList);
                GridView grid = (GridView) findViewById(R.id.friends_list);

                setGridDesignData(grid, adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setGridDesignData(GridView grid, FriendsAdapter adapter) {
        grid.setAdapter(adapter);

        grid.setVerticalSpacing(60);
        grid.setNumColumns(3);
        grid.setColumnWidth(40);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent singleEvent = new Intent(getApplicationContext(), EventActivity.class);

                singleEvent.putExtra("profile_picture", friendArrayList.get(position).getUserProfilePic());
                singleEvent.putExtra("name", friendArrayList.get(position).getUserName());
                singleEvent.putExtra("surname", friendArrayList.get(position).getUserSurname());
                singleEvent.putExtra("user_id", friendArrayList.get(position).getUserId());

                startActivity(singleEvent);
            }
        });
    }


}
