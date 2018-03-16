package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private String mUid;
    private String mMeetingId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_meeting_detail, frameLayout);

        Intent intent = getIntent();
        mSelectedMeeting = (Meeting) intent.getSerializableExtra("object");
        mMeetingId = mSelectedMeeting.getId();

        TextView meetingTitle = (TextView) findViewById(R.id.meetingTitle);
        meetingTitle.setText(mSelectedMeeting.getTitle());

        TextView meetingDate = (TextView) findViewById(R.id.meetingDate);
        meetingDate.setText(mSelectedMeeting.getDate());

        TextView meetingTime = (TextView) findViewById(R.id.meetingTime);
        meetingTime.setText(mSelectedMeeting.getTime());

        getDataFromFirebase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_update).setVisible(true);
        menu.findItem(R.id.action_delete).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_update:
                intent = new Intent(MeetingDetailActivity.this, MeetingUpdateActivity.class);
                intent.putExtra("meeting", mSelectedMeeting);
                startActivity(intent);
                break;
            case R.id.action_delete:
                deleteFromDatabase();
//                showDeleteConfirmationDialog();
                break;
        }
        return true;
    }


    private void getDataFromFirebase() {
        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference meetingParticipantDatabaseReference = mFirebaseDatabase.getReference()
                .child("meetings")
                .child(mUid)
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
                            .child(mUid)
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

    private void deleteFromDatabase() {
        final DatabaseReference meetingsDatabaseReference = mFirebaseDatabase.getReference().child("meetings").child(mUid).child(mMeetingId);
        deleteFromContact();
        meetingsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean meetingExists = false;
                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                    System.out.println("ds 1 " + dataSnapshot);
//                    getContactReference();
                }
//                if (meetingExists) {
//                    Toast.makeText(getApplicationContext(), "Data associated with this contact exists. Please delete such data before deleting this contact.", Toast.LENGTH_LONG).show();
//                } else {
//                    contactsDatabaseReference.removeValue();
//                    navigateToList(getApplicationContext());
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void deleteFromContact() {
        DatabaseReference contactDatabaseReference = mFirebaseDatabase.getReference().child("contacts").child(mUid);
        contactDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds1 : dataSnapshot.getChildren()) {
                    System.out.println("parent " + ds1);
                    System.out.println("DSS " + ds1.child("meetings").child(mMeetingId).getValue());
                    if(ds1.child("meetings").child(mMeetingId).getValue() != null && ds1.child("meetings").child(mMeetingId).getValue().toString().equals("true")) {
                        System.out.println("DS " + ds1.child("meetings").child(mMeetingId));
                    }

//                    if (ds1.child("meetings").child(mMeetingId).getValue().toString().equals("true")) {
//                        System.out.println("DS " + ds1.child("meetings").child(mMeetingId));
//                    }
//                    else {
//                        System.out.println("No DS " + ds1.child("meetings").child(mMeetingId));
//                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//        ArrayList<String> participantArray = new ArrayList<>();
//        DatabaseReference meetingDatabaseReference = mFirebaseDatabase.getReference().child("meetings").child(mUid).child(mMeetingId).child("participants");
//        meetingDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                System.out.println("snap " + dataSnapshot);
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    DatabaseReference contactsMeetingDatabaseReference = mFirebaseDatabase.getReference().child("contacts").child(ds.getKey()).child("meetings").child(mMeetingId);
//                    System.out.println("CMDR " + contactsMeetingDatabaseReference);
//                    contactsMeetingDatabaseReference.removeValue();
//                }
//            }
//                    System.out.println("Datas : " + ds.getKey());
//                    DatabaseReference contactsMeetingDatabaseReference = mFirebaseDatabase.getReference().child("contacts").child(ds.getKey()).child("meetings").child(mMeetingId);
//                    System.out.println("CMDR " + contactsMeetingDatabaseReference);
//                    contactsMeetingDatabaseReference.getRef().removeValue();
//                    System.out.println("HH");

//                    contactsMeetingDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            System.out.println("snapshot " + dataSnapshot);
//                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                System.out.println("KEYEY  " + ds);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });

//                }
//            }


}
