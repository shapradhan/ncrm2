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
 * Created by shameer on 2018-03-09.
 */

public class FileListActivity extends MainActivity {
    RecyclerView mFileListRecyclerView;
    FileAdapter mFileAdapter;
    ArrayList<File> mFileList;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mFilesDatabaseReference;
    ChildEventListener mChildEventListener;
    ArrayList<String> mFileNameList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_file_list, frameLayout);

        mFileListRecyclerView = (RecyclerView) findViewById(R.id.fileListRecyclerView);
        mFileListRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mFileListRecyclerView.setLayoutManager(layoutManager);
        mFileListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFileListRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        mFileList = new ArrayList<>();
        mFileNameList = new ArrayList<>();
        mFileAdapter = new FileAdapter(mFileList, this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        getDataFromFirebase();

        FloatingActionButton createFileFAB = (FloatingActionButton) findViewById(R.id.addFileFAB);
        createFileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FileListActivity.this, FileUploadActivity.class);
                intent.putExtra("fileList", mFileNameList);
                startActivity(intent);
            }
        });
    }

    private void getDataFromFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFilesDatabaseReference = mFirebaseDatabase.getReference().child("files").child(uid);
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    File file = dataSnapshot.getValue(File.class);
                    String id = dataSnapshot.getKey();
                    file.setId(id);
                    mFileNameList.add(file.getFileName());
                    mFileList.add(file);
                    mFileListRecyclerView.setAdapter(mFileAdapter);
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
            mFilesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }
}
