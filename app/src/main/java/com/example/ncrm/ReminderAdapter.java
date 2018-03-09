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
 * Created by shameer on 2018-03-09.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    ArrayList<Reminder> mReminderList = new ArrayList<>();
    Context mContext;

    public ReminderAdapter(ArrayList<Reminder> reminderList, Context context) {
        mReminderList = reminderList;
        mContext = context;
    }

    @Override
    public ReminderAdapter.ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item_view, parent, false);
        return new ReminderAdapter.ReminderViewHolder(view, mContext, mReminderList);
    }

    @Override
    public void onBindViewHolder(ReminderAdapter.ReminderViewHolder holder, int position) {
        Reminder reminder = mReminderList.get(position);
        holder.mReminderItem.setText(reminder.getReminderItem());
        holder.mReminderDate.setText(reminder.getReminderDate());
        holder.mReminderTime.setText(reminder.getReminderTime());
    }

    @Override
    public int getItemCount() {
        return mReminderList.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mReminderItem;
        TextView mReminderDate;
        TextView mReminderTime;
        ArrayList<Reminder> mReminders = new ArrayList<>();
        Context mContext;

        public ReminderViewHolder(View itemView, Context context, ArrayList<Reminder> reminders) {
            super(itemView);
            mReminders = reminders;
            mContext = context;
            itemView.setOnClickListener(this);
            mReminderItem = (TextView) itemView.findViewById(R.id.reminderItem);
            mReminderDate = (TextView) itemView.findViewById(R.id.reminderDate);
            mReminderTime = (TextView) itemView.findViewById(R.id.reminderTime);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Reminder reminder = this.mReminders.get(position);
//            Intent intent = new Intent(mContext, MeetingDetailActivity.class);
//            intent.putExtra("object", meeting);
//            mContext.startActivity(intent);
        }
    }
}
