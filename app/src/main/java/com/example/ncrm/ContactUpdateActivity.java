package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by shameer on 2018-02-11.
 */

public class ContactUpdateActivity extends MainActivity {

    private Button mUpdateContactBtn;
    EditText mNameEditText;
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

    Contact selectedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_contact_update, frameLayout);

        Intent intent = getIntent();
         selectedContact = (Contact) intent.getSerializableExtra("contact");

        mUpdateContactBtn = (Button) findViewById(R.id.updateContactBtn);
        mUpdateContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });
    }

    private void cleanUpEditText(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText = null;
        }
    }

    private void updateContact() {

            mNameEditText = findViewById(R.id.contactNameEditText);

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
                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                String uid = firebaseUser.getUid();
        System.out.println("UIDDD " + uid);
                String contactId = selectedContact.getId();
        System.out.println(" IDDDDD" + contactId);
                Contact updatedContact = new Contact(name, organization, streetAddress, city, country, phoneNumber, mobileNumber, email, website, facebookId, twitterId, linkedInId, contactId);
                mContactsDatabaseReference = mFirebaseDatabase.getReference().child("contacts").child(uid).child(contactId);
                mContactsDatabaseReference.setValue(updatedContact);


                cleanUpEditText(mNameEditText, mOrganizationEditText, mStreetAddressEditText, mCityEditText,
                        mPhoneNumberEditText, mMobileNumberEditText, mEmailEditText, mFacebookIdEditText,
                        mTwitterEditText, mLinkedInEditText);
                Intent intent = new Intent(ContactUpdateActivity.this, ContactListActivity.class);
                startActivity(intent);
            }
        }