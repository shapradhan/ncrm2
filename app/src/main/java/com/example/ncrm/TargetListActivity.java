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

public class TargetListActivity extends MainActivity {
    RecyclerView mTargetListRecyclerView;
    TargetAdapter mTargetAdapter;
    ArrayList<Target> mTargetList;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mTargetDatabaseReference;
    ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_target_list, frameLayout);

        mTargetListRecyclerView = (RecyclerView) findViewById(R.id.targetListRecyclerView);
        mTargetListRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mTargetListRecyclerView.setLayoutManager(layoutManager);
        mTargetListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTargetListRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        mTargetList = new ArrayList<>();
        mTargetAdapter = new TargetAdapter(mTargetList, this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        getDataFromFirebase();

        FloatingActionButton createTargetFAB = (FloatingActionButton) findViewById(R.id.addTargetFAB);
        createTargetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TargetListActivity.this, TargetAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataFromFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mTargetDatabaseReference = mFirebaseDatabase.getReference().child("targets").child(uid);
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Target target = dataSnapshot.getValue(Target.class);
                    String id = dataSnapshot.getKey();
                    target.setId(id);
                    mTargetList.add(target);
                    mTargetListRecyclerView.setAdapter(mTargetAdapter);
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
            mTargetDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }
}
