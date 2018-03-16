package com.example.ncrm;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_file_upload, frameLayout);

        mStorageReference = FirebaseStorage.getInstance().getReference();

        mFileUploadProgressBar = (ProgressBar) findViewById(R.id.fileUploadProgressBar);
        mFileUploadSizeTextView = (TextView) findViewById(R.id.fileUploadSizeTextView);

        Button selectFileBtn = (Button) findViewById(R.id.selectFileBtn);
        final Button uploadFileBtn = (Button) findViewById(R.id.uploadFileBtn);

        mFileNameEditText = (EditText) findViewById(R.id.fileNameEditText);
        mFileDescriptionEditText = (EditText) findViewById(R.id.fileDescriptionEditText);

        selectFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFileSelector();
            }
        });

        uploadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFileName = Utility.getStringFromEditText(mFileNameEditText);
                String fileDescription = Utility.getStringFromEditText(mFileDescriptionEditText);
                new UploadFile().execute();
            }
        });
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
        }
    }

    private void uploadFile() {
        if (mFilePath != null) {
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
        } else {
        }
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
