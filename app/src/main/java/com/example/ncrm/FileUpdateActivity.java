package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by shameer on 2018-03-17.
 */

public class FileUpdateActivity extends MainActivity {
    private File mSelectedFile;

    private EditText mFileNameEditText;
    private EditText mDescriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_file_update, frameLayout);

        Intent intent = getIntent();
        mSelectedFile = (File) intent.getSerializableExtra("object");

        setValuesFromDatabase(mSelectedFile);
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
        updateFile();
        navigateScene();
        return true;
    }

    private DatabaseReference getDatabaseReference() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String fileId = mSelectedFile.getId();
        DatabaseReference fileDatabaseReference = firebaseDatabase.getReference().child("files").child(uid).child(fileId);
        return fileDatabaseReference;
    }

    private void updateFile() {
        File updatedFile = getInputValues();
        DatabaseReference databaseReference = getDatabaseReference();
        databaseReference.setValue(updatedFile);
    }

    private void setValuesFromDatabase(File mSelectedFile) {
        mFileNameEditText = findViewById(R.id.fileNameEditText);
        mDescriptionEditText = findViewById(R.id.fileDescriptionEditText);

        mFileNameEditText.setText(mSelectedFile.getFileName());
        mDescriptionEditText.setText(mSelectedFile.getDescription());
    }

    private File getInputValues() {
        String filename = Utility.getStringFromEditText(mFileNameEditText);
        String description = Utility.getStringFromEditText(mDescriptionEditText);
        mSelectedFile.setFileName(filename);
        mSelectedFile.setDescription(description);
        mSelectedFile.setModifiedOn(System.currentTimeMillis());
        return mSelectedFile;
    }

    private void navigateScene() {
        Intent intent = new Intent(FileUpdateActivity.this, FileListActivity.class);
        startActivity(intent);
    }
}
