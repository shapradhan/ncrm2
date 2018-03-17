package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    private String mSortMethod;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_file_list, frameLayout);

        Intent intent = getIntent();
        mSortMethod = intent.getStringExtra("sortMethod");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_sortByFileName).setVisible(true);
        menu.findItem(R.id.action_sortByFileType).setVisible(true);
        menu.findItem(R.id.action_sortByDate).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        Intent intent = new Intent(FileListActivity.this, FileListActivity.class);
        switch (id) {
            case R.id.action_sortByFileName:
                intent.putExtra("sortMethod", "name");
                break;
            case R.id.action_sortByFileType:
                intent.putExtra("sortMethod", "type");
                break;
            case R.id.action_sortByDate:
                intent.putExtra("sortMethod", "date");
                break;
        }
        startActivity(intent);
        return true;
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
                    sortArray(mFileList, mSortMethod);
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

    private void sortArray(ArrayList<File> arrayList, final String sortMethod) {
        Collections.sort(arrayList, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                if (sortMethod.equals("name")) {
                    return file1.getFileName().compareTo(file2.getFileName());
                } else if (sortMethod.equals("type")) {
                    return file1.getType().compareTo(file2.getType());
                } else if (sortMethod.equals("date")) {
                    return file1.getCreatedOn().compareTo(file2.getCreatedOn());
                }
                return 1;
            }
        });
    }
}
