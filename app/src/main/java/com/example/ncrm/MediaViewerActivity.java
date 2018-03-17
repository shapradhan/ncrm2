package com.example.ncrm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

/**
 * Created by shameer on 2018-03-16.
 */

public class MediaViewerActivity extends MainActivity {
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.media_viewer, frameLayout);

        mImageView = findViewById(R.id.imageViewer);
        VideoView videoPlayer = findViewById(R.id.videoPlayer);

        Intent intent = getIntent();
        String fileType = intent.getStringExtra("fileType");
        String url = intent.getStringExtra("url");
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
}
