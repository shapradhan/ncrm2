package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by shameer on 2018-02-10.
 */

public class ContactListActivity extends AppCompatActivity {
    FloatingActionButton mCreateContactFAB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        mCreateContactFAB = (FloatingActionButton) findViewById(R.id.addContactFAB);
        mCreateContactFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this, ContactAddActivity.class);
                startActivity(intent);
            }
        });
    }
}
