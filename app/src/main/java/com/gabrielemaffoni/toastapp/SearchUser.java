package com.gabrielemaffoni.toastapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.gabrielemaffoni.toastapp.utils.Static.*;

/**
 * Created by gabrielemaffoni on 10/03/2017.
 */

public class SearchUser extends AppCompatActivity {

    DatabaseReference db;
    private SearchView searchView;
    private Button addUser;
    private TextView userFoundName;
    private RelativeLayout foundUserSection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user);

        db = FirebaseDatabase.getInstance().getReference().child(UDB);

        searchView = (SearchView) findViewById(R.id.userSearch);
        foundUserSection = (RelativeLayout) findViewById(R.id.foundUser);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
