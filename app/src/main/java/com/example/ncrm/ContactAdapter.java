package com.example.ncrm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shameer on 2018-02-10.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private ArrayList<Contact> mContactArrayList = new ArrayList<>();
    Context mContext;

    public ContactAdapter(ArrayList<Contact> contactList, Context context) {
        mContactArrayList = contactList;
        mContext = context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_view, parent, false);
        return new ContactViewHolder(view, mContext, mContactArrayList);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ContactViewHolder holder, int position) {
        Contact contact = mContactArrayList.get(position);
        holder.mNameItem.setText(contact.getName());
        holder.mOrganizationItem.setText(contact.getOrganization());
    }

    @Override
    public int getItemCount() {
        return mContactArrayList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mNameItem;
        TextView mOrganizationItem;
        ArrayList<Contact> mContacts = new ArrayList<>();
        Context mContext;

        public ContactViewHolder(View itemView, Context context, ArrayList<Contact> contacts) {
            super(itemView);
            mContacts = contacts;
            mContext = context;
            itemView.setOnClickListener(this);
            mNameItem = (TextView) itemView.findViewById(R.id.contactNameItem);
            mOrganizationItem = (TextView) itemView.findViewById(R.id.contactOrganizationItem);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Contact contact = this.mContacts.get(position);
            Intent intent = new Intent(mContext, ContactDetailActivity.class);
            intent.putExtra("object", contact);
            mContext.startActivity(intent);
        }
    }
}