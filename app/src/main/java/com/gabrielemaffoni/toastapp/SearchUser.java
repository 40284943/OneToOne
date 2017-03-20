package com.gabrielemaffoni.toastapp;

import android.os.Bundle;
import android.provider.ContactsContract;
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
 * This activity permits to add a user searching him/her by its email in the JSON database.
 *
 * @author 40284943
 * @version 1.2
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

        //we connect to the database
        firebaseAuth = FirebaseAuth.getInstance();
        //and we find the "users" child in it
        db = FirebaseDatabase.getInstance().getReference().child(UDB);

        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        findViews();

        //After the user press enter, we search on the database if there is a user
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //we search the user by email
                final Query findFriendByEmail = db.orderByChild(UEMAIL).equalTo(searchView.getQuery().toString());

                findFriendByEmail.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        //if the friend exists, show the data
                        if (dataSnapshot.exists()) {

                            foundUserSection.setVisibility(View.VISIBLE);
                            userFoundName = (TextView) findViewById(R.id.nameUserFound);

                            Friend receiver = downloadUserData(dataSnapshot);

                            addUserMethod(addUser, currentUserId, receiver.getUserId(), receiver, db);
                        }
                        //otherwise tell the user that it doesn't exist
                        else {
                            TextView userNotFound = (TextView) findViewById(R.id.user_not_found);
                            userNotFound.setVisibility(View.VISIBLE);
                        }
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

    private Friend downloadUserData(DataSnapshot dataSnapshot) {

        HashMap<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();

        userFoundName.setText(String.valueOf(users.get(UNAME)) + " " + String.valueOf(users.get(USURNAME)));
        addUser = (Button) findViewById(R.id.addUserFound);

        String finalEmail = String.valueOf(users.get(UEMAIL));
        String finalName = String.valueOf(users.get(UNAME));
        String finalSurname = String.valueOf(users.get(USURNAME));
        String finalUserId = String.valueOf(dataSnapshot.getKey());
        int finalProfilePic = Integer.parseInt(
                String.valueOf(users.get(UPROFPIC))
        );

        Friend friend = new Friend(
                finalUserId,
                finalName,
                finalSurname,
                finalProfilePic
        );
        friend.setUserEmail(finalEmail);


        return friend;

    }


    private void addUserMethod(Button addUser, final String searcherId, final String userFoundId, final Friend friendToAdd, final DatabaseReference db) {
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //We find the "friends" database
                final DatabaseReference dbToAddFriendIn = FirebaseDatabase.getInstance().getReference().child(FRIENDSDB);

                //And we add it to the current user id
                dbToAddFriendIn.child(searcherId).child(friendToAdd.getUserId()).setValue(friendToAdd);

                //Then we do the opposite, looking for the data in the users database
                Query findCurrentUserToAdd = db.orderByKey().equalTo(searcherId);
                findCurrentUserToAdd.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String s) {
                        HashMap<String, Object> searcher = (HashMap<String, Object>) snapshot.getValue();

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
                                finalSearcherSurname,
                                finalProfilePic
                        );

                        friendWhoSearched.setUserEmail(finalSearcherEmail);

                        //and we add it to the added user's "friends" database
                        DatabaseReference secondFriendDb = FirebaseDatabase.getInstance().getReference().child(FRIENDSDB).child(friendToAdd.getUserId());
                        secondFriendDb.child(searcherId).setValue(friendWhoSearched);


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

                //we finish this activity and go back to "home"
                finish();
                Toast.makeText(getApplicationContext(), friendToAdd.getUserName() + " has been added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findViews() {

        searchView = (SearchView) findViewById(R.id.userSearch);
        foundUserSection = (RelativeLayout) findViewById(R.id.foundUser);
    }
}
