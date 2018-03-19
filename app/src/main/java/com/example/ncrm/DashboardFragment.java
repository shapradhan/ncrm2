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
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * Created by shameer on 2018-03-18.
 */

public class DashboardFragment extends Fragment {

    private ArrayList<String> mMeetingItemsArrayList;
    private ArrayAdapter<String> mMeetingAdapter;
    private ArrayList<String> mReminderItemsArrayList;
    private ArrayAdapter<String> mReminderAdapter;
    private ArrayList<String> mRecentFilesItemsArrayList;
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
        mMeetingItemsArrayList = new ArrayList<>();
        mReminderItemsArrayList = new ArrayList<>();
        mRecentFilesItemsArrayList = new ArrayList<>();

        mEpochTime = System.currentTimeMillis();

        getUpcomingMeetings(uid);
        getReminders(uid);
        getRecentFiles(uid);

        TextView upcomingMeetingsTitle = view.findViewById(R.id.upcomingMeetingsTitle);
        TextView remindersTitle = view.findViewById(R.id.remindersTitle);
        TextView recentFilesTitle = view.findViewById(R.id.recentFilesTitle);

        upcomingMeetingsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MeetingListActivity.class);
                startActivity(intent);
            }
        });

        remindersTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReminderListActivity.class);
                startActivity(intent);
            }
        });

        recentFilesTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FileListActivity.class);
                intent.putExtra("sortMethod", "name");
                startActivity(intent);
            }
        });

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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Meeting meeting = ds.getValue(Meeting.class);
                    meeting.setId(ds.getKey());
                    String date = meeting.getDate();
                    String time = meeting.getTime();
                    Date formattedDate = convertStringToDate(date, time);
                    long epoch = formattedDate.getTime();

                    if (epoch > mEpochTime) {
                        mMeetingArrayList.add(meeting);
                    }

                    sortMeetingArray(mMeetingArrayList);
                }

                if (mMeetingArrayList.size() > 3) {
                    for (int i = 0; i < 3; i++) {
                        mMeetingItemsArrayList.add(mMeetingArrayList.get(i).getTitle());
                        mMeetingAdapter.notifyDataSetChanged();
                    }
                } else {
                    for (int i = 0; i < mMeetingArrayList.size(); i++) {
                        mMeetingItemsArrayList.add(mMeetingArrayList.get(i).getTitle());
                        mMeetingAdapter.notifyDataSetChanged();
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
                    reminder.setId(ds.getKey());
                    String date = reminder.getReminderDate();
                    String time = reminder.getReminderTime();
                    Date formattedDate = convertStringToDate(date, time);
                    long epoch = formattedDate.getTime();

                    if (epoch > mEpochTime) {
                        mReminderArrayList.add(reminder);
                    }

                    sortReminderArray(mReminderArrayList);
                }

                if (mReminderArrayList.size() > 3) {
                    for (int i = 0; i < 3; i++) {
                        mReminderItemsArrayList.add(mReminderArrayList.get(i).getReminderItem());
                        mReminderAdapter.notifyDataSetChanged();
                    }
                } else {
                    for (int i = 0; i < mReminderArrayList.size(); i++) {
                        mReminderItemsArrayList.add(mReminderArrayList.get(i).getReminderItem());
                        mReminderAdapter.notifyDataSetChanged();
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

    private void sortMeetingArray(ArrayList<Meeting> arrayList) {
        Collections.sort(arrayList, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting meeting1, Meeting meeting2) {
                Date date1 = convertStringToDate(meeting1.getDate());
                Date date2 = convertStringToDate(meeting2.getDate());

                if (date1.compareTo(date2) == 0) {
                    Date time1 = convertStringToTime(meeting1.getTime());
                    Date time2 = convertStringToTime(meeting2.getTime());
                    return (time1.compareTo(time2));
                }
                return date1.compareTo(date2);
            }
        });
    }

    private Date convertStringToDate(String dateString) {
        DateFormat format = new SimpleDateFormat("EE, dd MMMM yyyy", Locale.ENGLISH);
        try {
            Date date = format.parse(dateString);
            return date;
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Could not convert string to date", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private Date convertStringToTime(String timeString) {
        DateFormat format = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        try {
            Date time = format.parse(timeString);
            return time;
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Could not convert string to date", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void sortReminderArray(ArrayList<Reminder> arrayList) {
        Collections.sort(arrayList, new Comparator<Reminder>() {
            @Override
            public int compare(Reminder reminder1, Reminder reminder2) {
                Date date1 = convertStringToDate(reminder1.getReminderDate());
                Date date2 = convertStringToDate(reminder2.getReminderDate());

                if (date1.compareTo(date2) == 0) {
                    Date time1 = convertStringToTime(reminder1.getReminderTime());
                    Date time2 = convertStringToTime(reminder2.getReminderTime());
                    return (time1.compareTo(time2));
                }
                return date1.compareTo(date2);
            }
        });
    }
}