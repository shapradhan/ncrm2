package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by shameer on 2018-02-10.
 */

public class ContactAddActivity extends MainActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_contact_add, frameLayout);

        Button addContactBtn = (Button) findViewById(R.id.addContactBtn);
        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = (EditText) findViewById(R.id.contactNameEditText);
                EditText organizationEditText = (EditText) findViewById(R.id.contactOrganizationEditText);
                EditText streetAddressEditText = (EditText) findViewById(R.id.contactAddressStreetEditText);
                EditText cityEditText = (EditText) findViewById(R.id.contactAddressCityEditText);
                Spinner countrySpinner = (Spinner) findViewById(R.id.contactAddressCountrySpinner);
                EditText phoneNumberEditText = (EditText) findViewById(R.id.contactPhoneNumberEditText);
                EditText mobileNumberEditText = (EditText) findViewById(R.id.contactMobileNumberEditText);
                EditText emailEditText = (EditText) findViewById(R.id.contactEmailEditText);
                EditText websiteEditText = (EditText) findViewById(R.id.contactWebsiteEditText);
                EditText facebookIdEditText = (EditText) findViewById(R.id.contactFacebookEditText);
                EditText twitterEditText = (EditText) findViewById(R.id.contactTwitterEditText);
                EditText linkedInEditText = (EditText) findViewById(R.id.contactLinkedInEditText);

                String name = Utility.getStringFromEditText(nameEditText);
                String organization = Utility.getStringFromEditText(organizationEditText);
                String streetAddress = Utility.getStringFromEditText(streetAddressEditText);
                String city = Utility.getStringFromEditText(cityEditText);
                String country = countrySpinner.getSelectedItem().toString();
                String phoneNumber = Utility.getStringFromEditText(phoneNumberEditText);
                String mobileNumber = Utility.getStringFromEditText(mobileNumberEditText);
                String email = Utility.getStringFromEditText(emailEditText);
                String website = Utility.getStringFromEditText(websiteEditText);
                String facebookId = Utility.getStringFromEditText(facebookIdEditText);
                String twitterId = Utility.getStringFromEditText(twitterEditText);
                String linkedInId = Utility.getStringFromEditText(linkedInEditText);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference contactsDatabaseReference = firebaseDatabase.getReference().child("contacts").child(uid);

                Contact contact = new Contact(name, organization, streetAddress, city, country, phoneNumber, mobileNumber,
                        email, website, facebookId, twitterId, linkedInId, uid);
                contactsDatabaseReference.push().setValue(contact);

                Utility.cleanUpEditText(nameEditText, organizationEditText, streetAddressEditText, cityEditText,
                        phoneNumberEditText, mobileNumberEditText, emailEditText, facebookIdEditText,
                        twitterEditText, linkedInEditText);

                Intent intent = new Intent(ContactAddActivity.this, ContactListActivity.class);
                startActivity(intent);
            }
        });
    }
}
