package com.example.ncrm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shameer on 2018-02-10.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    List<Contact> mContactList;

    public ContactAdapter(List<Contact> contactList) {
        mContactList = contactList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_view, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ContactViewHolder holder, int position) {
        Contact contact = mContactList.get(position);
        holder.contactNameItem.setText(contact.getmName());
        holder.contactOrganizationItem.setText(contact.getmOrganization());
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactNameItem;
        TextView contactOrganizationItem;

        public ContactViewHolder(View itemView) {
            super(itemView);

            contactNameItem = (TextView) itemView.findViewById(R.id.contactNameItem);
            contactOrganizationItem = (TextView) itemView.findViewById(R.id.contactOrganizationItem);
        }
    }
}
