package com.example.ncrm;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
 * Created by shameer on 2018-02-10.
 */

public class ContactDetailActivity extends MainActivity {
    private Contact mSelectedContact;
    private ArrayList<String> mMeetingIDList = new ArrayList<>();
    private ArrayList<Meeting> mMeetingsArray = new ArrayList<>();
    private ArrayAdapter<String> mAllContactsNamesArrayAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private String mUid;
    private String mContactId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_contact_detail, frameLayout);

        Intent intent = getIntent();
        mSelectedContact = (Contact) intent.getSerializableExtra("object");
        mContactId = mSelectedContact.getId();

        ImageButton mapBtn = (ImageButton) findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactDetailActivity.this, MapsActivity.class);
                intent.putExtra("streetAddress", mSelectedContact.getStreetAddress());
                intent.putExtra("city", mSelectedContact.getCity());
                intent.putExtra("country", mSelectedContact.getCountry());
                startActivity(intent);
            }
        });

        setTextViewValues();
        setButtonClickListeners();
        getMeetingData();

        ListView meetingsListView = (ListView) findViewById(R.id.contactMeetingListView);
        mAllContactsNamesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        meetingsListView.setAdapter(mAllContactsNamesArrayAdapter);

        meetingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Meeting selectedMeeting = mMeetingsArray.get(position);
                Intent intent = new Intent(getApplicationContext(), MeetingDetailActivity.class);
                intent.putExtra("object", selectedMeeting);
                startActivity(intent);
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
                intent = new Intent(ContactDetailActivity.this, ContactUpdateActivity.class);
                intent.putExtra("contact", mSelectedContact);
                startActivity(intent);
                break;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                break;
        }
        return true;
    }

    private void initiateCall(String number, String typeOfCall) {
        if (number != null && number.length() != 0) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 10);
            } else {
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), typeOfCall + " is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS(String mobileNumber) {
        if (mobileNumber != null && mobileNumber.length() != 0) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("vnd.android-dir/mms-sms");
            intent.setData(Uri.parse("sms:" + mobileNumber));
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Mobile number is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail(String email) {
        if (email != null && email.length() != 0) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("mailto:"));
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            try {
                startActivity(intent.createChooser(intent, "Send email"));
                finish();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Email address is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWebsite(String websiteUrl) {
        if (websiteUrl != null && websiteUrl.length() != 0) {
            Uri uri;
            if (websiteUrl.startsWith("http://") || websiteUrl.startsWith("https://")) {
                uri = Uri.parse(websiteUrl);
            } else if (websiteUrl.startsWith("www.")) {
                uri = Uri.parse("https://" + websiteUrl);
            } else {
                uri = Uri.parse("https://www." + websiteUrl);
            }

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "There are no web clients installed.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Website is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFacebook(String facebookId) {
        if (facebookId != null && facebookId.length() != 0) {
            String url = "https://www.facebook.com/" + facebookId;
            Uri uri = Uri.parse(url);
            try {
                ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo("com.facebook.katana", 0);
                if (applicationInfo.enabled) {
                    uri = Uri.parse("fb://facewebmodal/f?href=" + url);
                }
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(this, "Facebook is not installed.", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Facebook ID is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openTwitter(String twitterId) {
        if (twitterId != null && twitterId.length() != 0) {
            Intent intent = null;
            try {
                getApplicationContext().getPackageManager().getPackageInfo("com.twitter.android", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitterId));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) {
                String url = "https://twitter.com/" + twitterId;
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                Toast.makeText(this, "Twitter is not installed.", Toast.LENGTH_SHORT).show();
            }
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Twitter ID is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMeetingData() {
        DatabaseReference contactMeetingDatabaseReference = Utility.getDatabaseReference(mFirebaseDatabase, "contacts", mUid).child(mContactId).child("meetings");
        contactMeetingDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mMeetingIDList.add(ds.getKey());
                }
                getMeetingInfo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getMeetingInfo() {
        for (final String meetingId : mMeetingIDList) {
            DatabaseReference contactMeetingDatabaseReference = Utility.getDatabaseReference(mFirebaseDatabase, "meetings", mUid).child(meetingId);

            contactMeetingDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Meeting meeting = dataSnapshot.getValue(Meeting.class);
                    meeting.setId(meetingId);
                    mMeetingsArray.add(meeting);
                    mAllContactsNamesArrayAdapter.add(meeting.getTitle() + " - " + meeting.getDate() + " - " + meeting.getTime());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactDetailActivity.this);
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

    private void deleteFromDatabase() {
        final DatabaseReference contactsDatabaseReference = Utility.getDatabaseReference(mFirebaseDatabase, "contacts", mUid).child(mContactId);
        contactsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean meetingExists = false;
                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                    if (ds1.getKey().equals("meetings")) {
                        meetingExists = true;
                    }
                }
                if (meetingExists) {
                    Toast.makeText(getApplicationContext(), "Data associated with this contact exists. Please delete such data before deleting this contact.", Toast.LENGTH_LONG).show();
                } else {
                    contactsDatabaseReference.removeValue();
                    navigateToList(getApplicationContext());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void navigateToList(Context context) {
        Intent intent = new Intent(context, ContactListActivity.class);
        startActivity(intent);
    }

    private void setTextViewValues() {
        TextView contactName = (TextView) findViewById(R.id.contactItemName);
        contactName.setText(mSelectedContact.getName());

        TextView contactOrganization = (TextView) findViewById(R.id.contactItemOrganization);
        contactOrganization.setText(mSelectedContact.getOrganization());

        TextView contactFullAddress = (TextView) findViewById(R.id.contactItemFullAddress);
        contactFullAddress.setText(mSelectedContact.getStreetAddress() + ", " + mSelectedContact.getCity() + ", " + mSelectedContact.getCountry());
    }

    private void setButtonClickListeners() {
        ImageButton phoneBtn = (ImageButton) findViewById(R.id.phoneBtn);
        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateCall(mSelectedContact.getPhoneNumber(), "Phone");
            }
        });

        ImageButton mobileBtn = (ImageButton) findViewById(R.id.mobileBtn);
        mobileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateCall(mSelectedContact.getMobileNumber(), "Mobile");
            }
        });

        ImageButton smsBtn = (ImageButton) findViewById(R.id.smsBtn);
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(mSelectedContact.getMobileNumber());
            }
        });

        ImageButton emailBtn = (ImageButton) findViewById(R.id.emailBtn);
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(mSelectedContact.getEmail());
            }
        });

        ImageButton websiteBtn = (ImageButton) findViewById(R.id.websiteBtn);
        websiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite(mSelectedContact.getWebsite());
            }
        });

        ImageButton facebookBtn = (ImageButton) findViewById(R.id.facebookBtn);
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebook(mSelectedContact.getFacebookId());
            }
        });

        ImageButton twitterBtn = (ImageButton) findViewById(R.id.twitterBtn);
        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(mSelectedContact.getTwitterId());
            }
        });
    }
}