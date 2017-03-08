package com.gabrielemaffoni.toastapp1;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.gabrielemaffoni.toastaapp1.R;

/**
 * Created by gabrielemaffoni on 08/03/2017.
 */

public class Event extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event_layout);

        ViewPager pager = (ViewPager) findViewById(R.id.cards);
    }
}
