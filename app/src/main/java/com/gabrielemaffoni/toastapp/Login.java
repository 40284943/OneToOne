package com.gabrielemaffoni.toastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This activity shows the email and password field and two buttons: login and register (if people are not registered yet).
 *
 * @author 40284943
 * @version 1.3
 */

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText userEmail;
    private EditText password;
    private Button submit;
    private Button register;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        //Check if the user has previously logged in
        firebaseAuth = FirebaseAuth.getInstance();

        //if so it directly goes to the homepage
        if (firebaseAuth.getCurrentUser() != null) {
            goHome();
        }

        //find the various fields
        userEmail = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        //Set on click
        submit.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    private void userLogin() {

        resetBottomLineColors();

        try {

            //get what's typed by the user
            String user = userEmail.getText().toString();
            String pass = password.getText().toString();

            //check in the database of Firebase if it exists
            firebaseAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //if so
                        goHome();
                    }
                }
            });

        } catch (IllegalArgumentException e) {
            //if there has been an error, it shows a red line under the right field and asks to the user to check again
            if (userEmail.getText().toString().isEmpty()) {
                setBottomLineColor(userEmail, R.color.colorRed);
            }
            if (password.getText().toString().isEmpty()) {
                setBottomLineColor(password, R.color.colorRed);
            }
            Toast.makeText(getApplicationContext(), "Please check again", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetBottomLineColors() {

        userEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*
                *UX improvement:
                *if the user is typing something after making a mistake, the field automatically reset itself in normal
                */
                setBottomLineColor(userEmail, R.color.colorAccent);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 /*
                *UX improvement:
                *if the user is typing something after making a mistake, the field automatically reset itself in normal
                */
                setBottomLineColor(password, R.color.colorAccent);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //goes to register activity
    private void userRegister() {
        Intent registerActivity = new Intent(getApplicationContext(), Register.class);
        finish();
        startActivity(registerActivity);
    }


    @Override
    public void onClick(View view) {
        if (view == submit) {
            userLogin();
        } else if (view == register) {
            userRegister();
        }
    }

    /**
     * This method set the bottom line of a field to the chosen color
     *
     * @param textField     The field where you have to change the color
     * @param colorResource The color you want to change it to
     */
    public void setBottomLineColor(EditText textField, int colorResource) {
        textField.setBackgroundTintList(getResources().getColorStateList(colorResource));
    }

    private void goHome() {
        finish();
        Intent startApp = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(startApp);
    }
}
