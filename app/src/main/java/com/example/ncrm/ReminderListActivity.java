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

/**
 * Created by shameer on 2018-03-09.
 */

public class ReminderListActivity extends MainActivity {
    RecyclerView mReminderListRecyclerView;
    ReminderAdapter mReminderAdapter;
    ArrayList<Reminder> mReminderList;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRemindersDatabaseReference;
    ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_reminder_list, frameLayout);

        mReminderListRecyclerView = (RecyclerView) findViewById(R.id.reminderListRecyclerView);
        mReminderListRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mReminderListRecyclerView.setLayoutManager(layoutManager);
        mReminderListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mReminderListRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        mReminderList = new ArrayList<>();
        mReminderAdapter = new ReminderAdapter(mReminderList, this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        getDataFromFirebase();

        FloatingActionButton createReminderFAB = (FloatingActionButton) findViewById(R.id.addReminderFAB);
        createReminderFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderListActivity.this, ReminderAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataFromFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRemindersDatabaseReference = mFirebaseDatabase.getReference().child("reminders").child(uid);
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Reminder reminder = dataSnapshot.getValue(Reminder.class);
                    String id = dataSnapshot.getKey();
                    reminder.setId(id);
                    mReminderList.add(reminder);
                    mReminderListRecyclerView.setAdapter(mReminderAdapter);
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
            mRemindersDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }
}
