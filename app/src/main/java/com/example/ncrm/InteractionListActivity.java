package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by KN on 2018-03-16.
 */

public class InteractionListActivity extends MainActivity {
    RecyclerView mInteractionListRecyclerView;
    InteractionAdapter mInteractionAdapter;
    ArrayList<Interaction> mInteractionList;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mInteractionDatabaseReference;
    ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_interaction_list, frameLayout);

        mInteractionListRecyclerView = (RecyclerView) findViewById(R.id.interactionListRecyclerView);
        mInteractionListRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mInteractionListRecyclerView.setLayoutManager(layoutManager);
        mInteractionListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mInteractionListRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        mInteractionList = new ArrayList<>();
        mInteractionAdapter = new InteractionAdapter(mInteractionList, this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        getDataFromFirebase();

        FloatingActionButton createInteractionFAB = (FloatingActionButton) findViewById(R.id.addInteractionFAB);
        createInteractionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InteractionListActivity.this, InteractionAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataFromFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mInteractionDatabaseReference = mFirebaseDatabase.getReference().child("interactions").child(uid);
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Interaction interaction = dataSnapshot.getValue(Interaction.class);
                    String id = dataSnapshot.getKey();
                    interaction.setId(id);
                    mInteractionList.add(interaction);
                    mInteractionListRecyclerView.setAdapter(mInteractionAdapter);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mInteractionDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }
}
