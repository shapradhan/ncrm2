package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by shameer on 2018-02-10.
 */

public class ContactAddActivity extends AppCompatActivity {

    private Button mAddContactBtn;
    private EditText mNameEditText;
    private EditText mOrganizationEditText;
    private EditText mStreetAddressEditText;
    private EditText mCityEditText;
    private Spinner mCountrySpinner;
    private EditText mPhoneNumberEditText;
    private EditText mMobileNumberEditText;
    private EditText mEmailEditText;
    private EditText mWebsiteEditText;
    private EditText mFacebookIdEditText;
    private EditText mTwitterEditText;
    private EditText mLinkedInEditText;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mContactsDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);


        mAddContactBtn = (Button) findViewById(R.id.addContactBtn);
        mAddContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNameEditText = (EditText) findViewById(R.id.contactNameEditText);
                mOrganizationEditText = (EditText) findViewById(R.id.contactOrganizationEditText);
                mStreetAddressEditText = (EditText) findViewById(R.id.contactAddressStreetEditText);
                mCityEditText = (EditText) findViewById(R.id.contactAddressCityEditText);
                mCountrySpinner = (Spinner) findViewById(R.id.contactAddressCountrySpinner);
                mPhoneNumberEditText = (EditText) findViewById(R.id.contactPhoneNumberEditText);
                mMobileNumberEditText = (EditText) findViewById(R.id.contactMobileNumberEditText);
                mEmailEditText = (EditText) findViewById(R.id.contactEmailEditText);
                mWebsiteEditText = (EditText) findViewById(R.id.contactWebsiteEditText);
                mFacebookIdEditText = (EditText) findViewById(R.id.contactFacebookEditText);
                mTwitterEditText = (EditText) findViewById(R.id.contactTwitterEditText);
                mLinkedInEditText = (EditText) findViewById(R.id.contactLinkedInEditText);

                String name = "";
                String organization = "";
                String streetAddress = "";
                String city = "";
                String country = "";
                String phoneNumber = "";
                String mobileNumber = "";
                String email = "";
                String website = "";
                String facebookId = "";
                String twitterId = "";
                String linkedInId = "";

                name = mNameEditText.getText().toString();
                organization = mOrganizationEditText.getText().toString();
                streetAddress = mStreetAddressEditText.getText().toString();
                city = mCityEditText.getText().toString();
                country = mCountrySpinner.getSelectedItem().toString();
                phoneNumber = mPhoneNumberEditText.getText().toString();
                mobileNumber = mMobileNumberEditText.getText().toString();
                email = mEmailEditText.getText().toString();
                website = mWebsiteEditText.getText().toString();
                facebookId = mFacebookIdEditText.getText().toString();
                twitterId = mTwitterEditText.getText().toString();
                linkedInId = mLinkedInEditText.getText().toString();

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mFirebaseAuth = FirebaseAuth.getInstance();

                mContactsDatabaseReference = mFirebaseDatabase.getReference("contacts");
                String user = mFirebaseAuth.getCurrentUser().getUid();
                Contact contact = new Contact(name, organization, streetAddress, city, country, phoneNumber, mobileNumber,
                        email, website, facebookId, twitterId, linkedInId, user);
                mContactsDatabaseReference.push().setValue(contact);
                cleanUpEditText(mNameEditText, mOrganizationEditText, mStreetAddressEditText, mCityEditText,
                        mPhoneNumberEditText, mMobileNumberEditText, mEmailEditText, mFacebookIdEditText,
                        mTwitterEditText, mLinkedInEditText);
                Intent intent = new Intent(ContactAddActivity.this, ContactListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cleanUpEditText(EditText... editTexts) {
        for (EditText editText: editTexts) {
            editText = null;
        }
    }
}
