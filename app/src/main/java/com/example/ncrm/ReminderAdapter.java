package com.example.ncrm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by shameer on 2018-03-09.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    private ArrayList<Reminder> mReminderArrayList = new ArrayList<>();
    private Context mContext;

    public ReminderAdapter(ArrayList<Reminder> reminderList, Context context) {
        mReminderArrayList = reminderList;
        mContext = context;
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item_view, parent, false);
        return new ReminderViewHolder(view, mContext, mReminderArrayList);
    }

    @Override
    public void onBindViewHolder(ReminderAdapter.ReminderViewHolder holder, int position) {
        final Reminder reminder = mReminderArrayList.get(position);
        holder.mReminderItem.setText(reminder.getReminderItem());
        holder.mReminderDate.setText(reminder.getReminderDate());
        holder.mReminderTime.setText(reminder.getReminderTime());
        attachChildListener(holder.mEditBtn, reminder, holder.getAdapterPosition());
        attachChildListener(holder.mDeleteBtn, reminder, holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return mReminderArrayList.size();
    }

    private void attachChildListener(final ImageButton button, final Reminder reminder, final int adapterPosition) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (button.getId()) {
                    case R.id.editBtn:
                        editReminder(reminder);
                        break;
                    case R.id.deleteBtn:
                        deleteReminder(reminder, adapterPosition);
                        break;
                }
            }
        });
    }

    private void editReminder(Reminder reminder) {
        Intent intent = new Intent(mContext, ReminderUpdateActivity.class);
        intent.putExtra("object", reminder);
        mContext.startActivity(intent);
    }

    private void deleteReminder(Reminder reminder, int adapterPosition) {
        String reminderId = reminder.getId();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference remindersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("reminders").child(uid).child(reminderId);
        remindersDatabaseReference.removeValue();
        mReminderArrayList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
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
