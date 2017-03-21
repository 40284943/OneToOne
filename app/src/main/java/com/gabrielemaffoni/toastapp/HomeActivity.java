package com.gabrielemaffoni.toastapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielemaffoni.toastapp.classes.FriendsAdapter;
import com.gabrielemaffoni.toastapp.objects.Event;
import com.gabrielemaffoni.toastapp.objects.Friend;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static com.gabrielemaffoni.toastapp.utils.Static.ACCEPTED;
import static com.gabrielemaffoni.toastapp.utils.Static.EACTIVE;
import static com.gabrielemaffoni.toastapp.utils.Static.EADDRESS;
import static com.gabrielemaffoni.toastapp.utils.Static.EDATE;
import static com.gabrielemaffoni.toastapp.utils.Static.EHOUR;
import static com.gabrielemaffoni.toastapp.utils.Static.ELAT;
import static com.gabrielemaffoni.toastapp.utils.Static.ELOCATION;
import static com.gabrielemaffoni.toastapp.utils.Static.ELON;
import static com.gabrielemaffoni.toastapp.utils.Static.EMINUTE;
import static com.gabrielemaffoni.toastapp.utils.Static.EMONTH;
import static com.gabrielemaffoni.toastapp.utils.Static.ERECEIVER;
import static com.gabrielemaffoni.toastapp.utils.Static.ERUID;
import static com.gabrielemaffoni.toastapp.utils.Static.ERUNAME;
import static com.gabrielemaffoni.toastapp.utils.Static.ERUPRPIC;
import static com.gabrielemaffoni.toastapp.utils.Static.ERUSURNAME;
import static com.gabrielemaffoni.toastapp.utils.Static.ESENDERID;
import static com.gabrielemaffoni.toastapp.utils.Static.ETIME;
import static com.gabrielemaffoni.toastapp.utils.Static.ETYPE;
import static com.gabrielemaffoni.toastapp.utils.Static.EVENTSDB;
import static com.gabrielemaffoni.toastapp.utils.Static.EWHEN;
import static com.gabrielemaffoni.toastapp.utils.Static.EYEAR;
import static com.gabrielemaffoni.toastapp.utils.Static.FRIENDSDB;
import static com.gabrielemaffoni.toastapp.utils.Static.REFUSED;
import static com.gabrielemaffoni.toastapp.utils.Static.UDB;
import static com.gabrielemaffoni.toastapp.utils.Static.UID;
import static com.gabrielemaffoni.toastapp.utils.Static.UNAME;
import static com.gabrielemaffoni.toastapp.utils.Static.UPROFPIC;
import static com.gabrielemaffoni.toastapp.utils.Static.USURNAME;

/**
 * This is the activity that appears in home screen after logging in or registering.
 *
 * @author 40284943
 * @version 2.3a
 */

public class HomeActivity extends AppCompatActivity {


    final ArrayList<Friend> friendArrayList = new ArrayList<>(); //the list of friends we are going to show in the grid
    DatabaseReference friendsDatabase;
    DatabaseReference eventsDatabase;
    private FirebaseAuth firebaseAuth;
    private String cUID;
    private View eventExists;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        //Set the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Check if the user is logged in
        firebaseAuth = FirebaseAuth.getInstance();

        //If not, we go back to the Login
        if (firebaseAuth.getCurrentUser() == null) {
            startLogin();
        }



