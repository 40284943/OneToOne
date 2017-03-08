package com.gabrielemaffoni.toastapp1;

import android.content.Intent;
import android.icu.util.GregorianCalendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gabrielemaffoni.toastaapp1.R;
import com.gabrielemaffoni.toastapp1.classes.Friend;
import com.gabrielemaffoni.toastapp1.classes.FriendsAdapter;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        ArrayList<Friend> friendArrayList;


        FriendsAdapter adapter = new FriendsAdapter(this,Friend.createListOfFriends());

        GridView grid = (GridView) findViewById(R.id.friends_list);

        grid.setAdapter(adapter);
        grid.setVerticalSpacing(60);
        grid.setNumColumns(3);
        grid.setColumnWidth(40);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent singleEvent = new Intent(getApplicationContext(), Event.class);
                startActivity(singleEvent);
            }
        });
    }
}
