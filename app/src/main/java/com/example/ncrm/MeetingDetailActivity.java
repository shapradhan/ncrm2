package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by shameer on 2018-02-19.
 */

public class MeetingDetailActivity extends MainActivity {

    Intent mIntent;
    Meeting mSelectedMeeting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_meeting_detail, frameLayout);

        mIntent = getIntent();
        mSelectedMeeting = (Meeting) mIntent.getSerializableExtra("object");

        TextView meetingTitle = (TextView) findViewById(R.id.meetingTitle);
        meetingTitle.setText(mSelectedMeeting.getTitle());
        TextView meetingDate = (TextView) findViewById(R.id.meetingDate);
        meetingDate.setText(mSelectedMeeting.getDate());
        TextView meetingTime = (TextView) findViewById(R.id.meetingTime);
        meetingTime.setText(mSelectedMeeting.getTime());

    }
}
