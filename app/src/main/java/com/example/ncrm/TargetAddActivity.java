package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by KN on 2018-03-15.
 */

public class TargetAddActivity extends MainActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_target_add, frameLayout);

        EditText targetItemEditText = (EditText) findViewById(R.id.targetItemEditText);

        EditText targetDateEditText = (EditText) findViewById(R.id.targetDateEditText);
        SetDate targetDate = new SetDate(this, targetDateEditText);

        EditText targetTimeEditText = (EditText) findViewById(R.id.targetTimeEditText);
        SetTime reminderTime = new SetTime(this, targetTimeEditText);

        Button addTargetBtn = (Button) findViewById(R.id.addTargetBtn);
        addTargetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText targetItemEditText = (EditText) findViewById(R.id.targetItemEditText);
                EditText targetDateEditText = (EditText) findViewById(R.id.targetDateEditText);
                EditText targetTimeEditText = (EditText) findViewById(R.id.targetTimeEditText);

                String targetItem = Utility.getStringFromEditText(targetItemEditText);
                String targetDate = Utility.getStringFromEditText(targetDateEditText);
                String targetTime = Utility.getStringFromEditText(targetTimeEditText);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference targetsDatabaseReference = firebaseDatabase.getReference().child("targets").child(uid);

                Target target = new Target(targetItem, targetDate, targetTime);

                targetsDatabaseReference.push().setValue(target);

                Utility.cleanUpEditText(targetItemEditText, targetDateEditText, targetTimeEditText);

                Intent intent = new Intent(TargetAddActivity.this, TargetListActivity.class);
                startActivity(intent);
            }
        });
    }
}
