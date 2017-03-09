package com.gabrielemaffoni.toastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gabrielemaffoni.toastapp.to.Friend;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by gabrielemaffoni on 09/03/2017.
 */

public class AddUser extends AppCompatActivity {

    DatabaseReference db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        db = FirebaseDatabase.getInstance().getReference();

        final EditText name = (EditText) findViewById(R.id.name);
        final EditText surname = (EditText) findViewById(R.id.surname);

        Button add = (Button) findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewUser(0, name.getText().toString(), surname.getText().toString());
                Toast.makeText(getBaseContext(), name.getText().toString() + " " + surname.getText().toString() + " added", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void writeNewUser(int profilePic, String name, String surname) {
        Friend friend = new Friend(name, surname);
        String userId = db.push().getKey();
        friend.setUserProfilePic(profilePic);
        friend.setUserId(userId);
        db.child("users").child(userId).setValue(friend);
    }
}
