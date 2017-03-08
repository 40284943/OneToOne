package com.gabrielemaffoni.toastapp1;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.gabrielemaffoni.toastaapp1.R;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class Event extends FragmentActivity {
    private static final int NUM_PAGES = 2;

    private ViewPager viewPager;
    private PagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event_layout);

        viewPager = (ViewPager) findViewById(R.id.cards);
        ImageView profilePicChosen = (ImageView) findViewById(R.id.profile_pic_chosen);
        profilePicChosen.setImageResource(R.drawable.ic_user);
        adapter = new Adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

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
                default:
                    return WhatCard.newInstance();
            }

        }
    }




}
