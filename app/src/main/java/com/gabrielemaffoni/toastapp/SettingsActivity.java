package com.gabrielemaffoni.toastapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gabrielemaffoni.toastapp.classes.SettingsFragment;

/**
 * Created by gabrielemaffoni on 17/03/2017.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle home = getIntent().getExtras();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(home);
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }
}
