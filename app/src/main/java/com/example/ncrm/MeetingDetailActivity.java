package com.example.ncrm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private String mUid;
    private String mMeetingId;
    private ArrayList<String> mParticipantsIdArrayList = new ArrayList<>();

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

        TextView venueTextView = (TextView) findViewById(R.id.meetingVenue);
        venueTextView.setText(mSelectedMeeting.getVenue());

        TextView streetAddressTextView = (TextView) findViewById(R.id.meetingStreetAddress);
        streetAddressTextView.setText(mSelectedMeeting.getStreetAddress());

        TextView cityTextView = (TextView) findViewById(R.id.meetingCity);
        cityTextView.setText(mSelectedMeeting.getCity());

        TextView countryTextView = (TextView) findViewById(R.id.meetingCountry);
        countryTextView.setText(mSelectedMeeting.getCountry());

        TextView meetingAgenda = (TextView) findViewById(R.id.meetingAgenda);
        meetingAgenda.setText(mSelectedMeeting.getAgenda());

        getDataFromFirebase();
        checkAddress(mSelectedMeeting.getVenue(), mSelectedMeeting.getStreetAddress(), mSelectedMeeting.getCity(), mSelectedMeeting.getCountry(), venueTextView, streetAddressTextView, cityTextView, countryTextView);

        mParticipantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final String contactId = mParticipantsIdArrayList.get(position);
                DatabaseReference contactDatabaseReference = Utility.getDatabaseReference(mFirebaseDatabase, "contacts", mUid).child(contactId);
                contactDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Contact selectedContact = dataSnapshot.getValue(Contact.class);
                        Intent intent = new Intent(getApplicationContext(), ContactDetailActivity.class);
                        selectedContact.setId(contactId);
                        intent.putExtra("object", selectedContact);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
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
                showDeleteConfirmationDialog();
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
                    mParticipantsIdArrayList.add(ds.getKey());
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
        final DatabaseReference meetingsDatabaseReference = Utility.getDatabaseReference(mFirebaseDatabase, "meetings", mUid).child(mMeetingId);
        deleteFromContact();
        meetingsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
                navigateToList(getApplicationContext());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void deleteFromContact() {
        DatabaseReference contactDatabaseReference = Utility.getDatabaseReference(mFirebaseDatabase, "contacts", mUid);
        contactDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                    if (ds1.child("meetings").child(mMeetingId).getValue() != null) {
                        if (ds1.child("meetings").child(mMeetingId).getValue().toString().equals("true")) {
                            ds1.child("meetings").child(mMeetingId).getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MeetingDetailActivity.this);
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                deleteFromDatabase();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog confirmationDialog = builder.create();
        confirmationDialog.show();
    }

    private void navigateToList(Context context) {
        Intent intent = new Intent(context, MeetingListActivity.class);
        startActivity(intent);
    }

    private void checkAddress(String venue, String streetAddress, String city, String country, TextView venueTextView, TextView streetAddressTextView, TextView cityTextView, TextView countryTextView) {
        if (venue == null || venue.equals("")) {
            venueTextView.setVisibility(View.GONE);
        }
        if (streetAddress == null || streetAddress.equals("")) {
            streetAddressTextView.setVisibility(View.GONE);
        }
        if (city == null || city.equals("")) {
            cityTextView.setVisibility(View.GONE);
        }
        if (country == null || country.equals("")) {
            countryTextView.setVisibility(View.GONE);
        }
    }
}
