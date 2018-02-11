package com.example.ncrm;

import android.*;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by shameer on 2018-02-10.
 */

public class ContactDetailActivity extends MainActivity {
    TextView mContactName;
    TextView mContactOrganization;
    TextView mContactFullAddress;
    Button mUpdateNameButton;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mContactsDatabaseReference;
    Contact selectedContact;

    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_contact_detail, frameLayout);

        intent = getIntent();

        selectedContact = (Contact) intent.getSerializableExtra("object");


        mContactName = (TextView) findViewById(R.id.contactItemName);
        mContactName.setText(selectedContact.getName());
        mContactOrganization = (TextView) findViewById(R.id.contactItemOrganization);
        mContactOrganization.setText(selectedContact.getOrganization());
        mContactFullAddress = (TextView) findViewById(R.id.contactItemFullAddress);
        mContactFullAddress.setText(selectedContact.getStreetAddress() + ", " + selectedContact.getCity() + ", " + selectedContact.getCountry());

        ImageButton phoneBtn = (ImageButton) findViewById(R.id.phoneBtn);
        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateCall(selectedContact.getPhoneNumber(), "Phone");
            }
        });

        ImageButton mobileBtn = (ImageButton) findViewById(R.id.mobileBtn);
        mobileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateCall(selectedContact.getMobileNumber(), "Mobile");
            }
        });

        ImageButton smsBtn = (ImageButton) findViewById(R.id.smsBtn);
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(selectedContact.getMobileNumber());
            }
        });

        ImageButton emailBtn = (ImageButton) findViewById(R.id.emailBtn);
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(selectedContact.getEmail());
            }
        });

        ImageButton websiteBtn = (ImageButton) findViewById(R.id.websiteBtn);
        websiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite(selectedContact.getWebsite());
            }
        });

        ImageButton facebookBtn = (ImageButton) findViewById(R.id.facebookBtn);
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebook(selectedContact.getFacebookId());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_update).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_update:

                Intent intent = new Intent(ContactDetailActivity.this, ContactUpdateActivity.class);

                intent.putExtra("contact", selectedContact);
                startActivity(intent);
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
        }
        else {
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
        }
        else {
            Toast.makeText(getApplicationContext(), "Mobile number is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail(String email) {
        if (email != null && email.length() != 0) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("mailto:"));
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
            try {
                startActivity(intent.createChooser(intent, "Send email"));
                finish();
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Email address is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWebsite(String websiteUrl) {
        if (websiteUrl != null && websiteUrl.length() != 0) {
            Uri uri;
            if(websiteUrl.startsWith("http://") || websiteUrl.startsWith("https://")) {
                uri = Uri.parse(websiteUrl);
            }
            else if (websiteUrl.startsWith("www.")) {
                uri = Uri.parse("https://" + websiteUrl);
            }
            else {
                uri = Uri.parse("https://www." + websiteUrl);
            }

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "There are no web clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
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
            }
            catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(this, "Facebook is not installed", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Facebook ID is not available.", Toast.LENGTH_SHORT).show();
        }
    }
}
