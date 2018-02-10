package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by shameer on 2018-02-10.
 */

public class ContactDetailActivity extends AppCompatActivity {
    TextView mContactItemName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        Intent intent = getIntent();

        mContactItemName = (TextView) findViewById(R.id.contactItemName);
        mContactItemName.setText(intent.getStringExtra("name"));

    }
}
