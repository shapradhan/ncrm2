package com.example.ncrm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shameer on 2018-03-16.
 */

public class MediaViewerActivity extends MainActivity {
    private ImageView mImageView;
    private File mSelectedFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.media_viewer, frameLayout);

        mImageView = findViewById(R.id.imageViewer);
        VideoView videoPlayer = findViewById(R.id.videoPlayer);

        Intent intent = getIntent();
        mSelectedFile = (File) intent.getSerializableExtra("object");
        String[] fileTypeParts = mSelectedFile.getType().split("/");
        String fileType = fileTypeParts[0];
        String url = mSelectedFile.getUrl();

        Uri uri = Uri.parse(url);
        switch (fileType) {
            case "image":
                videoPlayer.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(url).into(mImageView);
                break;
            case "video":
                mImageView.setVisibility(View.GONE);
                videoPlayer.setVisibility(View.VISIBLE);
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(videoPlayer);
                videoPlayer.setMediaController(mediaController);
                videoPlayer.setVideoURI(uri);
                videoPlayer.requestFocus();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_viewDetails).setVisible(true);
        menu.findItem(R.id.action_update).setVisible(true);
        menu.findItem(R.id.action_delete).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_viewDetails:
                AlertDialog alertDialog = new AlertDialog.Builder(MediaViewerActivity.this).create();
                alertDialog.setTitle(mSelectedFile.getFileName());
                alertDialog.setMessage(mSelectedFile.getDescription() + "\nCreated on: " + convertEpochTimeToDateTime(mSelectedFile.getCreatedOn()));

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;
            case R.id.action_update:
                Intent intent = new Intent(getApplicationContext(), FileUpdateActivity.class);
                intent.putExtra("object", mSelectedFile);
                startActivity(intent);
                break;
            case R.id.action_delete:
                String fileId = mSelectedFile.getId();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                StorageReference fileReference = FirebaseStorage.getInstance().getReference().child("files/" + mSelectedFile.getFileName());
                fileReference.delete();

                DatabaseReference remindersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("files").child(uid).child(fileId);
                remindersDatabaseReference.removeValue();
                navigateToList(getApplicationContext());
                break;
        }
        return true;
    }

    private String convertEpochTimeToDateTime(Long epochTime) {
        Date date = new Date(epochTime);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    private void navigateToList(Context context) {
        Intent intent = new Intent(context, FileListActivity.class);
        intent.putExtra("sortMethod", "name");
        startActivity(intent);
    }
}
