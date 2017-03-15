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
 * Created by gabrielemaffoni on 09/03/2017.
 *
 *
 *
 */

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private Button submit;
    private Button register;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            Intent startApp = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(startApp);
        }
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        submit.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    private void userLogin() {
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setBottomLineColor(username,R.color.colorAccent);
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
                setBottomLineColor(username,R.color.colorAccent);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        try {

            String user = username.getText().toString();
            String pass = password.getText().toString();
            firebaseAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        finish();
                        Intent startApp = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(startApp);
                    }
                }
            });

        } catch (IllegalArgumentException e){

            if (username.getText().toString().isEmpty()) {
                setBottomLineColor(username,R.color.colorRed);
            }
            if (password.getText().toString().isEmpty()){
                setBottomLineColor(password,R.color.colorRed);
            }
            Toast.makeText(getApplicationContext(), "Please check again", Toast.LENGTH_SHORT).show();
        }
    }

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

    public void setBottomLineColor(EditText textField, int colorResource){
        textField.setBackgroundTintList(getResources().getColorStateList(colorResource));
    }
}
