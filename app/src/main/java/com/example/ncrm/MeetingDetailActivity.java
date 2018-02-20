package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by shameer on 2018-02-19.
 */

public class MeetingDetailActivity extends MainActivity {
    private Meeting mSelectedMeeting;
    private ArrayList<String> mParticipantNamesArray = new ArrayList<>();
    private ListView mParticipantListView;
    private FirebaseDatabase mFirebaseDatabase;
    private String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_meeting_detail, frameLayout);

        Intent intent = getIntent();
        mSelectedMeeting = (Meeting) intent.getSerializableExtra("object");

        TextView meetingTitle = (TextView) findViewById(R.id.meetingTitle);
        meetingTitle.setText(mSelectedMeeting.getTitle());

        TextView meetingDate = (TextView) findViewById(R.id.meetingDate);
        meetingDate.setText(mSelectedMeeting.getDate());

        TextView meetingTime = (TextView) findViewById(R.id.meetingTime);
        meetingTime.setText(mSelectedMeeting.getTime());

        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference meetingParticipantDatabaseReference = mFirebaseDatabase.getReference()
                .child("meetings")
                .child(uid)
                .child(mSelectedMeeting.getId())
                .child("participants");

        mParticipantListView = (ListView) findViewById(R.id.participantList);

        addMeetingParticipantValueEventListener(meetingParticipantDatabaseReference);
    }

    private void addMeetingParticipantValueEventListener(DatabaseReference meetingParticipantDatabaseReference) {
        meetingParticipantDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DatabaseReference meetingParticipantNameDatabaseReference = mFirebaseDatabase.getReference()
                            .child("contacts")
                            .child(uid)
                            .child(ds.getKey())
                            .child("name");

                    meetingParticipantNameDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mParticipantNamesArray.add(dataSnapshot.getValue().toString());
                            ArrayAdapter<String> participantListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mParticipantNamesArray);
                            mParticipantListView.setAdapter(participantListAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
