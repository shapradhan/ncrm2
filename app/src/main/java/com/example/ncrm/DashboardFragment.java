package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shameer on 2018-03-18.
 */

public class DashboardFragment extends Fragment {

    private ArrayList<String> mMeetingItemsArrayList = new ArrayList<>();
    private ArrayAdapter<String> mMeetingAdapter;
    private ArrayList<String> mReminderItemsArrayList = new ArrayList<>();
    private ArrayAdapter<String> mReminderAdapter;
    private ArrayList<String> mRecentFilesItemsArrayList = new ArrayList<>();
    private ArrayAdapter<String> mRecentFilesAdapter;
    private FirebaseDatabase mFirebaseDatabase;


    private ArrayList<Meeting> mMeetingArrayList = new ArrayList<>();
    private ArrayList<Reminder> mReminderArrayList = new ArrayList<>();
    private ArrayList<File> mRecentFilesArrayList = new ArrayList<>();

    private long mEpochTime;

    private ListView mUpcomingMeetingsListView;
    private ListView mRemindersListView;
    private ListView mRecentFilesListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard, container, false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mEpochTime = System.currentTimeMillis();

        getUpcomingMeetings(uid);
        getReminders(uid);
        getRecentFiles(uid);

        mMeetingAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mMeetingItemsArrayList);
        mUpcomingMeetingsListView = (ListView) view.findViewById(R.id.upcomingMeetingsListView);
        mUpcomingMeetingsListView.setAdapter(mMeetingAdapter);

        mUpcomingMeetingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Meeting meeting = mMeetingArrayList.get(position);
                Intent intent = new Intent(getContext(), MeetingDetailActivity.class);
                intent.putExtra("object", meeting);
                startActivity(intent);
            }
        });

        mReminderAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mReminderItemsArrayList);
        mRemindersListView = (ListView) view.findViewById(R.id.remindersListView);
        mRemindersListView.setAdapter(mReminderAdapter);

        mRecentFilesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mRecentFilesItemsArrayList);
        mRecentFilesListView = (ListView) view.findViewById(R.id.recentFilesListView);
        mRecentFilesListView.setAdapter(mRecentFilesAdapter);

        mRecentFilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                File file = mRecentFilesArrayList.get(position);
                Intent intent = new Intent(getContext(), MediaViewerActivity.class);
                intent.putExtra("object", file);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getUpcomingMeetings(String uid) {
        DatabaseReference databaseReference = Utility.getDatabaseReference(mFirebaseDatabase, "meetings", uid);
        System.out.println("CURRE " + mEpochTime);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Meeting meeting = ds.getValue(Meeting.class);

                    String date = meeting.getDate();
                    String time = meeting.getTime();
                    Date formattedDate = convertStringToDate(date, time);
                    long epoch = formattedDate.getTime();

                    if (mMeetingItemsArrayList.size() != 3) {
                        if (epoch > mEpochTime) {
                            mMeetingItemsArrayList.add(meeting.getTitle());
                            meeting.setId(ds.getKey());
                            mMeetingArrayList.add(meeting);
                            mMeetingAdapter.notifyDataSetChanged();
                        }
                    }
                }
                Utility.getListViewSize(mUpcomingMeetingsListView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getReminders(String uid) {
        DatabaseReference databaseReference = Utility.getDatabaseReference(mFirebaseDatabase, "reminders", uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Reminder reminder = ds.getValue(Reminder.class);

                    String date = reminder.getReminderDate();
                    String time = reminder.getReminderTime();
                    Date formattedDate = convertStringToDate(date, time);
                    long epoch = formattedDate.getTime();

                    if (mReminderArrayList.size() != 3) {
                        if (epoch > mEpochTime) {
                            mReminderItemsArrayList.add(reminder.getReminderItem());
                            reminder.setId(ds.getKey());
                            mReminderArrayList.add(reminder);
                            mReminderAdapter.notifyDataSetChanged();
                        }
                    }
                }
                Utility.getListViewSize(mRemindersListView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getRecentFiles(String uid) {
        DatabaseReference databaseReference = Utility.getDatabaseReference(mFirebaseDatabase, "files", uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (mRecentFilesArrayList.size() != 3) {
                        File file = ds.getValue(File.class);
                        mRecentFilesItemsArrayList.add(file.getFileName());
                        file.setId(ds.getKey());
                        mRecentFilesArrayList.add(file);
                        mRecentFilesAdapter.notifyDataSetChanged();
                    }
                }
                Utility.getListViewSize(mRecentFilesListView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Date convertStringToDate(String date, String time) {
        DateFormat dateFormat = new SimpleDateFormat("EE, dd MMMM yyyy hh:mm aa");
        Date formattedDate = null;
        try {
            formattedDate = dateFormat.parse(date + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
}