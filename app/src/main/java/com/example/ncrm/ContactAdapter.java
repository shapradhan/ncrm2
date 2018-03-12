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
    ArrayList<Contact> mContactList = new ArrayList<>();
    Context mContext;
    ArrayList<String> mContactIDList = new ArrayList<>();
    ArrayList<String> mcid;

    public ContactAdapter(ArrayList<Contact> contactList, Context context) {
        mContactList = contactList;
        mContext = context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_view, parent, false);
        return new ContactViewHolder(view, mContext, mContactList);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ContactViewHolder holder, int position) {
        Contact contact = mContactList.get(position);
        holder.mContactNameItem.setText(contact.getName());
        holder.mContactOrganizationItem.setText(contact.getOrganization());
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mContactNameItem;
        TextView mContactOrganizationItem;
        ArrayList<Contact> mContacts = new ArrayList<>();
        Context mContext;

        public ContactViewHolder(View itemView, Context context, ArrayList<Contact> contacts) {
            super(itemView);
            mContacts = contacts;
            mContext = context;
            itemView.setOnClickListener(this);
            mContactNameItem = (TextView) itemView.findViewById(R.id.contactNameItem);
            mContactOrganizationItem = (TextView) itemView.findViewById(R.id.contactOrganizationItem);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Contact contact = this.mContacts.get(position);
            Intent intent = new Intent(mContext, ContactDetailActivity.class);
            intent.putExtra("object", contact);
            intent.putExtra("contactIDList", mContactIDList);
            mContext.startActivity(intent);
        }
    }

    private void getMeetingData(String contactId) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference contactMeetingDatabaseReference = firebaseDatabase.getReference().child("contacts").child(uid).child(contactId).child("meetings");
        contactMeetingDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mContactIDList.add(ds.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