        try {

            //Save the current user ID
            cUID = firebaseAuth.getCurrentUser().getUid();

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();

            //Checking if events child exists
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(EVENTSDB)) {
                        //if it exists, we go inside and check if there is any event of the current user
                        eventsDatabase = FirebaseDatabase.getInstance().getReference().child(EVENTSDB);
                        checkIfEventAlreadyExist(eventsDatabase, savedInstanceState);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            //Find the path to the friends and events database
            friendsDatabase = FirebaseDatabase.getInstance().getReference().child(FRIENDSDB).child(cUID);



            //We check on the database if there are new events
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.app_name);
            toolbar.inflateMenu(R.menu.menu_home);
            final GridView grid = (GridView) findViewById(R.id.friends_list);


            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.add_user:
                            searchUser();
                            break;
                        case R.id.logout:
                            logOut();
                            break;

                    }

                    generateFriendsGrid(friendsDatabase, grid);

                    return true;
                }
            });

        } catch (NullPointerException e) {
            startLogin();
            Toast.makeText(getApplicationContext(), "Sorry, problems with the database", Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            startLogin();
            Toast.makeText(getApplicationContext(), "Sorry, problems with the database", Toast.LENGTH_SHORT).show();
        }

    }

    private void logOut() {
        firebaseAuth.signOut();
        Intent signIn = new Intent(getApplicationContext(), Login.class);
        startActivity(signIn);
        finish();
        Toast.makeText(getApplicationContext(), "You logged out", Toast.LENGTH_SHORT).show();

    }

    private void searchUser() {
        Intent searchUser = new Intent(getApplicationContext(), SearchUser.class);
        startActivity(searchUser);
    }

    public void startLogin() {
        finish();
        Intent login = new Intent(this, Login.class);
        startActivity(login);

    }

    private void checkIfEventAlreadyExist(final DatabaseReference eventsDatabase, final Bundle savedInstanceState) {


        eventsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //If there is a new event we call the method to download it and find out if it's still active
                if (dataSnapshot.hasChild(cUID)) {
                    downloadAndShowEventData(eventsDatabase, savedInstanceState);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventExists = findViewById(R.id.event_exists);
                eventExists.setVisibility(View.GONE);
            }
        });


    }

    private void generateFriendsGrid(DatabaseReference friendsDatabase, final GridView grid) {

        friendsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            //If something changes in the database (like we delete a friend or someone adds us)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //We clear the database
                friendArrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //We download all the friends again and add them to the List
                    Friend friend = postSnapshot.getValue(Friend.class);
                    friend.setUserId(postSnapshot.getKey());
                    friend.convertAvatar(friend.getUserProfilePic());
                    friendArrayList.add(friend);
                }

                //Then we show them in the grid
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

                inviteFriend(position);

            }
        });

        //On long press shows the context menu
        registerForContextMenu(grid);

    }

    public void inviteFriend(int position) {
        Intent singleEvent = new Intent(getApplicationContext(), EventActivity.class);

        singleEvent.putExtra("profile_picture", friendArrayList.get(position).getUserProfilePic());
        singleEvent.putExtra("name", friendArrayList.get(position).getUserName());
        singleEvent.putExtra("surname", friendArrayList.get(position).getUserSurname());
        singleEvent.putExtra("user_id", friendArrayList.get(position).getUserId());

        startActivity(singleEvent);
    }

    public void deleteFriend(int position) {
        DatabaseReference otherFriendDb = FirebaseDatabase.getInstance().getReference().child(FRIENDSDB).child(friendArrayList.get(position).getUserId());
        friendsDatabase.child(friendArrayList.get(position).getUserId()).removeValue();
        otherFriendDb.child(cUID).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(getApplicationContext(), "Friend removed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(friendArrayList.get(info.position).getUserName() + " " + friendArrayList.get(info.position).getUserSurname());
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_long_press_grid, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_friend:
                deleteFriend(info.position);
                break;
            case R.id.info:
                showFriendsProfile(info.position);
                break;
            case R.id.invite:
                inviteFriend(info.position);
                break;
        }

        return true;
    }

    private void showFriendsProfile(int position) {
        Intent showProfile = new Intent(this.getApplicationContext(), Profile.class);
        showProfile.putExtra("name", friendArrayList.get(position).getUserName());
        showProfile.putExtra("surname", friendArrayList.get(position).getUserSurname());
        showProfile.putExtra("email", friendArrayList.get(position).getUserEmail());
        showProfile.putExtra("avatar", friendArrayList.get(position).getUserProfilePic());
        startActivity(showProfile);
    }


    private void downloadAndShowEventData(final DatabaseReference eventsDatabase, final Bundle savedInstanceState) {

        final Query evenToDownload = eventsDatabase.child(cUID).getRef();

        evenToDownload.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //We check first of all if the date is not already expired.
                Calendar today = Calendar.getInstance();
                Calendar tomorrow = Calendar.getInstance();
                tomorrow.add(Calendar.DAY_OF_MONTH, 1);
                HashMap<String, Object> data;

                /*
                !IMPORTANT
                This if is important for the compatibility between the different versions of android:
                in the case of Android 25 we don't upload GregorianCalendar, but and HashMap of data.
                In the case of Android 22 - 23 we upload a GregorianCalendar.
                Therefore when we download we have to check which type of data has been uploaded.
                 */
                if (dataSnapshot.child(EWHEN).child(ETIME).exists()) {
                    data = (HashMap<String, Object>) dataSnapshot.child(EWHEN).child(ETIME).getValue();
                } else {
                    data = (HashMap<String, Object>) dataSnapshot.child(EWHEN).getValue();
                }

                int eventDate = Integer.valueOf(String.valueOf(data.get(EDATE)));
                int todayInt = today.get(Calendar.DAY_OF_MONTH);
                int tomorrowInt = tomorrow.get(Calendar.DAY_OF_MONTH);

                //If it's still active we show event data
                if (eventDate == todayInt || eventDate == tomorrowInt) {
                    showEventData(dataSnapshot, savedInstanceState);
                } else {
                    eventsDatabase.child(cUID).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            eventExists = findViewById(R.id.event_exists);
                            eventExists.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {
                    showEventData(dataSnapshot, savedInstanceState);
                } else {
                    eventExists.findViewById(R.id.event_exists);
                    eventExists.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                eventExists.findViewById(R.id.event_exists);
                eventExists.setVisibility(View.GONE);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void showEventData(DataSnapshot dataSnapshot, Bundle savedInstanceState) {

        //If we are allowed to show the event.
        HashMap<String, Object> activeAndLocationData = (HashMap<String, Object>) dataSnapshot.getValue();

        //We store all the data into the variables
        int eventActive = Integer.valueOf(String.valueOf(activeAndLocationData.get(EACTIVE)));
        String eventAddress = String.valueOf(activeAndLocationData.get(EADDRESS));
        double eventLat = Double.valueOf(String.valueOf(activeAndLocationData.get(ELAT)));
        double eventLon = Double.valueOf(String.valueOf(activeAndLocationData.get(ELON)));
        String eventLocationName = String.valueOf(activeAndLocationData.get(ELOCATION));
        int eventType = Integer.valueOf(String.valueOf(activeAndLocationData.get(ETYPE)));
        String senderID = String.valueOf(activeAndLocationData.get(ESENDERID));

        //We query the data of the sender
        HashMap<String, Object> receiverData = (HashMap<String, Object>) dataSnapshot.child(ERECEIVER).getValue();

        //And we store them into variables
        String eventRUID = String.valueOf(receiverData.get(ERUID)); //gets the ID of the Sender
        String eventRUName = String.valueOf(receiverData.get(ERUNAME)); //gets the Name of the sender
        int eventRUProfPic = Integer.valueOf(String.valueOf(receiverData.get(ERUPRPIC))); //gets the avatar of the sender
        String eventRUSurname = String.valueOf(receiverData.get(ERUSURNAME)); //gets the surname of the sender
        HashMap<String, Object> dateData;
        //We take the time values

        //To make sure that our compatibility works, we add a passage to verify if the event has been created from version 25 => we check if in the database there is an object = to
        if (dataSnapshot.child(EWHEN).child(ETIME).exists()) {
            dateData = (HashMap<String, Object>) dataSnapshot.child(EWHEN).child(ETIME).getValue();
        } else {
            dateData = (HashMap<String, Object>) dataSnapshot.child(EWHEN).getValue();
        }


        //We store them into variables
        int eventHour = Integer.valueOf(String.valueOf(dateData.get(EHOUR)));
        int eventMinute = Integer.valueOf(String.valueOf(dateData.get(EMINUTE)));
        int eventDate = Integer.valueOf(String.valueOf(dateData.get(EDATE)));
        int eventMonth = Integer.valueOf(String.valueOf(dateData.get(EMONTH)));
        int eventYear = Integer.valueOf(String.valueOf(dateData.get(EYEAR)));


        //We create the sender object
        Friend sender = new Friend(
                eventRUID,
                eventRUName,
                eventRUSurname,
                eventRUProfPic
        );

        //THe date object
        GregorianCalendar date = new GregorianCalendar(
                eventYear,
                eventMonth,
                eventDate,
                eventHour,
                eventMinute
        );

        //And finally the event object
        Event downloadedEvent = new Event(
                sender,
                date,
                eventActive,
                eventType,
                eventLocationName,
                eventAddress,
                eventLat,
                eventLon,
                senderID
        );


        //We check if the sender is who is using the app right now
        boolean isSender = cUID.equals(String.valueOf(downloadedEvent.getSenderID()));

        showValuesOnCard(downloadedEvent, savedInstanceState, isSender);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle args = new Bundle();

        eventsDatabase = FirebaseDatabase.getInstance().getReference().child(EVENTSDB).child(cUID);


        if (eventsDatabase != null) {
            downloadAndShowEventData(eventsDatabase, args);
        } else {
            eventExists.setVisibility(View.GONE);
        }
    }

    private void showValuesOnCard(final Event event, Bundle savedInstanceState, final boolean isSender) {

        //Setting visibility of the card
        eventExists = findViewById(R.id.event_exists);
        eventExists.setVisibility(View.VISIBLE);

        //We find the card in the includer
        final CardView cardBottom = (CardView) eventExists.findViewById(R.id.total_card_notif);

        //We check if the user of the phone is the sender
        if (isSender) {
            //if so, we display just the "waiting for" display
            View waitingFor = eventExists.findViewById(R.id.waiting_for);
            waitingFor.setVisibility(View.VISIBLE);

            if (event.getActive() == ACCEPTED) {
                //When the event's status changes, we display the total card with no buttons
                waitingFor.setVisibility(View.GONE);
            }

        }


        //We find the everything in the view
        ImageView typeEvent = (ImageView) eventExists.findViewById(R.id.what_notif);
        final ImageView senderProfPic = (ImageView) eventExists.findViewById(R.id.receiver_notif);
        TextView day = (TextView) eventExists.findViewById(R.id.day_notif);
        TextView time = (TextView) eventExists.findViewById(R.id.time_notif);
        MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.map_picked_conf_notif);
        TextView whereName = (TextView) eventExists.findViewById(R.id.where_name_notif);
        TextView address = (TextView) eventExists.findViewById(R.id.where_address_notif);


        final Button accept = (Button) eventExists.findViewById(R.id.accept_notif);
        final Button iCant = (Button) eventExists.findViewById(R.id.sorry_notif);

        //If the receiver accepts we hide the buttons
        if (event.getActive() == ACCEPTED) {
            accept.setVisibility(View.GONE);
            iCant.setVisibility(View.GONE);
        } else {
            //otherwise we keep seeing them
            accept.setVisibility(View.VISIBLE);
            iCant.setVisibility(View.VISIBLE);
        }

        //showing the data from event downloaded
        typeEvent.setImageResource(Event.findRightImageResource(event.getType()));

        //get user profile picture
        if (isSender) {
            event.getReceiver().convertAvatar(event.getReceiver().getUserProfilePic());

        } else {
            Query findSender = FirebaseDatabase.getInstance().getReference().child(UDB).child(event.getSenderID());

            findSender.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot senderDataSnapshot) {
                    HashMap<String, Object> senderData = (HashMap<String, Object>) senderDataSnapshot.getValue();
                    int profilePic = Integer.valueOf(String.valueOf(senderData.get(UPROFPIC)));
                    Friend sender = new Friend(
                            String.valueOf(senderData.get(UID)),
                            String.valueOf(senderData.get(UNAME)),
                            String.valueOf(senderData.get(USURNAME))

                    );
                    sender.convertAvatar(profilePic);
                    event.setReceiver(
                            sender
                    );

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //convert and set user profile picture
        event.getReceiver().convertAvatar(event.getReceiver().getUserProfilePic());
        senderProfPic.setImageResource(event.getReceiver().getUserProfilePic());

        //We set the day
        day.setText(Event.setTodayOrTomorrow(event.getWhen()));

        //We display the time
        Date timer = event.getWhen().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

        time.setText(formatter.format(timer));


        //Showing map
        map.onCreate(savedInstanceState);
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng finalPlace = new LatLng(event.getLat(), event.getLon());
                googleMap.addMarker(new MarkerOptions().position(finalPlace));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(finalPlace, 15));
            }
        });

        whereName.setText(event.getLocation_name());
        address.setText(event.getAddress());

        //if the user clicks on "Accept"
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptEvent(event, iCant, accept, isSender);
            }
        });

        //else if he/she clicks on icant
        iCant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCantEvent(event);

            }
        });

    }


    private void acceptEvent(final Event event, final Button iCant, final Button accepted, final boolean isSender) {
        DatabaseReference accept = FirebaseDatabase.getInstance().getReference().child(EVENTSDB);
        //On sender's event
        accept.child(cUID).child(event.getReceiver().getUserId()).child(EACTIVE).setValue(ACCEPTED).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                iCant.setVisibility(View.GONE);
                accepted.setVisibility(View.GONE);

                if (isSender) {
                    View waitingFor = eventExists.findViewById(R.id.waiting_for);
                    waitingFor.setVisibility(View.GONE);

                }
            }
        });
        //On Receiver's event
        accept.child(event.getReceiver().getUserId()).child(cUID).child(EACTIVE).setValue(ACCEPTED).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                iCant.setVisibility(View.GONE);
                accepted.setVisibility(View.GONE);

                if (isSender) {
                    View waitingFor = eventExists.findViewById(R.id.waiting_for);
                    waitingFor.setVisibility(View.GONE);
                }

            }
        });

    }


    //if the user tap on "iCant", we delete the event from the database and it automatically deletes it from the app
    private void iCantEvent(Event event) {
        DatabaseReference accept = FirebaseDatabase.getInstance().getReference().child(EVENTSDB);
        accept.child(cUID).child(event.getReceiver().getUserId()).child(EACTIVE).setValue(REFUSED);//On sender's event
        accept.child(event.getReceiver().getUserId()).child(cUID).child(EACTIVE).setValue(REFUSED); //On receiver's event
        accept.child(cUID).removeValue();

        accept.child(event.getReceiver().getUserId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                eventExists.setVisibility(View.GONE);
            }
        });

    }

}



