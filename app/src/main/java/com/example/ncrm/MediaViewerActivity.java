package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by shameer on 2018-03-16.
 */

public class MediaViewerActivity extends MainActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.media_viewer, frameLayout);

        ImageView imageViewer = findViewById(R.id.imageViewer);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        Glide.with(getApplicationContext()).load(url).into(imageViewer);
    }
}
