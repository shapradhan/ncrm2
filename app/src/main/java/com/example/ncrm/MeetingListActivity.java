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
 * Created by shameer on 2018-02-15.
 */

public class MeetingListActivity extends MainActivity {
    private RecyclerView mMeetingArrayRecyclerView;
    private MeetingAdapter mMeetingAdapter;
    private ArrayList<Meeting> mMeetingArray;
    private FirebaseDatabase mFirebaseDatabase;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_meeting_list, frameLayout);

        mMeetingArrayRecyclerView = (RecyclerView) findViewById(R.id.meetingListRecyclerView);
        mMeetingArrayRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mMeetingArrayRecyclerView.setLayoutManager(layoutManager);
        mMeetingArrayRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMeetingArrayRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        mMeetingArray = new ArrayList<>();
        mMeetingAdapter = new MeetingAdapter(mMeetingArray, this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        getDataFromFirebase();

        FloatingActionButton mCreateMeetingFAB = (FloatingActionButton) findViewById(R.id.addMeetingFAB);
        mCreateMeetingFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetingListActivity.this, MeetingAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataFromFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference meetingsDatabaseReference = mFirebaseDatabase.getReference().child("meetings").child(uid);
        attachDatabaseReadListener(meetingsDatabaseReference);
    }

    private void attachDatabaseReadListener(DatabaseReference meetingsDatabaseReference) {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Meeting meeting = dataSnapshot.getValue(Meeting.class);
                    String id = dataSnapshot.getKey();
                    meeting.setId(id);
                    mMeetingArray.add(meeting);
                    mMeetingArrayRecyclerView.setAdapter(mMeetingAdapter);
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
            meetingsDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }
}
