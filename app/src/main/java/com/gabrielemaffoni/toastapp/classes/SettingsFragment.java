package com.gabrielemaffoni.toastapp.classes;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.gabrielemaffoni.toastapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.gabrielemaffoni.toastapp.utils.Static.*;

/**
 * Created by gabrielemaffoni on 17/03/2017.
 */

public class SettingsFragment extends PreferenceFragment {
    DatabaseReference db;
    FirebaseAuth firebaseAuth;
    private EditTextPreference name;
    private EditTextPreference surname;
    private EditTextPreference email;
    private EditTextPreference password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        String cUID = getArguments().getString("cUID");

        db = FirebaseDatabase.getInstance().getReference().child(UDB).child(cUID);

        name = (EditTextPreference) findPreference(getString(R.string.pref_name));
        surname = (EditTextPreference) findPreference(getString(R.string.pref_surname));
        email = (EditTextPreference) findPreference(getString(R.string.pref_email));
        password = (EditTextPreference) findPreference(getString(R.string.pref_password));

        downloadCurrentData(db);


    }


    private void downloadCurrentData(DatabaseReference database) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> data = (HashMap<String, Object>) dataSnapshot.getValue();
                name.setTitle(String.valueOf(data.get(UNAME)));
                surname.setTitle(String.valueOf(data.get(USURNAME)));
                email.setTitle(String.valueOf(data.get(UEMAIL)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
