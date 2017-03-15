package com.gabrielemaffoni.toastapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.vision.text.Text;

/**
 * Created by gabrielemaffoni on 15/03/2017.
 */

public class Profile extends AppCompatActivity {
    private ImageView profilePicture;
    private TextView name;
    private TextView surname;
    private TextView email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_profile);

        Bundle home = getIntent().getExtras();
        findVisualComponents();
        setValueVisualComponents(
                home.getInt("avatar"),
                home.getString("name"),
                home.getString("surname"),
                home.getString("email")
        );

    }

    public void findVisualComponents() {
        profilePicture = (ImageView) findViewById(R.id.avatar_friend);
        name = (TextView) findViewById(R.id.name_friend);
        surname = (TextView) findViewById(R.id.surname_friend);
        email = (TextView) findViewById(R.id.email_friend);
    }

    public void setValueVisualComponents(int avatar, String nameFriend, String surnameFriend, String emailFriend) {
        profilePicture.setImageResource(avatar);
        name.setText(nameFriend);
        surname.setText(surnameFriend);
        email.setText(emailFriend);
    }
}
