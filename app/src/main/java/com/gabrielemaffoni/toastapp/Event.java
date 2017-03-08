package com.gabrielemaffoni.toastapp;

import android.content.Intent;
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


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class Event extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int NUM_PAGES = 2;


    private ViewPager viewPager;
    private PagerAdapter adapter;
    private GoogleApiClient mGoogleApiClient;
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event_layout);

        Bundle previousIntent = getIntent().getExtras();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        viewPager = (ViewPager) findViewById(R.id.cards);
        ImageView profilePicChosen = (ImageView) findViewById(R.id.profile_pic_chosen);
        profilePicChosen.setImageResource(R.drawable.ic_user);
        adapter = new Adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Result", result.toString());
    }

    public class Adapter extends FragmentPagerAdapter{
        public Adapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public int getCount(){
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position){
            switch (position){
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
