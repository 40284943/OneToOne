package com.gabrielemaffoni.toastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.gabrielemaffoni.toastapp.to.Event;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.gabrielemaffoni.toastapp.utils.Static.*;


/**
 *
 */

public class DateAndTime extends Fragment {
    public static boolean IS_PRESSED = false;
    static int PLACE_AUTOCOMPLETE_REQUESTE_CODE = 1;
    static String TAG = "Places";
    private TabLayout day;
    private TabLayout time;
    private Switch addLocation;
    private MapView mapView;
    private GoogleMap googleMap;
    private FloatingActionButton okay;
    private Event event;

    public static DateAndTime newInstance() {

        Bundle args = new Bundle();

        DateAndTime fragment = new DateAndTime();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.date_time_location, container, false);
        Bundle args = getArguments();
        final int type = args.getInt("Type");


        day = (TabLayout) rootView.findViewById(R.id.day);
        time = (TabLayout) rootView.findViewById(R.id.time);
        addLocation = (Switch) rootView.findViewById(R.id.add_location);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        okay = (FloatingActionButton) rootView.findViewById(R.id.okay);


        addLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    //opens the activity to find the right location
                    try {
                        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUESTE_CODE);
                    } catch (GooglePlayServicesRepairableException e) {
                        //HANDLER
                    } catch (GooglePlayServicesNotAvailableException e) {
                        //HANDLER
                    }


                }
            }
        });

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View preConfirmation = getView().getRootView().findViewById(R.id.preconfirmation_layout);
                ImageView what = (ImageView) preConfirmation.findViewById(R.id.what);
                ImageView receiver = (ImageView) preConfirmation.findViewById(R.id.receiver);
                TextView date = (TextView) preConfirmation.findViewById(R.id.day);
                TextView time = (TextView) preConfirmation.findViewById(R.id.time);
                TextView where = (TextView) preConfirmation.findViewById(R.id.where_name);
                TextView whereAddress = (TextView) preConfirmation.findViewById(R.id.where_address);
                MapView mapPicked = (MapView) preConfirmation.findViewById(R.id.map_picked_conf);
                Button done = (Button) preConfirmation.findViewById(R.id.done);
                IS_PRESSED = true;

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

                //Set the event image
                what.setImageResource(imageResource);


                preConfirmation.setVisibility(View.VISIBLE);


            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUESTE_CODE) {
            if (resultCode == RESULT_OK) {
                final Place place = PlaceAutocomplete.getPlace(this.getContext(), data);


                //show the mapsection
                LinearLayout mapSection = (LinearLayout) getView().findViewById(R.id.map_section);
                mapSection.setVisibility(View.VISIBLE);

                //find the elements inside mapsection
                TextView nameLocation = (TextView) getView().findViewById(R.id.name_location);
                TextView addressLocation = (TextView) getView().findViewById(R.id.address_location);
                MapView mapView = (MapView) getView().findViewById(R.id.mapView);

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


}
