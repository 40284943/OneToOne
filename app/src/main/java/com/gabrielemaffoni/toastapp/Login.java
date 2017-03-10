package com.gabrielemaffoni.toastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by gabrielemaffoni on 09/03/2017.
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
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

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
}
