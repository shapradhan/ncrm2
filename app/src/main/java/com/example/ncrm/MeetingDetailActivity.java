package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    Meeting mSelectedMeeting;
    ArrayList<String> participantNamesArray = new ArrayList<>();

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
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference meetingParticipantsDatabaseReference = firebaseDatabase.getReference()
                .child("meetings")
                .child(user.getUid())
                .child(mSelectedMeeting.getId())
                .child("participants");

        final ListView participantListView = (ListView) findViewById(R.id.participantList);

        meetingParticipantsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DatabaseReference meetingParticipantNameDatabaseReference = firebaseDatabase.getReference()
                            .child("contacts")
                            .child(user.getUid())
                            .child(ds.getKey())
                            .child("name");

                    meetingParticipantNameDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            participantNamesArray.add(dataSnapshot.getValue().toString());
                            ArrayAdapter<String> participantListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, participantNamesArray);
                            participantListView.setAdapter(participantListAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
}
