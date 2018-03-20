package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by KN on 2018-03-16.
 */

public class TargetDetailActivity extends MainActivity {
    private Target mSelectedTarget;
    private ArrayList<String> mParticipantNamesArray = new ArrayList<>();
    private ListView mParticipantListView;
    private FirebaseDatabase mFirebaseDatabase;
    private String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_target_detail, frameLayout);

        Intent intent = getIntent();
        mSelectedTarget = (Target) intent.getSerializableExtra("object");

        TextView targetTitle = (TextView) findViewById(R.id.targetTitle);
        targetTitle.setText(mSelectedTarget.getTargetItem());

        TextView targetDate = (TextView) findViewById(R.id.targetDateEditText);
        targetDate.setText(mSelectedTarget.getTargetDate());

        TextView targetTime = (TextView) findViewById(R.id.targetTimeEditText);
        targetTime.setText(mSelectedTarget.getTargetTime());

        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference targetParticipantDatabaseReference = mFirebaseDatabase.getReference()
                .child("targets")
                .child(uid)
                .child(mSelectedTarget.getId())
                .child("participants");

        mParticipantListView = (ListView) findViewById(R.id.participantList);

        addTargetParticipantValueEventListener(targetParticipantDatabaseReference);
    }

    private void addTargetParticipantValueEventListener(DatabaseReference targetParticipantDatabaseReference) {
//        targetParticipantDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    DatabaseReference targetParticipantNameDatabaseReference = mFirebaseDatabase.getReference()
//                            .child("contacts")
//                            .child(uid)
//                            .child(ds.getKey())
//                            .child("name");
//
//                    targetParticipantNameDatabaseReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            mParticipantNamesArray.add(dataSnapshot.getValue().toString());
//                            ArrayAdapter<String> participantListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mParticipantNamesArray);
//                            mParticipantListView.setAdapter(participantListAdapter);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

    }
}
