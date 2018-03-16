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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

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
                    sortArray(mMeetingArray);
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

    private void sortArray(ArrayList<Meeting> arrayList) {
        Collections.sort(arrayList, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting meeting1, Meeting meeting2) {
                Date date1 = convertStringToDate(meeting1.getDate());
                Date date2 = convertStringToDate(meeting2.getDate());

                if (date1.compareTo(date2) == 0) {
                    Date time1 = convertStringToTime(meeting1.getTime());
                    Date time2 = convertStringToTime(meeting2.getTime());
                    return (time1.compareTo(time2));
                }
                return date1.compareTo(date2);
            }
        });
    }

    private Date convertStringToDate(String dateString) {
        DateFormat format = new SimpleDateFormat("EE, dd MMMM yyyy", Locale.ENGLISH);
        try {
            Date date = format.parse(dateString);
            return date;
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Could not convert string to date", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private Date convertStringToTime(String timeString) {
        DateFormat format = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        try {
            Date time = format.parse(timeString);
            return time;
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Could not convert string to date", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
