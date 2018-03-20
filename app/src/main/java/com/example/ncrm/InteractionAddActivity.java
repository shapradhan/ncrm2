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

public class InteractionAddActivity extends MainActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_interaction_add, frameLayout);

        EditText interactionItemEditText = (EditText) findViewById(R.id.interactionItemEditText);

        EditText interactionDateEditText = (EditText) findViewById(R.id.interactionDateEditText);
        SetDate interactionDate = new SetDate(this, interactionDateEditText);

        EditText InteractionTimeEditText = (EditText) findViewById(R.id.interactionTimeEditText);
        SetTime interactionTime = new SetTime(this, InteractionTimeEditText);

        Button addInteractionBtn = (Button) findViewById(R.id.addInterBtn);
        addInteractionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText interactionItemEditText = (EditText) findViewById(R.id.interactionItemEditText);
                EditText interactionDateEditText = (EditText) findViewById(R.id.interactionDateEditText);
                EditText interactionTimeEditText = (EditText) findViewById(R.id.interactionTimeEditText);

                String interactionItem = Utility.getStringFromEditText(interactionItemEditText);
                String interactionDate = Utility.getStringFromEditText(interactionDateEditText);
                String interactionTime = Utility.getStringFromEditText(interactionTimeEditText);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference interactionDatabaseReference = firebaseDatabase.getReference().child("interaction").child(uid);

                Interaction interaction = new Interaction(interactionItem, interactionDate, interactionTime);
                interactionDatabaseReference.push().setValue(interaction);

                Utility.cleanUpEditText(interactionItemEditText, interactionDateEditText, interactionTimeEditText);

                Intent intent = new Intent(InteractionAddActivity.this, InteractionListActivity.class);
                startActivity(intent);
            }
        });
    }
}
