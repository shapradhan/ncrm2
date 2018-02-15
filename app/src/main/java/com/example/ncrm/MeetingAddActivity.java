package com.example.ncrm;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by shameer on 2018-02-12.
 */

public class MeetingAddActivity extends MainActivity {
    private final Calendar calendar = Calendar.getInstance();
    ArrayAdapter<String> participantListAdapter;
    ListView participantListView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mContactNameDatabaseReference;
    private DatabaseReference mContactOrganizationDatabaseReference;
    private DatabaseReference mContactDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private EditText mMeetingTitleEditText;
    private EditText mMeetingDateEditText;
    private EditText mMeetingTimeEditText;
    private EditText mMeetingVenueEditText;
    private EditText mMeetingStreetAddressEditText;
    private EditText mMeetingCityEditText;
    private EditText mMeetingCountryEditText;
    private AutoCompleteTextView mMeetingParticipantAutoCompleteTextView;
    private EditText mMeetingAgendaEditText;
    private ArrayList<String> participantsNames = new ArrayList<>();
    private Dictionary contactsId = new Hashtable();

    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            return;
        }
        // Get total height
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_meeting_add, frameLayout);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mContactNameDatabaseReference = mFirebaseDatabase.getReference().child("contacts").child("name");
        mContactOrganizationDatabaseReference = mFirebaseDatabase.getReference().child("contacts").child("organization");

        mFirebaseAuth = FirebaseAuth.getInstance();

        final ArrayAdapter<String> names = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        mContactDatabaseReference = mFirebaseDatabase.getReference().child("contacts").child(user.getUid());

        mContactDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String organization = ds.child("organization").getValue(String.class);
                    String editTextLabel = name + " - " + organization;
                    names.add(editTextLabel);
                    contactsId.put(editTextLabel, ds.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mMeetingParticipantAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.meetingParticipantAutoCompleteTextView);
        mMeetingParticipantAutoCompleteTextView.setAdapter(names);

        EditText meetingDateEditText = (EditText) findViewById(R.id.meetingDateEditText);
        SetDate meetingDate = new SetDate(this, meetingDateEditText);

        EditText meetingTimeEditText = (EditText) findViewById(R.id.meetingTimeEditText);
        SetTime meetingTime = new SetTime(this, meetingTimeEditText);

        participantListView = (ListView) findViewById(R.id.participantList);
        ImageButton addParticipantBtn = (ImageButton) findViewById(R.id.addParticipantBtn);
        participantListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        participantListView.setAdapter(participantListAdapter);

        addParticipantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participantListAdapter.add(mMeetingParticipantAutoCompleteTextView.getText().toString());
                participantListAdapter.notifyDataSetChanged();

                System.out.println("there");
                getListViewSize(participantListView);
            }
        });

        participantListView.setOnTouchListener(new View.OnTouchListener() {
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
                mMeetingTitleEditText = (EditText) findViewById(R.id.meetingTitleEditText);
                mMeetingDateEditText = (EditText) findViewById(R.id.meetingDateEditText);
                mMeetingTimeEditText = (EditText) findViewById(R.id.meetingTimeEditText);
                mMeetingVenueEditText = (EditText) findViewById(R.id.meetingVenueEditText);
                mMeetingStreetAddressEditText = (EditText) findViewById(R.id.meetingStreetAddressEditText);
                mMeetingCityEditText = (EditText) findViewById(R.id.meetingCityEditText);
                mMeetingCountryEditText = (EditText) findViewById(R.id.meetingCountryEditText);
                mMeetingAgendaEditText = (EditText) findViewById(R.id.meetingAgendaEditText);

                String meetingTitle = "";
                String meetingDate = "";
                String meetingTime = "";
                String meetingVenue = "";
                String meetingStreetAddress = "";
                String meetingCity = "";
                String meetingCountry = "";
                String meetingAgenda = "";

                meetingTitle = mMeetingTitleEditText.getText().toString();
                meetingDate = mMeetingDateEditText.getText().toString();
                meetingTime = mMeetingTimeEditText.getText().toString();
                meetingVenue = mMeetingVenueEditText.getText().toString();
                meetingStreetAddress = mMeetingStreetAddressEditText.getText().toString();
                meetingCity = mMeetingCityEditText.getText().toString();
                meetingCountry = mMeetingCountryEditText.getText().toString();
                meetingAgenda = mMeetingAgendaEditText.getText().toString();

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mFirebaseAuth = FirebaseAuth.getInstance();

                String uid = mFirebaseAuth.getCurrentUser().getUid();

                HashMap<String, Boolean> participantInMeeting = new HashMap<>();
                for (String participantName : participantsNames) {
                    participantInMeeting.put(contactsId.get(participantName).toString(), true);
                }

                DatabaseReference mMeetingsDatabaseReference = mFirebaseDatabase.getReference().child("meetings").child(uid);
                Meeting meeting = new Meeting(
                        meetingTitle,
                        meetingVenue,
                        meetingStreetAddress,
                        meetingCity,
                        meetingCountry,
                        meetingDate,
                        meetingTime,
                        participantInMeeting);

                DatabaseReference newMeetingReference = mMeetingsDatabaseReference.push();
                String meetingId = newMeetingReference.getKey();
                newMeetingReference.setValue(meeting);

                HashMap<String, Boolean> meetingInContact = new HashMap<>();
                meetingInContact.put(meetingId, true);

                Set<String> keys = participantInMeeting.keySet();

                for (String key : keys) {
                    mContactDatabaseReference.child(key).child("meetings").child(meetingId).setValue(true);
                }
            }
        });
    }
}
