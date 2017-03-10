package com.gabrielemaffoni.toastapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielemaffoni.toastapp.to.Friend;
import com.gabrielemaffoni.toastapp.to.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

import static com.gabrielemaffoni.toastapp.utils.Static.*;

/**
 * Created by gabrielemaffoni on 10/03/2017.
 */

public class SearchUser extends AppCompatActivity {

    DatabaseReference db;
    FirebaseAuth firebaseAuth;
    private SearchView searchView;
    private Button addUser;
    private TextView userFoundName;
    private RelativeLayout foundUserSection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child(UDB);

        final String currentUserId = firebaseAuth.getCurrentUser().getUid();
        searchView = (SearchView) findViewById(R.id.userSearch);
        foundUserSection = (RelativeLayout) findViewById(R.id.foundUser);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                foundUserSection.setVisibility(View.VISIBLE);
                userFoundName = (TextView) findViewById(R.id.nameUserFound);

                Query query1 = db.orderByChild(UEMAIL).equalTo(searchView.getQuery().toString());

                query1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        HashMap<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();

                        userFoundName.setText(String.valueOf(users.get(UNAME)) + " " + String.valueOf(users.get(USURNAME)));
                        addUser = (Button) findViewById(R.id.addUserFound);
                        String finalName = String.valueOf(users.get(UNAME));
                        String finalSurname = String.valueOf(users.get(USURNAME));
                        String finalUserId = String.valueOf(users.get(UID));
                        int finalProfilePic = Integer.parseInt(
                                String.valueOf(users.get(UPROFPIC))
                        );

                        Friend friend = new Friend(

                                finalName,
                                finalSurname,
                                finalProfilePic
                        );
                        addUserMethod(addUser, currentUserId, finalUserId, friend);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void addUserMethod(Button addUser, final String userIdAdded, final String friendUserID, final Friend friendToAdd) {
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database2 = FirebaseDatabase.getInstance().getReference().child(FRIENDSDB);
                database2.child(userIdAdded).child(friendUserID).setValue(friendToAdd);
                finish();
                Toast.makeText(getApplicationContext(), friendToAdd.getUserName() + " has been added", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
