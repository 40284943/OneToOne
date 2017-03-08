package com.gabrielemaffoni.toastapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class WhatCard extends Fragment {

    private ImageView beer;
    private ImageView cocktail;
    private ImageView coffee;
    private ImageView lunch;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSatate){
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.what_card, container, false);
        beer = (ImageView) rootView.findViewById(R.id.beer);
        cocktail = (ImageView) rootView.findViewById(R.id.cocktail);
        coffee = (ImageView) rootView.findViewById(R.id.coffee);
        lunch = (ImageView) rootView.findViewById(R.id.lunch);

        beer.setImageResource(R.drawable.ic_beer);
        cocktail.setImageResource(R.drawable.ic_cocktail);
        coffee.setImageResource(R.drawable.ic_coffee);
        lunch.setImageResource(R.drawable.ic_lunch);

        beer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateAndTime when = new DateAndTime();
                FragmentTransaction manager = getFragmentManager().beginTransaction();
                manager.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
                manager.replace(R.id.what_card, when);
                manager.addToBackStack("Transaction1");
                manager.commit();
            }
        });
        return rootView;
    }

    public static WhatCard newInstance() {

        Bundle args = new Bundle();

        WhatCard fragment = new WhatCard();
        fragment.setArguments(args);
        return fragment;
    }
}
