package com.example.ncrm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KN on 2018-03-15.
 */

public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.TargetViewHolder> {
    ArrayList<Target> mTargetList = new ArrayList<>();
    Context mContext;

    public TargetAdapter(ArrayList<Target> targetList, Context context) {
        mTargetList = targetList;
        mContext = context;
    }

    @Override
    public TargetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.target_item_view, parent, false);
        return new TargetViewHolder(view, mContext, mTargetList);
    }

    @Override
    public void onBindViewHolder(TargetViewHolder holder, int position) {
        Target target = mTargetList.get(position);
        holder.mTargetItem.setText(target.getTargetItem());
        holder.mTargetDate.setText(target.getTargetDate());
        holder.mTargetTime.setText(target.getTargetTime());
    }

    @Override
    public int getItemCount() {
        return mTargetList.size();
    }

    public class TargetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTargetItem;
        TextView mTargetDate;
        TextView mTargetTime;
        ArrayList<Target> mTargets = new ArrayList<>();
        Context mContext;

        public TargetViewHolder(View itemView, Context context, ArrayList<Target> targets) {
            super(itemView);
            mTargets = targets;
            mContext = context;
            itemView.setOnClickListener(this);
            mTargetItem = (TextView) itemView.findViewById(R.id.targetItemEditText);
            System.out.println("Mtv" + mTargetItem);
            mTargetDate = (TextView) itemView.findViewById(R.id.targetDateEditText);
            mTargetTime = (TextView) itemView.findViewById(R.id.targetTimeEditText);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Target target = this.mTargets.get(position);
//            Intent intent = new Intent(mContext, MeetingDetailActivity.class);
//            intent.putExtra("object", meeting);
//            mContext.startActivity(intent);
        }
    }
}
