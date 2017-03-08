package com.gabrielemaffoni.toastapp1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabrielemaffoni.toastaapp1.R;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class WhatCard extends Fragment {

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSatate){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.what_card,container,false);
        return rootView;
    }

    public static WhatCard newInstance() {

        Bundle args = new Bundle();

        WhatCard fragment = new WhatCard();
        fragment.setArguments(args);
        return fragment;
    }
}
