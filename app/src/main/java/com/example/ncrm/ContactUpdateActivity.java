package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
    private Contact selectedContact;

    private EditText mNameEditText;
    private EditText mOrganizationEditText;
    private EditText mStreetAddressEditText;
    private EditText mCityEditText;
    private EditText mPhoneNumberEditText;
    private EditText mMobileNumberEditText;
    private EditText mEmailEditText;
    private EditText mWebsiteEditText;
    private EditText mFacebookIdEditText;
    private EditText mTwitterEditText;

    private Spinner mCountrySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_contact_update, frameLayout);

        Intent intent = getIntent();
        selectedContact = (Contact) intent.getSerializableExtra("contact");

        setValuesFromDatabase(selectedContact);

        Button updateContactBtn = (Button) findViewById(R.id.updateContactBtn);
        updateContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });
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
        updateContact();
        navigateScene();
        return true;
    }

    private DatabaseReference getDatabaseReference() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String contactId = selectedContact.getId();
        DatabaseReference contactsDatabaseReference = firebaseDatabase.getReference().child("contacts").child(uid).child(contactId);
        return contactsDatabaseReference;
    }

    private void updateContact() {
        Contact updatedContact = getInputValues();

        DatabaseReference databaseReference = getDatabaseReference();
        databaseReference.setValue(updatedContact);
    }

    private void setValuesFromDatabase(Contact selectedContact) {
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

        String country = selectedContact.getCountry();
        ArrayAdapter arrayAdapter = (ArrayAdapter) mCountrySpinner.getAdapter();
        int spinnerPosition = arrayAdapter.getPosition(country);
        mCountrySpinner.setSelection(spinnerPosition);

        mNameEditText.setText(selectedContact.getName());
        mOrganizationEditText.setText(selectedContact.getOrganization());
        mStreetAddressEditText.setText(selectedContact.getStreetAddress());
        mCityEditText.setText(selectedContact.getCity());
        mPhoneNumberEditText.setText(selectedContact.getPhoneNumber());
        mMobileNumberEditText.setText(selectedContact.getMobileNumber());
        mEmailEditText.setText(selectedContact.getEmail());
        mWebsiteEditText.setText(selectedContact.getWebsite());
        mFacebookIdEditText.setText(selectedContact.getFacebookId());
        mTwitterEditText.setText(selectedContact.getTwitterId());
    }

    private Contact getInputValues() {
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

        Contact updatedContact = new Contact(
                name,
                organization,
                streetAddress,
                city,
                country,
                phoneNumber,
                mobileNumber,
                email,
                website,
                facebookId,
                twitterId
        );

        return updatedContact;
    }

    private void navigateScene() {
        Intent intent = new Intent(ContactUpdateActivity.this, ContactListActivity.class);
        startActivity(intent);
    }
}