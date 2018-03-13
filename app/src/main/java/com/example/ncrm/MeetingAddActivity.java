package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by shameer on 2018-02-12.
 */

public class MeetingAddActivity extends MainActivity {
    private ArrayAdapter<String> mMeetingParticipantArrayAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mContactDatabaseReference;
    private ArrayList<String> mParticipantsArray = new ArrayList<>();
    private Dictionary mParticipatingContactsIdDictionary = new Hashtable();
    private ArrayAdapter<String> mAllContactsNamesArrayAdapter;
    private AutoCompleteTextView mMeetingParticipantAutoCompleteTextView;
    private ListView mParticipantListView;
    private String mUid;

    public static void getListViewSize(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // Get total height
        int totalHeight = 0;
        for (int size = 0; size < listAdapter.getCount(); size++) {
            View listItem = listAdapter.getView(size, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_meeting_add, frameLayout);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mContactDatabaseReference = mFirebaseDatabase.getReference().child("contacts").child(mUid);

        mParticipantListView = (ListView) findViewById(R.id.participantList);

        // ArrayAdapter to show all available contacts in a AutoCompleteTextView
        mAllContactsNamesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mMeetingParticipantAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.meetingParticipantAutoCompleteTextView);
        mMeetingParticipantAutoCompleteTextView.setAdapter(mAllContactsNamesArrayAdapter);

        // Get the data from database and put it in ArrayAdapters
        addContactValueEventListener(mContactDatabaseReference);

        EditText meetingDateEditText = (EditText) findViewById(R.id.meetingDateEditText);
        SetDate meetingDate = new SetDate(this, meetingDateEditText);

        EditText meetingTimeEditText = (EditText) findViewById(R.id.meetingTimeEditText);
        SetTime meetingTime = new SetTime(this, meetingTimeEditText);

        // ArrayAdapter to show selected contact names who participate in the meeting
        mMeetingParticipantArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        mParticipantListView.setAdapter(mMeetingParticipantArrayAdapter);

        ImageButton addParticipantBtn = (ImageButton) findViewById(R.id.addParticipantBtn);
        addParticipant(addParticipantBtn);

        mParticipantListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        Button addMeetingBtn = (Button) findViewById(R.id.addMeetingBtn);
        addMeetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeeting();
            }
        });
    }

    private void addMeeting() {
        Meeting meeting = getInputValues();
        DatabaseReference databaseReference = getDatabaseReference();

        DatabaseReference newMeetingReference = databaseReference.push();
        String meetingId = newMeetingReference.getKey();
        newMeetingReference.setValue(meeting);

        HashMap<String, Boolean> meetingInContact = new HashMap<>();
        meetingInContact.put(meetingId, true);

        Set<String> keys = meeting.getParticipants().keySet();
        for (String key : keys) {
            mContactDatabaseReference.child(key).child("meetings").child(meetingId).setValue(true);
        }
    }

    private void addContactValueEventListener(DatabaseReference databaseReference) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String organization = ds.child("organization").getValue(String.class);
                    String editTextLabel = name + " - " + organization;
                    mAllContactsNamesArrayAdapter.add(editTextLabel);
                    mParticipatingContactsIdDictionary.put(editTextLabel, ds.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void addParticipant(ImageButton addParticipantBtn) {
        addParticipantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMeetingParticipantArrayAdapter.add(mMeetingParticipantAutoCompleteTextView.getText().toString());
                mMeetingParticipantArrayAdapter.notifyDataSetChanged();
                mParticipantsArray.add(mMeetingParticipantAutoCompleteTextView.getText().toString());
                getListViewSize(mParticipantListView);
            }
        });
    }

    private Meeting getInputValues() {
        EditText meetingTitleEditText = (EditText) findViewById(R.id.meetingTitleEditText);
        EditText meetingDateEditText = (EditText) findViewById(R.id.meetingDateEditText);
        EditText meetingTimeEditText = (EditText) findViewById(R.id.meetingTimeEditText);
        EditText meetingVenueEditText = (EditText) findViewById(R.id.meetingVenueEditText);
        EditText meetingStreetAddressEditText = (EditText) findViewById(R.id.meetingStreetAddressEditText);
        EditText meetingCityEditText = (EditText) findViewById(R.id.meetingCityEditText);
        EditText meetingAgendaEditText = (EditText) findViewById(R.id.meetingAgendaEditText);
        Spinner meetingCountrySpinner = (Spinner) findViewById(R.id.contactAddressCountrySpinner);

        String meetingTitle = Utility.getStringFromEditText(meetingTitleEditText);
        String meetingDate = Utility.getStringFromEditText(meetingDateEditText);
        String meetingTime = Utility.getStringFromEditText(meetingTimeEditText);
        String meetingVenue = Utility.getStringFromEditText(meetingVenueEditText);
        String meetingStreetAddress = Utility.getStringFromEditText(meetingStreetAddressEditText);
        String meetingCity = Utility.getStringFromEditText(meetingCityEditText);
        String meetingAgenda = Utility.getStringFromEditText(meetingAgendaEditText);
        String meetingCountry = meetingCountrySpinner.getSelectedItem().toString();

        HashMap<String, Boolean> meetingParticipantsDictionary = new HashMap<>();
        for (String participantName : mParticipantsArray) {
            meetingParticipantsDictionary.put(mParticipatingContactsIdDictionary.get(participantName).toString(), true);
        }

        Meeting meeting = new Meeting(
                meetingTitle,
                meetingVenue,
                meetingStreetAddress,
                meetingCity,
                meetingCountry,
                meetingDate,
                meetingTime,
                meetingParticipantsDictionary
        );

        return meeting;
    }

    private DatabaseReference getDatabaseReference() {
        DatabaseReference meetingsDatabaseReference = mFirebaseDatabase.getReference().child("meetings").child(mUid);
        return meetingsDatabaseReference;
    }

    private void navigateScene() {
        Intent intent = new Intent(MeetingAddActivity.this, MeetingListActivity.class);
        startActivity(intent);
    }
}