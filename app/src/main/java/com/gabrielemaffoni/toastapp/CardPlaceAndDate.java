package com.gabrielemaffoni.toastapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielemaffoni.toastapp.objects.Event;
import com.gabrielemaffoni.toastapp.objects.Friend;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.gabrielemaffoni.toastapp.EventActivity.currentItem;
import static com.gabrielemaffoni.toastapp.EventActivity.event;
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
import static com.gabrielemaffoni.toastapp.utils.Static.ESENDERID;
import static com.gabrielemaffoni.toastapp.utils.Static.ETYPE;
import static com.gabrielemaffoni.toastapp.utils.Static.EVENTSDB;
import static com.gabrielemaffoni.toastapp.utils.Static.EWHEN;
import static com.gabrielemaffoni.toastapp.utils.Static.EYEAR;
import static com.gabrielemaffoni.toastapp.utils.Static.IS_PRESSED;
import static com.gabrielemaffoni.toastapp.utils.Static.MAYBE;
import static com.gabrielemaffoni.toastapp.utils.Static.PLACE_AUTOCOMPLETE_REQUEST_CODE;
import static com.gabrielemaffoni.toastapp.utils.Static.TAG;
import static com.gabrielemaffoni.toastapp.utils.Static.UDB;
import static com.gabrielemaffoni.toastapp.utils.Static.UID;
import static com.gabrielemaffoni.toastapp.utils.Static.UNAME;
import static com.gabrielemaffoni.toastapp.utils.Static.UPROFPIC;
import static com.gabrielemaffoni.toastapp.utils.Static.USURNAME;


/**
 *  This fragment contains also a view of a Map in Lite Mde and the Autocomplete module of Google Places API.
 *
 *  @author 40284943
 *  @version 2.6
 */

public class CardPlaceAndDate extends Fragment implements AdapterView.OnItemSelectedListener {


    private static String HALF_HOUR = "30";
    private static String TOTAL_HOUR = "00";
    private DatabaseReference db;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private Spinner day;
    private Spinner time;
    private Switch addLocation;
    private SupportMapFragment mapView;

    private FloatingActionButton okay;

    private Calendar currentDay;
    private String selectedDay;
    private GregorianCalendar todayCal;
    private GregorianCalendar dayChosen;
    private TextView nameLocation;
    private TextView addressLocation;
    private Friend receiver;

    private Place place;

    //New instance to navigate through the adapterview
    public static CardPlaceAndDate newInstance(Bundle args) {
        currentItem = 1;
        CardPlaceAndDate fragment = new CardPlaceAndDate();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        //Inflate the data in the adapterview
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.date_time_location_alt, container, false);

        //connects to the database
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child(EVENTSDB);
        currentUserId = firebaseAuth.getCurrentUser().getUid();


        receiver = event.getReceiver();

        //get all the arguments from the previous fragment
        Bundle args = getArguments();
        final int type = args.getInt("type");

        //find in the View the elements
        findIntialElements(rootView, savedInstanceState);
        getCurrentSelectedDate(day, time);

        //set the final date of the event
        event.setWhen(dayChosen);
        setWhere();

        //Shows the confirmation card onClick
        showPreconfirmationCard(okay, savedInstanceState);

