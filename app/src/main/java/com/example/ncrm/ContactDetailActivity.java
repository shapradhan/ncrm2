package com.example.ncrm;

import android.content.ActivityNotFoundException;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by shameer on 2018-02-10.
 */

public class ContactDetailActivity extends MainActivity {
    Contact mSelectedContact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_contact_detail, frameLayout);

        Intent intent = getIntent();
        mSelectedContact = (Contact) intent.getSerializableExtra("object");

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

        TextView contactName = (TextView) findViewById(R.id.contactItemName);
        contactName.setText(mSelectedContact.getName());

        TextView contactOrganization = (TextView) findViewById(R.id.contactItemOrganization);
        contactOrganization.setText(mSelectedContact.getOrganization());

        TextView contactFullAddress = (TextView) findViewById(R.id.contactItemFullAddress);
        contactFullAddress.setText(mSelectedContact.getStreetAddress() + ", " + mSelectedContact.getCity() + ", " + mSelectedContact.getCountry());

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
                intent.putExtra("contact", mSelectedContact);
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
}
