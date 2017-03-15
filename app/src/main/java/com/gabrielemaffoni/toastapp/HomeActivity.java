package com.gabrielemaffoni.toastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.gabrielemaffoni.toastapp.to.Event;
import com.gabrielemaffoni.toastapp.to.Friend;
import com.google.android.gms.ads.MobileAds;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;



import static com.gabrielemaffoni.toastapp.utils.Static.*;

//TODO add notifications (check onChildChanged - or similar) - ask teacher (!IMPORTANT)
//TODO Add personal settings(!IMPORTANT)
//TODO Add possibility to change avatar (< IMPORTANT)
public class HomeActivity extends AppCompatActivity {


    final ArrayList<Friend> friendArrayList = new ArrayList<>();
    DatabaseReference friendsDatabase;
    DatabaseReference eventsDatabase;
    private FirebaseAuth firebaseAuth;
    private String cUID;
    private View eventExists;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
        eventsDatabase = FirebaseDatabase.getInstance().getReference().child(EVENTSDB).child(cUID);

        eventsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    downloadAndShowEventData(eventsDatabase, savedInstanceState);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_home);
        final GridView grid = (GridView) findViewById(R.id.friends_list);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_user:
                        Intent activity = new Intent(getApplicationContext(), SearchUser.class);
                        startActivity(activity);
                        break;
                    case R.id.logout:
                        firebaseAuth.signOut();
                        finish();
                        Toast.makeText(getApplicationContext(), "You logged out", Toast.LENGTH_SHORT).show();
                        Intent signIn = new Intent(getApplicationContext(), Login.class);
                        startActivity(signIn);
                        break;
                    case R.id.settings:
                        //Intent settings = new Intent(getApplicationContext(), Settings.class);
                        //startActivity(settings);
                        //doNothing
                        break;
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
                    friend.convertAvatar(friend.getUserProfilePic());
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

    private void setGridDesignData(final GridView grid, FriendsAdapter adapter) {
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
        Query eventToDownload = eventsDatabase.getRef();

        eventToDownload.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                showEventData(dataSnapshot, savedInstanceState);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                    showEventData(dataSnapshot, savedInstanceState);


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                eventExists = findViewById(R.id.event_exists);
                eventExists.setVisibility(View.GONE);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventExists = findViewById(R.id.event_exists);
                eventExists.setVisibility(View.GONE);
            }
        });


    }

    private void showEventData(DataSnapshot dataSnapshot, Bundle savedInstanceState) {

        //QUERY DATA FOR LOCATION AND TYPE
        HashMap<String, Object> activeAndLocationData = (HashMap<String, Object>) dataSnapshot.getValue();

        int eventActive = Integer.valueOf(String.valueOf(activeAndLocationData.get(EACTIVE)));
        String eventAddress = String.valueOf(activeAndLocationData.get(EADDRESS));
        double eventLat = Double.valueOf(String.valueOf(activeAndLocationData.get(ELAT)));
        double eventLon = Double.valueOf(String.valueOf(activeAndLocationData.get(ELON)));
        String eventLocationName = String.valueOf(activeAndLocationData.get(ELOCATION));
        int eventType = Integer.valueOf(String.valueOf(activeAndLocationData.get(ETYPE)));
        String senderID = String.valueOf(activeAndLocationData.get(ESENDERID));
        //QUERY DATA FOR SENDER
        HashMap<String, Object> receiverData = (HashMap<String, Object>) dataSnapshot.child(ERECEIVER).getValue();

        String eventRUID = String.valueOf(receiverData.get(ERUID)); //gets the ID of the Sender
        String eventRUName = String.valueOf(receiverData.get(ERUNAME)); //gets the Name of the sender
        int eventRUProfPic = Integer.valueOf(String.valueOf(receiverData.get(ERUPRPIC))); //gets the avatar of the sender
        String eventRUSurname = String.valueOf(receiverData.get(ERUSURNAME));

        //QUERY DATA FOR TIME AND DATE
        HashMap<String, Object> dateData = (HashMap<String, Object>) dataSnapshot.child(EWHEN).child(ETIME).getValue();

        int eventHour = Integer.valueOf(String.valueOf(dateData.get(EHOUR)));
        int eventMinute = Integer.valueOf(String.valueOf(dateData.get(EMINUTE)));
        int eventDate = Integer.valueOf(String.valueOf(dateData.get(EDATE)));
        int eventMonth = Integer.valueOf(String.valueOf(dateData.get(EMONTH)));
        int eventYear = Integer.valueOf(String.valueOf(dateData.get(EYEAR)));

        Friend sender = new Friend(
                eventRUID,
                eventRUName,
                eventRUSurname,
                eventRUProfPic
        );

        GregorianCalendar date = new GregorianCalendar(
                eventYear,
                eventMonth,
                eventDate,
                eventHour,
                eventMinute
        );

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
        //Setting visibility of the include
        eventExists = findViewById(R.id.event_exists);
        eventExists.setVisibility(View.VISIBLE);

        final CardView cardBottom = (CardView) eventExists.findViewById(R.id.total_card_notif);

        if (isSender) {
            View waitingFor = eventExists.findViewById(R.id.waiting_for);
            waitingFor.setVisibility(View.VISIBLE);
            if (event.getActive() == ACCEPTED) {

                waitingFor.setVisibility(View.GONE);
            }

        }


        //finding in the view everything
        ImageView typeEvent = (ImageView) eventExists.findViewById(R.id.what_notif);
        ImageView senderProfPic = (ImageView) eventExists.findViewById(R.id.receiver_notif);
        TextView day = (TextView) eventExists.findViewById(R.id.day_notif);
        TextView time = (TextView) eventExists.findViewById(R.id.time_notif);
        MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.map_picked_conf_notif);
        TextView whereName = (TextView) eventExists.findViewById(R.id.where_name_notif);
        TextView address = (TextView) eventExists.findViewById(R.id.where_address_notif);

        final Button accept = (Button) eventExists.findViewById(R.id.accept_notif);
        final Button iCant = (Button) eventExists.findViewById(R.id.sorry_notif);

        if (event.getActive() == ACCEPTED) {
            accept.setVisibility(View.GONE);
            iCant.setVisibility(View.GONE);
        } else {
            accept.setVisibility(View.VISIBLE);
            iCant.setVisibility(View.VISIBLE);
        }

        //showing the data from event downloaded
        typeEvent.setImageResource(Event.findRightImageResource(event.getType()));
        senderProfPic.setImageResource(event.getReceiver().getUserProfilePic());
        day.setText(Event.setTodayOrTomorrow(event.getWhen()));
        time.setText(event.getWhen().get(Calendar.HOUR_OF_DAY) + ":" + event.getWhen().get(Calendar.MINUTE));


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

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptEvent(event, iCant, accept, isSender, cardBottom);
            }
        });

        iCant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCantEvent(event);

            }
        });

    }

    private void acceptEvent(final Event event, final Button iCant, final Button accepted, final boolean isSender, final CardView cardBottom) {
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



