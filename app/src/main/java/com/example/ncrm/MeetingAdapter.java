package com.example.ncrm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shameer on 2018-02-15.
 */

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {
    ArrayList<Meeting> mMeetingList = new ArrayList<>();
    Context mContext;

    public MeetingAdapter(ArrayList<Meeting> meetingList, Context context) {
        mMeetingList = meetingList;
        mContext = context;
    }

    @Override
    public MeetingAdapter.MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_item_view, parent, false);
        return new MeetingAdapter.MeetingViewHolder(view, mContext, mMeetingList);
    }

    @Override
    public void onBindViewHolder(MeetingAdapter.MeetingViewHolder holder, int position) {
        Meeting meeting = mMeetingList.get(position);
        holder.mMeetingTitleItem.setText(meeting.getTitle());
        holder.mMeetingDateItem.setText(meeting.getDate());
        holder.mMeetingTimeItem.setText(meeting.getTime());
    }

    @Override
    public int getItemCount() {
        return mMeetingList.size();
    }

    public class MeetingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mMeetingTitleItem;
        TextView mMeetingDateItem;
        TextView mMeetingTimeItem;
        ArrayList<Meeting> mMeetings = new ArrayList<>();
        Context mContext;

        public MeetingViewHolder(View itemView, Context context, ArrayList<Meeting> meetings) {
            super(itemView);
            mMeetings = meetings;
            mContext = context;
            itemView.setOnClickListener(this);
            mMeetingTitleItem = (TextView) itemView.findViewById(R.id.meetingTitleItem);
            mMeetingDateItem = (TextView) itemView.findViewById(R.id.meetingDateItem);
            mMeetingTimeItem = (TextView) itemView.findViewById(R.id.meetingTimeItem);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Meeting meeting = this.mMeetings.get(position);
            Intent intent = new Intent(mContext, MeetingDetailActivity.class);
            intent.putExtra("object", meeting);
            mContext.startActivity(intent);
        }
    }
}