package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by shameer on 2018-02-10.
 */

public class ContactDetailActivity extends MainActivity {
    TextView mContactName;
    TextView mContactOrganization;
    TextView mContactFullAddress;
    Button mUpdateNameButton;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mContactsDatabaseReference;
    Contact selectedContact;

    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_contact_detail, frameLayout);

        intent = getIntent();

        selectedContact = (Contact) intent.getSerializableExtra("object");


        mContactName = (TextView) findViewById(R.id.contactItemName);
        mContactName.setText(selectedContact.getName());
        mContactOrganization = (TextView) findViewById(R.id.contactItemOrganization);
        mContactOrganization.setText(selectedContact.getOrganization());
        mContactFullAddress = (TextView) findViewById(R.id.contactItemFullAddress);
        mContactFullAddress.setText(selectedContact.getStreetAddress() + ", " + selectedContact.getCity() + ", " + selectedContact.getCountry());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_update).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_update:

                Intent intent = new Intent(ContactDetailActivity.this, ContactUpdateActivity.class);

                intent.putExtra("contact", selectedContact);
                startActivity(intent);
        }
        return true;
    }



}
