package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by shameer on 2018-02-10.
 */

public class ContactListActivity extends MainActivity {
    RecyclerView mContactListRecyclerView;
    ContactAdapter mContactAdapter;
    ArrayList<Contact> mContactList;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mContactsDatabaseReference;
    ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_contact_list, frameLayout);

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

        FloatingActionButton createContactFAB = (FloatingActionButton) findViewById(R.id.addContactFAB);
        createContactFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this, ContactAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataFromFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mContactsDatabaseReference = mFirebaseDatabase.getReference().child("contacts").child(uid);
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Contact contact = dataSnapshot.getValue(Contact.class);
                    String id = dataSnapshot.getKey();
                    contact.setId(id);
                    mContactList.add(contact);
                    sortArray(mContactList);
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

    private void sortArray(ArrayList<Contact> arrayList) {
        Collections.sort(arrayList, new Comparator<Contact>() {
            @Override
            public int compare(Contact contact1, Contact contact2) {
                return contact1.getName().compareTo(contact2.getName());
            }
        });
    }
}
