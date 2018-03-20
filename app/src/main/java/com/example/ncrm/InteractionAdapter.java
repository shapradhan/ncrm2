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

public class InteractionAdapter extends RecyclerView.Adapter<InteractionAdapter.InteractionViewHolder> {
    ArrayList<Interaction> mInteractionList = new ArrayList<>();
    Context mContext;

    public InteractionAdapter(ArrayList<Interaction> interactionList, Context context) {
        mInteractionList = interactionList;
        mContext = context;
    }

    @Override
    public InteractionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interaction_item_view, parent, false);
        return new InteractionViewHolder(view, mContext, mInteractionList);
    }

    @Override
    public void onBindViewHolder(InteractionViewHolder holder, int position) {
        Interaction interaction = mInteractionList.get(position);
        holder.mInteractionItem.setText(interaction.getInteractionItem());
        holder.mInteractionDate.setText(interaction.getInteractionDate());
        holder.mInteractionTime.setText(interaction.getInteractionTime());
    }

    @Override
    public int getItemCount() {
        return mInteractionList.size();
    }

    public class InteractionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mInteractionItem;
        TextView mInteractionDate;
        TextView mInteractionTime;
        ArrayList<Interaction> mInteraction = new ArrayList<>();
        Context mContext;

        public InteractionViewHolder(View itemView, Context context, ArrayList<Interaction> interactions) {
            super(itemView);
            mInteraction = interactions;
            mContext = context;
            itemView.setOnClickListener(this);
            mInteractionItem = (TextView) itemView.findViewById(R.id.interactionItem);
            mInteractionDate = (TextView) itemView.findViewById(R.id.interactionDate);
            mInteractionTime = (TextView) itemView.findViewById(R.id.interactionTime);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Interaction interaction = this.mInteraction.get(position);
//            Intent intent = new Intent(mContext, MeetingDetailActivity.class);
//            intent.putExtra("object", meeting);
//            mContext.startActivity(intent);
        }
    }
}
