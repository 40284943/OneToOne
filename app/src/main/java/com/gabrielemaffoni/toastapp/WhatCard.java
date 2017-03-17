package com.gabrielemaffoni.toastapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static com.gabrielemaffoni.toastapp.EventActivity.currentItem;
import static com.gabrielemaffoni.toastapp.EventActivity.event;
import static com.gabrielemaffoni.toastapp.utils.Static.*;


/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class WhatCard extends Fragment implements View.OnClickListener {

    private ImageView beer;
    private ImageView cocktail;
    private ImageView coffee;
    private ImageView lunch;

    public static WhatCard newInstance(Bundle args) {
        currentItem = 0;
        WhatCard fragment = new WhatCard();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.what_card, container, false);
        beer = (ImageView) rootView.findViewById(R.id.beer);
        cocktail = (ImageView) rootView.findViewById(R.id.cocktail);
        coffee = (ImageView) rootView.findViewById(R.id.coffee);
        lunch = (ImageView) rootView.findViewById(R.id.lunch);

        beer.setImageResource(R.drawable.ic_beer);
        cocktail.setImageResource(R.drawable.ic_cocktail);
        coffee.setImageResource(R.drawable.ic_coffee);
        lunch.setImageResource(R.drawable.ic_lunch);

        beer.setOnClickListener(this);
        cocktail.setOnClickListener(this);
        coffee.setOnClickListener(this);
        lunch.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        DateAndTime when = new DateAndTime();
        int typeToPut = 0;
        //Pass the type of event and the data of the participant
        Bundle args = getArguments();

        switch (v.getId()) {
            case R.id.beer:
                typeToPut = BEER;
                break;
            case R.id.cocktail:
                typeToPut = COCKTAIL;
                break;
            case R.id.lunch:
                typeToPut = LUNCH;
                break;
            case R.id.coffee:
                typeToPut = COFFEE;
                break;
            default:
                typeToPut = 0;
        }

        args.putInt("type", typeToPut);
        event.setType(typeToPut);
        when.setArguments(args);

        //start transaction
        FragmentTransaction manager = getFragmentManager().beginTransaction();
        ((EventActivity) getActivity()).setCurrentItem(1, true);
        manager.addToBackStack("Transaction1");
        manager.commit();


    }


}

