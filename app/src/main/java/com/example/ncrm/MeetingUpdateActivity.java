package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import java.util.Map;

/**
 * Created by shameer on 2018-03-13.
 */

public class MeetingUpdateActivity extends MainActivity {
    private Meeting mSelectedMeeting;

    private EditText mTitleEditText;
    private EditText mDateEditText;
    private EditText mTimeEditText;
    private EditText mVenueEditText;
    private EditText mStreetAddressEditText;
    private EditText mCityEditText;
    private EditText mAgendaEditText;
    private Spinner mCountrySpinner;
    private ListView mParticipantListView;

    private ArrayAdapter<String> mParticipantsNamesArrayAdapter;
    private AutoCompleteTextView mMeetingParticipantAutoCompleteTextView;

    private ArrayAdapter<String> mMeetingParticipantArrayAdapter;

    private ArrayList<String> mParticipantIdArray = new ArrayList<>();

    private static void getListViewSize(ListView listView) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_meeting_update, frameLayout);

        Intent intent = getIntent();
        mSelectedMeeting = (Meeting) intent.getSerializableExtra("meeting");

        // ArrayAdapter to show all available contacts in a AutoCompleteTextView
        mParticipantsNamesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mMeetingParticipantAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.meetingParticipantAutoCompleteTextView);
        mMeetingParticipantAutoCompleteTextView.setAdapter(mParticipantsNamesArrayAdapter);

        mParticipantListView = (ListView) findViewById(R.id.participantList);

        // ArrayAdapter to show selected contact names who participate in the meeting
        mMeetingParticipantArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mParticipantIdArray);
        mParticipantListView.setAdapter(mMeetingParticipantArrayAdapter);

        setValuesFromDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuItem menuItem = menu.add(Menu.NONE, 1000, Menu.NONE, R.string.update_caps);
        menuItem.setShowAsAction(1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        updateMeeting();
        navigateScene();
        return true;
    }

    private DatabaseReference getDatabaseReference() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String meetingId = mSelectedMeeting.getId();
        DatabaseReference meetingsDatabaseReference = firebaseDatabase.getReference().child("meetings").child(uid).child(meetingId);
        return meetingsDatabaseReference;
    }

    private void updateMeeting() {
        Meeting updatedMeeting = getInputValues();
        DatabaseReference databaseReference = getDatabaseReference();
        databaseReference.setValue(updatedMeeting);
    }

    private void setValuesFromDatabase() {
        mTitleEditText = findViewById(R.id.meetingTitleEditText);
        mDateEditText = (EditText) findViewById(R.id.meetingDateEditText);
        mTimeEditText = (EditText) findViewById(R.id.meetingTimeEditText);
        mVenueEditText = (EditText) findViewById(R.id.meetingVenueEditText);
        mStreetAddressEditText = (EditText) findViewById(R.id.meetingStreetAddressEditText);
        mCityEditText = (EditText) findViewById(R.id.meetingCityEditText);
        mCountrySpinner = (Spinner) findViewById(R.id.meetingCountrySpinner);
        mAgendaEditText = (EditText) findViewById(R.id.meetingAgendaEditText);


        String country = mSelectedMeeting.getCountry();
        ArrayAdapter arrayAdapter = (ArrayAdapter) mCountrySpinner.getAdapter();
        int spinnerPosition = arrayAdapter.getPosition(country);
        mCountrySpinner.setSelection(spinnerPosition);

        mTitleEditText.setText(mSelectedMeeting.getTitle());
        mDateEditText.setText(mSelectedMeeting.getDate());
        mTimeEditText.setText(mSelectedMeeting.getTime());
        mVenueEditText.setText(mSelectedMeeting.getVenue());
        mStreetAddressEditText.setText(mSelectedMeeting.getStreetAddress());
        mCityEditText.setText(mSelectedMeeting.getCity());
        mAgendaEditText.setText(mSelectedMeeting.getAgenda());


        Map<String, Boolean> participants = mSelectedMeeting.getParticipants();
        for (Map.Entry<String, Boolean> pair : participants.entrySet()) {
            getNameFromId(pair.getKey());
        }
    }

    private Meeting getInputValues() {
        String title = Utility.getStringFromEditText(mTitleEditText);
        String date = Utility.getStringFromEditText(mDateEditText);
        String time = Utility.getStringFromEditText(mTimeEditText);
        String venue = Utility.getStringFromEditText(mVenueEditText);
        String streetAddress = Utility.getStringFromEditText(mStreetAddressEditText);
        String city = Utility.getStringFromEditText(mCityEditText);
        String country = mCountrySpinner.getSelectedItem().toString();
        String agenda = Utility.getStringFromEditText(mAgendaEditText);

//        Meeting updatedMeeting = new Meeting(
//                title,
//                venue,
//                streetAddress,
//                city,
//                country,
//                date,
//                time,
//                agenda,
//
//
//        );

        Meeting updatedMeeting = new Meeting();

        return updatedMeeting;
    }

    private void navigateScene() {
        Intent intent = new Intent(MeetingUpdateActivity.this, MeetingListActivity.class);
        startActivity(intent);
    }

    private void getNameFromId(String contactId) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference contactsDatabaseReference = firebaseDatabase.getReference().child("contacts").child(uid).child(contactId);
        contactsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMeetingParticipantArrayAdapter.add(dataSnapshot.child("name").getValue().toString() + " - " + dataSnapshot.child("organization").getValue().toString());
                getListViewSize(mParticipantListView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
