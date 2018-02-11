package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
    TextView mContactItemName;
    Button mUpdateNameButton;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mContactsDatabaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_contact_detail);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_contact_detail, frameLayout);

        final Intent intent = getIntent();



        mContactItemName = (TextView) findViewById(R.id.contactItemName);
        mContactItemName.setText(intent.getStringExtra("name"));

        mUpdateNameButton = (Button) findViewById(R.id.updateNameBtn);
        mUpdateNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = intent.getStringExtra("id");
                System.out.println("ID " + id);
                firebaseAuth = FirebaseAuth.getInstance();
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                Contact contact = new Contact("Mary", "", "", "", "Sweden", "", "", "", "", "", "", "", intent.getStringExtra("uid"));

                mContactsDatabaseReference = mFirebaseDatabase.getReference("contacts");
                mContactsDatabaseReference.child(firebaseUser.getUid()).child(id).setValue(contact);
            }
        });

    }
}
