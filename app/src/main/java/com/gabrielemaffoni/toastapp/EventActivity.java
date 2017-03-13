package com.gabrielemaffoni.toastapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gabrielemaffoni.toastapp.to.Event;
import com.gabrielemaffoni.toastapp.to.Friend;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import static com.gabrielemaffoni.toastapp.utils.Static.*;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */


//TODO: Add the change of upperpart once clicked the type of event

public class EventActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int NUM_PAGES = 2;
    public static Event event;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private GoogleApiClient mGoogleApiClient;
    private int profilePic;
    private String nameInvited;
    private String surnameInvited;
    private String invitedId;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event_layout);

        event = new Event();

        //get the name and the cover image of the person chosen
        Bundle previousIntent = getIntent().getExtras();
        Friend receiver = new Friend(
                previousIntent.getString("user_id"),
                previousIntent.getString("name"),
                previousIntent.getString("surname"),
                previousIntent.getInt("profile_picture")
        );
        profilePic = receiver.getUserProfilePic();
        nameInvited = receiver.getUserName();
        surnameInvited = receiver.getUserSurname();
        invitedId = receiver.getUserId();

        event.setReceiver(receiver);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


        viewPager = (ViewPager) findViewById(R.id.cards);
        ImageView profilePicChosen = (ImageView) findViewById(R.id.profile_pic_chosen);
        TextView nameChosen = (TextView) findViewById(R.id.name_invited);
        profilePicChosen.setImageResource(profilePic);
        nameChosen.setText(nameInvited);
        adapter = new Adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Result", result.toString());
    }

    @Override
    public void onBackPressed() {

        //The overridden method checks before if we are already at the end of the process of selecting all the appointments data
        if (IS_PRESSED) {

        /*
        If so, it goes to the previous visualisation
         */

            View preConfirmation = findViewById(R.id.preconfirmation_layout);
            preConfirmation.setVisibility(View.GONE);
            IS_PRESSED = false;

        /*
        Otherwise its behaviours are normal.
         */
        } else {
            int count = getFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }

    public class Adapter extends FragmentPagerAdapter {
        public Adapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Bundle argsEvent = new Bundle();
                    argsEvent.putInt("receiver_profile_pic", profilePic);
                    argsEvent.putString("receiver_name", nameInvited);
                    argsEvent.putString("receiver_surname", surnameInvited);
                    return WhatCard.newInstance(argsEvent);

                case 1:
                    Bundle argsDateAndTime = new Bundle();
                    return DateAndTime.newInstance(argsDateAndTime);

                default:
                    Bundle args = new Bundle();
                    args.putInt("receiver_profile_pic", profilePic);
                    args.putString("receiver_name", nameInvited);
                    args.putString("receiver_surname", surnameInvited);
                    return WhatCard.newInstance(args);
            }

        }
    }


}
