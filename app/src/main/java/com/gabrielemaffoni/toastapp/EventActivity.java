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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class EventActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int NUM_PAGES = 2;


    private ViewPager viewPager;
    private PagerAdapter adapter;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event_layout);

        Bundle previousIntent = getIntent().getExtras();
        int coverImage = previousIntent.getInt("Profile picture");
        String nameInvited = previousIntent.getString("Name");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        viewPager = (ViewPager) findViewById(R.id.cards);
        ImageView profilePicChosen = (ImageView) findViewById(R.id.profile_pic_chosen);
        TextView nameChosen = (TextView) findViewById(R.id.name_invited);
        profilePicChosen.setImageResource(coverImage);
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
        if (DateAndTime.IS_PRESSED) {

        /*
        If so, it goes to the previous visualisation
         */

            View preconfirmation = findViewById(R.id.preconfirmation_layout);
            preconfirmation.setVisibility(View.GONE);
            DateAndTime.IS_PRESSED = false;

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
                    return WhatCard.newInstance();

                case 1:
                    return DateAndTime.newInstance();

                default:
                    return WhatCard.newInstance();
            }

        }
    }


}
