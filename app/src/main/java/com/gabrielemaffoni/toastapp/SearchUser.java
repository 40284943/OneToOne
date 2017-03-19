package com.gabrielemaffoni.toastapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielemaffoni.toastapp.objects.Friend;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

import static com.gabrielemaffoni.toastapp.utils.Static.FRIENDSDB;
import static com.gabrielemaffoni.toastapp.utils.Static.UDB;
import static com.gabrielemaffoni.toastapp.utils.Static.UEMAIL;
import static com.gabrielemaffoni.toastapp.utils.Static.UID;
import static com.gabrielemaffoni.toastapp.utils.Static.UNAME;
import static com.gabrielemaffoni.toastapp.utils.Static.UPROFPIC;
import static com.gabrielemaffoni.toastapp.utils.Static.USURNAME;

/**
 * TODO ADD JAVADOC COMMENTS
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

                Query findFriendByEmail = db.orderByChild(UEMAIL).equalTo(searchView.getQuery().toString());

                findFriendByEmail.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        HashMap<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();

                        userFoundName.setText(String.valueOf(users.get(UNAME)) + " " + String.valueOf(users.get(USURNAME)));
                        addUser = (Button) findViewById(R.id.addUserFound);
                        String finalEmail = String.valueOf(users.get(UEMAIL));
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
                        friend.setUserEmail(finalEmail);
                        friend.convertAvatar(friend.getUserProfilePic());
                        addUserMethod(addUser, currentUserId, finalUserId, friend, db);
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


    private void addUserMethod(Button addUser, final String searcherId, final String userFound, final Friend friendToAdd, final DatabaseReference db) {
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference dbToAddFriendIn = FirebaseDatabase.getInstance().getReference().child(FRIENDSDB);
                dbToAddFriendIn.child(searcherId).child(userFound).setValue(friendToAdd);


                Query findCurrentUserToAdd = db.orderByKey().equalTo(searcherId);
                findCurrentUserToAdd.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        HashMap<String, Object> searcher = (HashMap<String, Object>) dataSnapshot.getValue();

                        String finalSearcherEmail = String.valueOf(searcher.get(UEMAIL));
                        String finalSearcherName = String.valueOf(searcher.get(UNAME));
                        String finalSearcherSurname = String.valueOf(searcher.get(USURNAME));
                        String finalSearcherId = String.valueOf(searcher.get(UID));
                        int finalProfilePic = Integer.parseInt(
                                String.valueOf(searcher.get(UPROFPIC))
                        );

                        Friend friendWhoSearched = new Friend(
                                finalSearcherId,
                                finalSearcherName,
                                finalSearcherSurname
                        );
                        friendWhoSearched.convertAvatar(finalProfilePic);
                        friendWhoSearched.setUserEmail(finalSearcherEmail);


                        dbToAddFriendIn.child(userFound).child(searcherId).setValue(friendWhoSearched);


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
                finish();
                Toast.makeText(getApplicationContext(), friendToAdd.getUserName() + " has been added", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
