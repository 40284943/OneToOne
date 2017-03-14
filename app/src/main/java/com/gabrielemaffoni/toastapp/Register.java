package com.gabrielemaffoni.toastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gabrielemaffoni.toastapp.to.User;
import com.gabrielemaffoni.toastapp.utils.Static;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.gabrielemaffoni.toastapp.utils.Static.*;


/**
 * Created by gabrielemaffoni on 09/03/2017.
 */

//TODO add the possibility to choose the avatar.

public class Register extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference db;
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;
    private Button register;
    private Button signIn;
    private FirebaseAuth firebaseAuth;
    private ImageView standardAvatar;
    private ImageView avatar1;
    private ImageView avatar2;
    private ImageView avatar3;
    private ImageView avatar4;
    private int avatarChosen;
    private RelativeLayout chooseAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        avatarChosen = AVATAR_STANDARD;
        name = (EditText) findViewById(R.id.name_register);
        surname = (EditText) findViewById(R.id.surname_register);
        email = (EditText) findViewById(R.id.email_register);
        password = (EditText) findViewById(R.id.password_register);
        standardAvatar = (ImageView) findViewById(R.id.change_profile_pic);
        chooseAvatar = (RelativeLayout) findViewById(R.id.avatar_to_choose);
        avatar1 = (ImageView) findViewById(R.id.avatar_1_choose);
        avatar2 = (ImageView) findViewById(R.id.avatar_2_choose);
        avatar3 = (ImageView) findViewById(R.id.avatar_3_choose);
        avatar4 = (ImageView) findViewById(R.id.avatar_4_choose);

        register = (Button) findViewById(R.id.register_register);
        signIn = (Button) findViewById(R.id.goto_signin);

        register.setOnClickListener(this);
        signIn.setOnClickListener(this);
        standardAvatar.setOnClickListener(this);
    }


    private void registerUser() {
        final String userName = name.getText().toString().trim();
        final String userSurname = surname.getText().toString().trim();
        final String userEmail = email.getText().toString().trim();
        final String userPassword = password.getText().toString().trim();
        final int userProfilePic = avatarChosen;
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                db = FirebaseDatabase.getInstance().getReference();
                writeNewUser(userName, userSurname, userEmail, userPassword, userProfilePic);
                finish();
                Intent startApp = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(startApp);

            }
        });
    }

    private void writeNewUser(String name, String surname, String email, String password, int profilePic) {
        //FIXME add condition for the username and password to be empty.

        User user = new User(
                firebaseAuth.getCurrentUser().getUid(),
                name,
                surname,
                email,
                password,
                profilePic);

        db.child(UDB).child(user.getUserId()).setValue(user);

    }

    private void chooseProfilePic() {
        chooseAvatar.setVisibility(View.VISIBLE);

        avatar1.setOnClickListener(this);
        avatar2.setOnClickListener(this);
        avatar3.setOnClickListener(this);
        avatar4.setOnClickListener(this);

    }

    private void clickOnAvatar(int avatar) {
        standardAvatar.setImageResource(avatar);
        avatarChosen = avatar;
        chooseAvatar.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {


        if (view == register) {
            registerUser();
        } else if (view == signIn) {
            finish();
            Intent signInApp = new Intent(this, Login.class);
            startActivity(signInApp);
        } else if (view == standardAvatar) {
            chooseProfilePic();
        } else if (view == avatar1) {
            clickOnAvatar(AVATAR1);
        } else if (view == avatar2) {
            clickOnAvatar(AVATAR2);
        } else if (view == avatar3) {
            clickOnAvatar(AVATAR3);
        } else if (view == avatar4) {
            clickOnAvatar(AVATAR4);
        }
    }
}
