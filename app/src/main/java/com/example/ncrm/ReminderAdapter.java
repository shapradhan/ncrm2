package com.example.ncrm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item_view, parent, false);
        return new ReminderViewHolder(view, mContext, mReminderList);
    }

    @Override
    public void onBindViewHolder(ReminderAdapter.ReminderViewHolder holder, int position) {
        final Reminder reminder = mReminderList.get(position);
        holder.mReminderItem.setText(reminder.getReminderItem());
        holder.mReminderDate.setText(reminder.getReminderDate());
        holder.mReminderTime.setText(reminder.getReminderTime());
        attachChildListener(holder.mEditBtn, reminder);
        attachChildListener(holder.mDeleteBtn, reminder);
    }

    @Override
    public int getItemCount() {
        return mReminderList.size();
    }

    private void attachChildListener(final ImageButton button, final Reminder reminder) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (button.getId()) {
                    case R.id.editBtn:
                        editReminder(reminder);
                        break;
                    case R.id.deleteBtn:
                        deleteReminder(reminder);
                        break;
                }
            }
        });
    }

    private void editReminder(Reminder reminder) {
    }

    private void deleteReminder(Reminder reminder) {
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mReminderItem;
        TextView mReminderDate;
        TextView mReminderTime;
        ImageButton mEditBtn;
        ImageButton mDeleteBtn;
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
            mEditBtn = (ImageButton) itemView.findViewById(R.id.editBtn);
            mDeleteBtn = (ImageButton) itemView.findViewById(R.id.deleteBtn);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
