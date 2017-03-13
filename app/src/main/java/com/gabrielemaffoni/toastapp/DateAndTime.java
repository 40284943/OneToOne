package com.gabrielemaffoni.toastapp;

import android.content.Intent;
import android.os.Bundle;
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

import com.gabrielemaffoni.toastapp.to.Event;
import com.gabrielemaffoni.toastapp.to.Friend;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.gabrielemaffoni.toastapp.EventActivity.event;
import static com.gabrielemaffoni.toastapp.utils.Static.*;


/**
 *
 * TODO: Add data sharing with confirmation (!IMPORTANT)
 * TODO: Add code to set the event on the database (!IMPORTANT)
 */

public class DateAndTime extends Fragment implements AdapterView.OnItemSelectedListener {


    private static String HALF_HOUR = "30";
    private static String TOTAL_HOUR = "00";
    DatabaseReference db;
    FirebaseAuth firebaseAuth;
    private Spinner day;
    private Spinner time;
    private Switch addLocation;
    private MapView mapView;

    private FloatingActionButton okay;

    private Calendar currentDay;
    private String selectedDay;
    private GregorianCalendar todayCal;
    private GregorianCalendar dayChosen;
    private TextView nameLocation;
    private TextView addressLocation;

    //NEw instance to navigate through the adapterview
    public static DateAndTime newInstance(Bundle args) {

        DateAndTime fragment = new DateAndTime();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        //Inflate the data in the adapterview
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.date_time_location_alt, container, false);

        //get all the arguments from the previous fragment
        Bundle args = getArguments();
        final int type = args.getInt("type");

        //find in the View the elements
        findIntialElements(rootView, savedInstanceState);

        getCurrentSelectedDate(day, time);

        //set the final date of the event
        event.setWhen(dayChosen);

        setWhere();

        showPreconfirmationCard(type, okay);

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
                Place place = PlaceAutocomplete.getPlace(this.getContext(), data);
                event.setPlace(place);

                //show the mapsection
                LinearLayout mapSection = (LinearLayout) getView().findViewById(R.id.map_section);
                mapSection.setVisibility(View.VISIBLE);

                //find the elements inside mapsection
                nameLocation = (TextView) getView().findViewById(R.id.name_location);
                addressLocation = (TextView) getView().findViewById(R.id.address_location);
                mapView = (MapView) getView().findViewById(R.id.mapView);

                //Copy the name and the location of the place
                nameLocation.setText(event.getPlace().getName());
                addressLocation.setText(event.getPlace().getAddress());

                mapView.onCreate(getArguments());
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng placePicked = event.getPlace().getLatLng();
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
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
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

        if (now < 22) {

            for (int i = now; i < 24; i++) {
                timeFromNow.add(i + ":" + HALF_HOUR);
                timeFromNow.add(i + ":" + TOTAL_HOUR);
            }
        } else {
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

            dayChosen.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeChosen.substring(0, 2).toString()));
            dayChosen.set(Calendar.MINUTE, Integer.valueOf(timeChosen.substring(3)));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }

    public void showPreconfirmationCard(final int type, FloatingActionButton button) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View preConfirmation = getView().getRootView().findViewById(R.id.preconfirmation_layout);
                ImageView what = (ImageView) preConfirmation.findViewById(R.id.what);
                ImageView receiverProfilePic = (ImageView) preConfirmation.findViewById(R.id.receiver);
                TextView date = (TextView) preConfirmation.findViewById(R.id.day);
                TextView time = (TextView) preConfirmation.findViewById(R.id.time);
                TextView where = (TextView) preConfirmation.findViewById(R.id.where_name);
                TextView whereAddress = (TextView) preConfirmation.findViewById(R.id.where_address);
                MapView mapPicked = (MapView) preConfirmation.findViewById(R.id.map_picked_conf);
                Button done = (Button) preConfirmation.findViewById(R.id.done);
                IS_PRESSED = true;

                //Set the event image
                what.setImageResource(findRightImageResource(event.getType()));
                receiverProfilePic.setImageResource(event.getReceiver().getUserProfilePic());
                date.setText(selectedDay);
                time.setText(event.getWhen().get(Calendar.HOUR_OF_DAY) + ":" + event.getWhen().get(Calendar.MINUTE));


                if (event.getPlace().getAddress() != null) {
                    where.setText(event.getPlace().getName());
                    whereAddress.setText(event.getPlace().getAddress());
                    mapPicked.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng finalPlace = event.getPlace().getLatLng();
                            googleMap.addMarker(new MarkerOptions().position(finalPlace));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(finalPlace, 200));
                        }
                    });
                } else {
                    CardView mapIf = (CardView) preConfirmation.findViewById(R.id.where_map);
                    mapIf.setVisibility(View.INVISIBLE);
                }


                preConfirmation.setVisibility(View.VISIBLE);

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setDatabaseUser();
                    }


                });
            }


        });

    }


    private void setDatabaseUser() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child(UID);


        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        db.getParent().child(EVENTSDB).child(currentUserId).child(event.getReceiver().getUserId()).setValue(event);
        final Event eventOpposite = event;

        final DatabaseReference dbToAddFriendIn = FirebaseDatabase.getInstance().getReference().child(EVENTSDB);

        Query findCurrentUserToAdd = db.orderByKey().equalTo(currentUserId);
        findCurrentUserToAdd.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
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
                eventOpposite.setReceiver(friendWhoSearched);

                dbToAddFriendIn.child(event.getReceiver().getUserId()).child(eventOpposite.getReceiver().getUserId()).setValue(eventOpposite);
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

    private int findRightImageResource(int type) {
        //Check which event it is
        int imageResource = 0;

        switch (type) {
            case BEER:
                imageResource = R.drawable.ic_beer;
                break;
            case COCKTAIL:
                imageResource = R.drawable.ic_cocktail;
                break;
            case LUNCH:
                imageResource = R.drawable.ic_lunch;
                break;
            case COFFEE:
                imageResource = R.drawable.ic_coffee;
                break;
        }

        return imageResource;
    }
}



