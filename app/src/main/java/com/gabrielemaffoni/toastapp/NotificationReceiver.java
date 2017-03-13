package com.gabrielemaffoni.toastapp;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by gabrielemaffoni on 13/03/2017.
 */

public class NotificationReceiver extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
    }
}