        return rootView;
    }


    private void setWhere() {
        addLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //opens the activity to find the right location
                    try {
                        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException e) {
                        //HANDLER
                    } catch (GooglePlayServicesNotAvailableException e) {
                        //HANDLER
                    }


                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                place = PlaceAutocomplete.getPlace(this.getContext(), data);
                event.setAddress(place.getAddress().toString());
                event.setLocation_name(place.getName().toString());
                event.setLat(place.getLatLng().latitude);
                event.setLon(place.getLatLng().longitude);

                //show the mapsection
                LinearLayout mapSection = (LinearLayout) getView().findViewById(R.id.map_section);
                mapSection.setVisibility(View.VISIBLE);

                //find the elements inside mapsection
                nameLocation = (TextView) getView().findViewById(R.id.name_location);
                addressLocation = (TextView) getView().findViewById(R.id.address_location);
                mapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);

                //Copy the name and the location of the place
                nameLocation.setText(place.getName());
                addressLocation.setText(place.getAddress());

                mapView.onCreate(getArguments());
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng placePicked = place.getLatLng();
                        googleMap.addMarker(new MarkerOptions().position(placePicked));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placePicked, 20));
                    }
                });

                //Shows a preview of the map


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this.getContext(), data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                addLocation = (Switch) getView().findViewById(R.id.add_location);
                addLocation.setChecked(false);
            }
        }
    }

    private void findIntialElements(ViewGroup rootView, Bundle savedInstanceState) {
        day = (Spinner) rootView.findViewById(R.id.day_spinner);
        time = (Spinner) rootView.findViewById(R.id.time_spinner);
        addLocation = (Switch) rootView.findViewById(R.id.add_location);
        mapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        okay = (FloatingActionButton) rootView.findViewById(R.id.okay);
    }


    private void getCurrentSelectedDate(Spinner day, final Spinner time) {

        //Get current day
        currentDay = Calendar.getInstance();
        //changing it to gregorian calendar to manage it easily
        todayCal = new GregorianCalendar(
                currentDay.get(Calendar.YEAR),
                currentDay.get(Calendar.MONTH),
                currentDay.get(Calendar.DAY_OF_MONTH)
        );


        event.setWhen(todayCal);

        //We show the possible timeslots from the moment the user access to the app
        setTimeFromNow(time, currentDay);

        selectedDay = "Today";

        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(getContext().getApplicationContext(), R.array.day_dropdown, R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        day.setAdapter(dayAdapter);


        //Checking if there are changes on tab selecting
        day.setOnItemSelectedListener(this);
        time.setOnItemSelectedListener(this);


    }

    private void setTimeFromNow(Spinner timeList, Calendar calendar) {


        int now = calendar.get(Calendar.HOUR_OF_DAY);


        ArrayList<String> timeFromNow = new ArrayList<>();

        //if the current time is later than 10PM, it doesn't give any timeslot.
        if (now < 22) {

            for (int i = now; i < 24; i++) {
                timeFromNow.add(i + ":" + HALF_HOUR);
                timeFromNow.add(i + ":" + TOTAL_HOUR);
            }
        } else {
            //but it shows an alternative
            timeFromNow.add(getContext().getString(R.string.sorry_tomorrow));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext().getApplicationContext(), R.layout.simple_spinner_item, timeFromNow);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        dayChosen = todayCal;
        selectedDay = "Today";
        timeList.setAdapter(adapter);

    }

    private void setTimeForTomorrow(Spinner timeList) {


        ArrayList<String> timeForTomorrow = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                timeForTomorrow.add("0" + i + ":" + HALF_HOUR);
                timeForTomorrow.add("0" + i + ":" + TOTAL_HOUR);
            } else {
                timeForTomorrow.add(i + ":" + HALF_HOUR);
                timeForTomorrow.add(i + ":" + TOTAL_HOUR);
            }
        }
        GregorianCalendar tomorrowCal;
        tomorrowCal = todayCal;
        tomorrowCal.add(Calendar.DAY_OF_MONTH, 1);

        event.setWhen(tomorrowCal);
        dayChosen = tomorrowCal;
        selectedDay = "Tomorrow";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext().getApplicationContext(), R.layout.simple_spinner_item, timeForTomorrow);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        timeList.setAdapter(adapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;


        if (spinner.getId() == R.id.day_spinner) {
            switch (parent.getSelectedItemPosition()) {
                case 0:
                    setTimeFromNow(time, currentDay);
                    break;
                case 1:
                    setTimeForTomorrow(time);
                    break;
                default:
                    setTimeFromNow(time, currentDay);
                    break;
            }
        } else if (spinner.getId() == R.id.time_spinner) {

            String timeChosen = spinner.getSelectedItem().toString();

            try {
                dayChosen.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeChosen.substring(0, 2).toString()));
                dayChosen.set(Calendar.MINUTE, Integer.valueOf(timeChosen.substring(3)));

            } catch (NumberFormatException e) {
                //Do nothing
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }

    public void showPreconfirmationCard(FloatingActionButton okay, final Bundle savedInstanceState) {
        //if they click on the floating button
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //we show the confirmation
                View preConfirmation = getView().getRootView().findViewById(R.id.preconfirmation_layout);
                ImageView what = (ImageView) preConfirmation.findViewById(R.id.what);
                ImageView receiverProfilePic = (ImageView) preConfirmation.findViewById(R.id.receiver);
                TextView date = (TextView) preConfirmation.findViewById(R.id.day);
                TextView time = (TextView) preConfirmation.findViewById(R.id.time);
                TextView where = (TextView) preConfirmation.findViewById(R.id.where_name);
                TextView whereAddress = (TextView) preConfirmation.findViewById(R.id.where_address);
                MapFragment mapPicked = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map_picked_conf);
                Button done = (Button) preConfirmation.findViewById(R.id.done);
                IS_PRESSED = true;  //this boolean will be useful after for the onBackPressed method

                //Set the event image
                what.setImageResource(Event.findRightImageResource(event.getType()));
                receiverProfilePic.setImageResource(event.getReceiver().getUserProfilePic());
                date.setText(selectedDay);

                //formats the time
                Date timer = event.getWhen().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                time.setText(formatter.format(timer));


                try {

                    where.setText(event.getLocation_name());
                    whereAddress.setText(event.getAddress());

                    mapPicked.onCreate(savedInstanceState);
                    mapPicked.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng finalPlace = new LatLng(event.getLat(), event.getLon());
                            googleMap.addMarker(new MarkerOptions().position(finalPlace));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(finalPlace, 200));
                        }
                    });

                } catch (NullPointerException e) {
                    CardView mapIf = (CardView) preConfirmation.findViewById(R.id.where_map);
                    mapIf.setVisibility(View.GONE);
                }


                preConfirmation.setVisibility(View.VISIBLE);

                done.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        event.setActive(MAYBE);
                        event.setSenderID(currentUserId);
                        setDatabaseUser();
                    }


                });
            }


        });

    }


    private void setDatabaseUser() {

        //add the event created to the database of the current user
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            try {
                db.child(currentUserId).child(event.getReceiver().getUserId()).setValue(event);
            } catch (DatabaseException e) {
                //Do nothing

            }
        } else {

            HashMap<String, Object> eventConverted = new HashMap<>();
            eventConverted.putAll(event.convertEventToHashmap(event.getReceiver(), currentUserId));
            db.child(currentUserId).child(event.getReceiver().getUserId()).setValue(eventConverted);
        }

        //now the opposite, add to the database of the one invited the opposite event, changing the data inside the event

        //let's find the database
        DatabaseReference usersDatabase = FirebaseDatabase.getInstance().getReference().child(UDB);

        //we search the data of the current user in our database
        Query findCurrentUserToAdd = usersDatabase.orderByKey().equalTo(currentUserId);


        findCurrentUserToAdd.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //we don't need to check if it exists
                HashMap<String, Object> searcher = (HashMap<String, Object>) dataSnapshot.getValue();

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
                //We call the database
                DatabaseReference databaseToAddTheOppositeEvent = FirebaseDatabase.getInstance().getReference().child(EVENTSDB);
                Event eventOpposite = new Event();

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {

                    //therefore we create a new event that's exactly like the one we already have
                    event.copyToEventExceptFriend(eventOpposite);
                    //but we change the receiver
                    eventOpposite.setReceiver(friendWhoSearched);

                    //Then we add everything to the database
                    databaseToAddTheOppositeEvent.child(event.getReceiver().getUserId()).child(currentUserId).setValue(eventOpposite);

                } else {

                    HashMap<String, Object> eventConverted = new HashMap<>();
                    eventConverted.putAll(event.convertEventToHashmap(friendWhoSearched, currentUserId));
                    databaseToAddTheOppositeEvent.child(event.getReceiver().getUserId()).child(currentUserId).setValue(eventConverted);
                }

                Toast.makeText(getContext().getApplicationContext(), "Event created", Toast.LENGTH_SHORT).show();

                getActivity().finish();
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
    }

}



