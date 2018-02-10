package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shameer on 2018-02-10.
 */

public class ContactListActivity extends AppCompatActivity {
    FloatingActionButton mCreateContactFAB;
    RecyclerView mContactListRecyclerView;
    ContactAdapter mContactAdapter;
    ArrayList<Contact> mContactList;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mContactsDatabaseReference;
    ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        mContactListRecyclerView = (RecyclerView) findViewById(R.id.contactListRecyclerView);
        mContactListRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mContactListRecyclerView.setLayoutManager(layoutManager);
        mContactListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mContactListRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        mContactList = new ArrayList<>();
        mContactAdapter = new ContactAdapter(mContactList, this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        getDataFromFirebase();

        mCreateContactFAB = (FloatingActionButton) findViewById(R.id.addContactFAB);
        mCreateContactFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this, ContactAddActivity.class);
                startActivity(intent);
            }
        });
    }



    private void getDataFromFirebase() {
        mContactsDatabaseReference = mFirebaseDatabase.getReference().child("contacts");
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Contact contact = new Contact();
                    contact = dataSnapshot.getValue(Contact.class);
                    mContactList.add(contact);
                    mContactListRecyclerView.setAdapter(mContactAdapter);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mContactsDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }
}
