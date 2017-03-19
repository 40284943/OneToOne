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
import static com.gabrielemaffoni.toastapp.utils.Static.BEER;
import static com.gabrielemaffoni.toastapp.utils.Static.COCKTAIL;
import static com.gabrielemaffoni.toastapp.utils.Static.COFFEE;
import static com.gabrielemaffoni.toastapp.utils.Static.LUNCH;


/**
 * This fragment shows four buttons to rapidly choose what you want to take with your friend.
 *
 * @author 40284943
 * @version 2.0
 */

public class CardEventType extends Fragment implements View.OnClickListener {

    private ImageView beer;
    private ImageView cocktail;
    private ImageView coffee;
    private ImageView lunch;

    public static CardEventType newInstance(Bundle args) {
        currentItem = 0;
        CardEventType fragment = new CardEventType();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.what_card, container, false);


        findViewsAndSetData(rootView);

        beer.setOnClickListener(this);
        cocktail.setOnClickListener(this);
        coffee.setOnClickListener(this);
        lunch.setOnClickListener(this);

        return rootView;
    }

    private void findViewsAndSetData(ViewGroup rootView) {
        beer = (ImageView) rootView.findViewById(R.id.beer);
        cocktail = (ImageView) rootView.findViewById(R.id.cocktail);
        coffee = (ImageView) rootView.findViewById(R.id.coffee);
        lunch = (ImageView) rootView.findViewById(R.id.lunch);

        beer.setImageResource(R.drawable.ic_beer);
        cocktail.setImageResource(R.drawable.ic_cocktail);
        coffee.setImageResource(R.drawable.ic_coffee);
        lunch.setImageResource(R.drawable.ic_lunch);
    }

    @Override
    public void onClick(View v) {

        CardPlaceAndDate when = new CardPlaceAndDate();
        int typeToPut;

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

        //Switch to the other card
        FragmentTransaction manager = getFragmentManager().beginTransaction();
        ((EventActivity) getActivity()).setCurrentItem(1, true);
        manager.addToBackStack("Transaction1");
        manager.commit();


    }


}

