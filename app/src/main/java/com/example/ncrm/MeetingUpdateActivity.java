package com.example.ncrm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private Dictionary mParticipatingContactsIdDictionary = new Hashtable();

    private ArrayAdapter<String> mAllContactsNamesArrayAdapter;
    private AutoCompleteTextView mMeetingParticipantAutoCompleteTextView;

    private ArrayList<String> mParticipantsArray = new ArrayList<>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mContactDatabaseReference;

    private String mUid;

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

        EditText meetingDateEditText = (EditText) findViewById(R.id.meetingDateEditText);
        SetDate meetingDate = new SetDate(this, meetingDateEditText);

        EditText meetingTimeEditText = (EditText) findViewById(R.id.meetingTimeEditText);
        SetTime meetingTime = new SetTime(this, meetingTimeEditText);


        Intent intent = getIntent();
        mSelectedMeeting = (Meeting) intent.getSerializableExtra("meeting");

        // ArrayAdapter to show all available contacts in a AutoCompleteTextView
        mAllContactsNamesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mMeetingParticipantAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.meetingParticipantAutoCompleteTextView);
        mMeetingParticipantAutoCompleteTextView.setAdapter(mAllContactsNamesArrayAdapter);

        mParticipantListView = (ListView) findViewById(R.id.participantList);

        // ArrayAdapter to show selected contact names who participate in the meeting
        mParticipantListView.setAdapter(new MeetingUpdateActivity.ParticipantAdapter(this, R.layout.participant_list_item, mParticipantsArray));

        setValuesFromDatabase();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mContactDatabaseReference = mFirebaseDatabase.getReference().child("contacts").child(mUid);

        // Get the data from database and put it in ArrayAdapters
        addContactValueEventListener(mContactDatabaseReference);

        ImageButton addParticipantBtn = (ImageButton) findViewById(R.id.addParticipantBtn);
        addParticipant(addParticipantBtn);

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
        String meetingId = databaseReference.getKey();

        HashMap<String, Boolean> meetingInContact = new HashMap<>();
        meetingInContact.put(meetingId, true);

        Set<String> keys = updatedMeeting.getParticipants().keySet();
        for (String key : keys) {
            System.out.println("KEY " + key);
            mContactDatabaseReference.child(key).child("meetings").child(meetingId).setValue(true);
        }




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

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Boolean> participants = mSelectedMeeting.getParticipants();
        if (participants != null) {
            for (Map.Entry<String, Boolean> pair : participants.entrySet()) {
                getNameFromId(pair.getKey());
                DatabaseReference contactsDatabaseReference = firebaseDatabase.getReference().child("contacts").child(uid).child(pair.getKey());
                contactsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.child("meetings").child(mSelectedMeeting.getId()).getRef().removeValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
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

        for (int position = 0; position < mParticipantListView.getCount(); position++) {
            mParticipantsArray.add(mParticipantListView.getAdapter().getItem(position).toString());
        }

        HashMap<String, Boolean> participantsDictionary = new HashMap<>();
        for (String participantName : mParticipantsArray) {
            participantsDictionary.put(mParticipatingContactsIdDictionary.get(participantName).toString(), true);
        }

        Meeting updatedMeeting = new Meeting(
                title,
                venue,
                streetAddress,
                city,
                country,
                date,
                time,
                agenda,
                participantsDictionary
        );

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
                mParticipantsArray.add(dataSnapshot.child("name").getValue().toString() + " - " + dataSnapshot.child("organization").getValue().toString());
                getListViewSize(mParticipantListView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                mParticipantsArray.add(mMeetingParticipantAutoCompleteTextView.getText().toString());
                getListViewSize(mParticipantListView);
                mMeetingParticipantAutoCompleteTextView.setText("");
            }
        });
    }

    private class ParticipantAdapter extends ArrayAdapter<String> {
        private int layout;

        public ParticipantAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            MeetingUpdateActivity.ParticipantViewHolder mainParticipantViewHolder = null;
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                convertView = layoutInflater.inflate(layout, parent, false);
                MeetingUpdateActivity.ParticipantViewHolder participantViewHolder = new MeetingUpdateActivity.ParticipantViewHolder();
                participantViewHolder.removeBtn = (ImageButton) convertView.findViewById(R.id.removeBtn);
                participantViewHolder.participantTextView = (TextView) convertView.findViewById(R.id.participantText);
                participantViewHolder.participantTextView.setText(getItem(position));
                participantViewHolder.removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mParticipantsArray.remove(position);
                        notifyDataSetChanged();
                    }
                });
                convertView.setTag(participantViewHolder);
            } else {
                mainParticipantViewHolder = (MeetingUpdateActivity.ParticipantViewHolder) convertView.getTag();
                mainParticipantViewHolder.participantTextView.setText(getItem(position));
            }
            return convertView;
        }
    }

    public class ParticipantViewHolder {
        ImageButton removeBtn;
        TextView participantTextView;
    }
}

