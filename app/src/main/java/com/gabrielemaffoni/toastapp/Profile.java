package com.gabrielemaffoni.toastapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This activity shows the basic info of a selected friend from a list
 *
 * @author 40284943
 * @version 1.0
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

        findViews();

        setData(
                home.getInt("avatar"),
                home.getString("name"),
                home.getString("surname"),
                home.getString("email")
        );

    }

    private void findViews() {
        profilePicture = (ImageView) findViewById(R.id.avatar_friend);
        name = (TextView) findViewById(R.id.name_friend);
        surname = (TextView) findViewById(R.id.surname_friend);
        email = (TextView) findViewById(R.id.email_friend);
    }

    private void setData(int avatar, String nameFriend, String surnameFriend, String emailFriend) {
        profilePicture.setImageResource(avatar);
        name.setText(nameFriend);
        surname.setText(surnameFriend);
        email.setText(emailFriend);
    }
}
