package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by shameer on 2018-02-11.
 */

public class ContactUpdateActivity extends MainActivity {
    EditText mNameEditText;
    Contact selectedContact;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_contact_update, frameLayout);

        Intent intent = getIntent();
        selectedContact = (Contact) intent.getSerializableExtra("contact");

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

        mNameEditText.setText(selectedContact.getName());
        mOrganizationEditText.setText(selectedContact.getOrganization());
        mStreetAddressEditText.setText(selectedContact.getStreetAddress());
        mCityEditText.setText(selectedContact.getCity());
        mCountrySpinner.setSelection(1);
        mPhoneNumberEditText.setText(selectedContact.getPhoneNumber());
        mMobileNumberEditText.setText(selectedContact.getMobileNumber());
        mEmailEditText.setText(selectedContact.getEmail());
        mWebsiteEditText.setText(selectedContact.getWebsite());
        mFacebookIdEditText.setText(selectedContact.getFacebookId());
        mTwitterEditText.setText(selectedContact.getTwitterId());

        Button updateContactBtn = (Button) findViewById(R.id.updateContactBtn);
        updateContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });
    }

    private void updateContact() {
        String name = Utility.getStringFromEditText(mNameEditText);
        String organization = Utility.getStringFromEditText(mOrganizationEditText);
        String streetAddress = Utility.getStringFromEditText(mStreetAddressEditText);
        String city = Utility.getStringFromEditText(mCityEditText);
        String country = mCountrySpinner.getSelectedItem().toString();
        String phoneNumber = Utility.getStringFromEditText(mPhoneNumberEditText);
        String mobileNumber = Utility.getStringFromEditText(mMobileNumberEditText);
        String email = Utility.getStringFromEditText(mEmailEditText);
        String website = Utility.getStringFromEditText(mWebsiteEditText);
        String facebookId = Utility.getStringFromEditText(mFacebookIdEditText);
        String twitterId = Utility.getStringFromEditText(mTwitterEditText);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String contactId = selectedContact.getId();

        Contact updatedContact = new Contact(name, organization, streetAddress, city, country, phoneNumber, mobileNumber, email, website, facebookId, twitterId, contactId);
        DatabaseReference contactsDatabaseReference = firebaseDatabase.getReference().child("contacts").child(uid).child(contactId);
        contactsDatabaseReference.setValue(updatedContact);

        Utility.cleanUpEditText(mNameEditText, mOrganizationEditText, mStreetAddressEditText, mCityEditText,
                mPhoneNumberEditText, mMobileNumberEditText, mEmailEditText, mFacebookIdEditText,
                mTwitterEditText);

        Intent intent = new Intent(ContactUpdateActivity.this, ContactListActivity.class);
        startActivity(intent);
    }
}