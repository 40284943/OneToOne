package com.gabrielemaffoni.toastapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gabrielemaffoni.toastapp.objects.Friend;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR1;
import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR2;
import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR3;
import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR4;
import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR_STANDARD;

/**
 * Created by gabrielemaffoni on 09/03/2017.
 */

public class AddUser extends AppCompatActivity {

    public int avatarFinal = AVATAR_STANDARD;
    DatabaseReference db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        db = FirebaseDatabase.getInstance().getReference();

        final EditText name = (EditText) findViewById(R.id.name);
        final EditText surname = (EditText) findViewById(R.id.surname);
        final ImageView avatarToChoose = (ImageView) findViewById(R.id.choose_avatar_button);

        final ImageView avatar1 = (ImageView) findViewById(R.id.avatar_1);
        final ImageView avatar2 = (ImageView) findViewById(R.id.avatar_2);
        final ImageView avatar3 = (ImageView) findViewById(R.id.avatar_3);
        final ImageView avatar4 = (ImageView) findViewById(R.id.avatar_4);

        avatarToChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout chooseAvatar = (LinearLayout) findViewById(R.id.choose_avatar_section);
                chooseAvatar.setVisibility(View.VISIBLE);

                avatar1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setAvatar(avatarToChoose, chooseAvatar, AVATAR1);
                    }
                });

                avatar2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setAvatar(avatarToChoose, chooseAvatar, AVATAR2);
                    }
                });
                avatar3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setAvatar(avatarToChoose, chooseAvatar, AVATAR3);
                    }
                });
                avatar4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setAvatar(avatarToChoose, chooseAvatar, AVATAR4);
                    }
                });
            }
        });


        Button add = (Button) findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewUser(avatarFinal, name.getText().toString(), surname.getText().toString());
                Toast.makeText(getBaseContext(), name.getText().toString() + " " + surname.getText().toString() + " added", Toast.LENGTH_SHORT).show();
                finish();

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


    private final void setAvatar(ImageView avatarToChoose, LinearLayout chooseSection, int avatar) {

        avatarToChoose.setImageResource(avatar);
        chooseSection.setVisibility(View.GONE);
        switch (avatar) {
            case AVATAR1:
                avatarFinal = 1;
                break;
            case AVATAR2:
                avatarFinal = 2;
                break;
            case AVATAR3:
                avatarFinal = 3;
                break;
            case AVATAR4:
                avatarFinal = 4;
                break;
            default:
                avatarFinal = 0;
        }

    }

}
