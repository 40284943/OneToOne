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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gabrielemaffoni.toastapp.objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR1;
import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR2;
import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR3;
import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR4;
import static com.gabrielemaffoni.toastapp.utils.Static.AVATAR_STANDARD;
import static com.gabrielemaffoni.toastapp.utils.Static.UDB;


/**
 * This activity shows the field necessary from the user to register on the database.
 * The two buttons are made to go to the login screen (in case of a mistake from the user) or to register and go to the homescreen.
 *
 * @author 40284943
 * @version 1.2
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

        //Connects to the database
        firebaseAuth = FirebaseAuth.getInstance();

        //check again if the user is already logged in
        if (firebaseAuth.getCurrentUser() != null) {
            goHome();
        }


        avatarChosen = 0; //as standard, the initial avatar is the number 0 -> the standard
        //Find all the views
        findViews();

        //set click of the different field
        register.setOnClickListener(this);
        signIn.setOnClickListener(this);
        standardAvatar.setOnClickListener(this);

    }

    private void findViews() {
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

    }


    private void registerUser() {

        //we reset all the bottom lines
        resetBottomLines();

        try {

            //take all the input
            final String userName = name.getText().toString().trim();
            final String userSurname = surname.getText().toString().trim();
            final String userEmail = email.getText().toString().trim();
            final String userPassword = password.getText().toString().trim();


            //create on the database the new user
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    db = FirebaseDatabase.getInstance().getReference();
                    //and writes it also in the "users" JSON child
                    writeNewUser(userName, userSurname, userEmail, avatarChosen);
                    //we start the home
                    goHome();

                }
            });

        } catch (IllegalArgumentException e) {
            //Security check: if there has been any mistake, we set the bottom line as red under where there has been any mistake
            if (name.getText().toString().isEmpty()) {
                setBottomLineColor(name, R.color.colorRed);
                Toast.makeText(getApplicationContext(), "Please fill every space", Toast.LENGTH_SHORT).show();
            }
            if (surname.getText().toString().isEmpty()) {
                setBottomLineColor(surname, R.color.colorRed);
                Toast.makeText(getApplicationContext(), "Please fill every space", Toast.LENGTH_SHORT).show();

            }

            if (email.getText().toString().isEmpty()) {
                setBottomLineColor(email, R.color.colorRed);
                Toast.makeText(getApplicationContext(), "Please fill every space", Toast.LENGTH_SHORT).show();

            }
            if (password.getText().toString().isEmpty()) {
                setBottomLineColor(password, R.color.colorRed);
                Toast.makeText(getApplicationContext(), "Please fill every space", Toast.LENGTH_SHORT).show();

            }
        }

    }

    private void writeNewUser(String name, String surname, String email, int profilePic) {

        try {
            //create a new user
            User user = new User(
                    firebaseAuth.getCurrentUser().getUid(),
                    name,
                    surname,
                    email,
                    profilePic);

            //Write it on the "users" (UDB) JSON child under it UserID.
            db.child(UDB).child(user.getUserId()).setValue(user);

        } catch (NullPointerException e) {
            Toast.makeText(this, "Problem with the registration, try again later.", Toast.LENGTH_SHORT).show();
        }


    }

    private void chooseProfilePic() {
        chooseAvatar.setVisibility(View.VISIBLE);

        avatar1.setOnClickListener(this);
        avatar2.setOnClickListener(this);
        avatar3.setOnClickListener(this);
        avatar4.setOnClickListener(this);

    }

    private void clickOnAvatar(int avatar) {

        //Check which avatar has been clicked
        standardAvatar.setImageResource(avatar);
        switch (avatar) {
            case AVATAR1:
                avatarChosen = 1;
                break;
            case AVATAR2:
                avatarChosen = 2;
                break;
            case AVATAR3:
                avatarChosen = 3;
                break;
            case AVATAR4:
                avatarChosen = 4;
                break;
            default:
                avatarChosen = 0;
        }
        chooseAvatar.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {


        if (view == register) {
            registerUser();
        } else if (view == signIn) {
            goToLogin();
        }
        if (view == standardAvatar) {
            chooseProfilePic();
            //When it's time to choose the avatar, we set the avatar also in the preview
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

    private void resetBottomLines() {
        setLineBottomNormal(name);
        setLineBottomNormal(surname);
        setLineBottomNormal(email);
        setLineBottomNormal(password);
    }

    private void setLineBottomNormal(final EditText editText) {


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setBottomLineColor(editText, R.color.colorAccent);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    private void setBottomLineColor(EditText textField, int resourceColorId) {
        textField.setBackgroundTintList(getResources().getColorStateList(resourceColorId));
    }

    private void goHome() {
        finish();
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void goToLogin() {
        finish();
        Intent signInApp = new Intent(this, Login.class);
        startActivity(signInApp);
    }

}
