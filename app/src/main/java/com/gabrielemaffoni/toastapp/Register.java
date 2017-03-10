package com.gabrielemaffoni.toastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gabrielemaffoni.toastapp.to.User;
import com.gabrielemaffoni.toastapp.utils.Static;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by gabrielemaffoni on 09/03/2017.
 */

public class Register extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference db;
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;
    private Button register;
    private Button signIn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        name = (EditText) findViewById(R.id.name_register);
        surname = (EditText) findViewById(R.id.surname_register);
        email = (EditText) findViewById(R.id.email_register);
        password = (EditText) findViewById(R.id.password_register);

        register = (Button) findViewById(R.id.register_register);
        signIn = (Button) findViewById(R.id.goto_signin);

        register.setOnClickListener(this);
        signIn.setOnClickListener(this);
    }


    private void registerUser() {
        final String userName = name.getText().toString().trim();
        final String userSurname = surname.getText().toString().trim();
        final String userEmail = email.getText().toString().trim();
        final String userPassword = password.getText().toString().trim();
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                db = FirebaseDatabase.getInstance().getReference();
                writeNewUser(userName, userSurname, userEmail, userPassword);
                finish();
                Intent startApp = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(startApp);

            }
        });
    }

    private void writeNewUser(String name, String surname, String email, String password) {
        User user = new User(name, surname, email, password);

        user.setUserId(firebaseAuth.getCurrentUser().getUid());

        user.setUserProfilePic(Static.AVATAR_STANDARD);

        db.child("users").child(user.getUserId()).setValue(user);

    }

    @Override
    public void onClick(View view) {
        if (view == register) {
            registerUser();
        } else if (view == signIn) {
            finish();
            Intent signInApp = new Intent(this, Login.class);
            startActivity(signInApp);
        }
    }
}
