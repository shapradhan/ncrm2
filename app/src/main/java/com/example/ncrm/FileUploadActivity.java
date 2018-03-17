package com.example.ncrm;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

/**
 * Created by shameer on 2018-03-09.
 */

public class FileUploadActivity extends MainActivity {
    private static final int PICK_FILE_REQUEST = 80;
    private Uri mFilePath;
    private StorageReference mStorageReference;
    private ProgressBar mFileUploadProgressBar;
    private String mFileName;
    private TextView mFileUploadSizeTextView;
    private String mFileMimeType;
    private EditText mFileNameEditText;
    private EditText mFileDescriptionEditText;
    private ArrayList<String> mFileNameList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_file_upload, frameLayout);

        mStorageReference = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        mFileNameList = intent.getStringArrayListExtra("fileList");
        System.out.println("FL " + mFileNameList);

        mFileUploadProgressBar = (ProgressBar) findViewById(R.id.fileUploadProgressBar);
        mFileUploadSizeTextView = (TextView) findViewById(R.id.fileUploadSizeTextView);

        Button selectFileBtn = (Button) findViewById(R.id.selectFileBtn);

        mFileNameEditText = (EditText) findViewById(R.id.fileNameEditText);
        mFileDescriptionEditText = (EditText) findViewById(R.id.fileDescriptionEditText);

        selectFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFileSelector();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuItem menuItem = menu.add(Menu.NONE, 1000, Menu.NONE, R.string.upload_caps);
        menuItem.setShowAsAction(1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        mFileName = Utility.getStringFromEditText(mFileNameEditText);
        String fileDescription = Utility.getStringFromEditText(mFileDescriptionEditText);

        String fileCategories[] = null;
        String fileType = null;

        if (mFilePath != null) {
            fileCategories = mFileMimeType.split("/");
            fileType = fileCategories[0];
        }

        if (mFilePath == null) {
            Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_SHORT).show();
        } else if (!(fileType.equals("image") || fileType.equals("video"))) {
            Toast.makeText(getApplicationContext(), "Only videos and images are allowed to be uploaded", Toast.LENGTH_SHORT).show();
        } else if (fileNameExistenceChecker(mFileName)) {
            Toast.makeText(getApplicationContext(), "Given file name already exists", Toast.LENGTH_SHORT).show();
        } else {
            new UploadFile().execute();
        }
        return true;
    }

    private void displayFileSelector() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Image is selected
            mFilePath = data.getData();
            mFileMimeType = getContentResolver().getType(mFilePath);
            TextView selectedFileNameTextView = findViewById(R.id.selectedFileNameTextView);
            selectedFileNameTextView.setText(mFileMimeType + " file selected.");
        }
    }

    private void uploadFile() {
        StorageReference fileReference = mStorageReference.child("files/" + mFileName);
        fileReference.putFile(mFilePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri uploadUrl = taskSnapshot.getDownloadUrl();
                        mFileUploadProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "File successfully uploaded", Toast.LENGTH_SHORT).show();

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DatabaseReference filesDatabaseReference = firebaseDatabase.getReference().child("files").child(uid);

                        File file = new File(mFileName, System.currentTimeMillis(), mFileMimeType, uid, taskSnapshot.getDownloadUrl().toString());
                        filesDatabaseReference.push().setValue(file);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        mFileUploadProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "File could not be uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double TOTAL_PERCENTAGE = 100.0;
                        double percentageUploaded = (TOTAL_PERCENTAGE * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        mFileUploadProgressBar.setVisibility(View.VISIBLE);
                        mFileUploadSizeTextView.setVisibility(View.VISIBLE);
                        mFileUploadProgressBar.setProgress((int) percentageUploaded);
                        mFileUploadSizeTextView.setText(taskSnapshot.getBytesTransferred() + " bytes of " + taskSnapshot.getTotalByteCount() + " bytes");
                    }
                });
    }

    private boolean fileNameExistenceChecker(String newFileName) {
        for (String fileName : mFileNameList) {
            if (fileName.equals(newFileName)) {
                return true;
            }
        }
        return false;
    }

    public class UploadFile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Utility.cleanUpEditText(mFileNameEditText, mFileDescriptionEditText);
            Intent intent = new Intent(FileUploadActivity.this, FileListActivity.class);
            startActivity(intent);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            uploadFile();
            return null;
        }
    }
}
