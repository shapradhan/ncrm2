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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by shameer on 2018-03-10.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    ArrayList<File> mFileList = new ArrayList<>();
    Context mContext;
    private StorageReference mStorageReference;

    public FileAdapter(ArrayList<File> fileList, Context context) {
        mFileList = fileList;
        mContext = context;
    }

    @Override
    public FileAdapter.FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item_view, parent, false);
        return new FileAdapter.FileViewHolder(view, mContext, mFileList);
    }

    @Override
    public void onBindViewHolder(FileAdapter.FileViewHolder holder, int position) {
        File file = mFileList.get(position);
        holder.mFileNameItem.setText(file.getFileName());
        holder.mFileTypeItem.setText(file.getType());
        holder.mCreatedOnItem.setText(file.getCreatedOn().toString());
        attachChildListener(holder.mEditBtn, file, holder.getAdapterPosition());
        attachChildListener(holder.mDeleteBtn, file, holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    private void attachChildListener(final ImageButton button, final File file, final int adapterPosition) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (button.getId()) {
                    case R.id.editBtn:
                        editFile(file);
                        break;
                    case R.id.deleteBtn:
                        deleteFile(file, adapterPosition);
                        break;
                }
            }
        });
    }

    private void editFile(File file) {
        Intent intent = new Intent(mContext, FileUploadActivity.class);
        intent.putExtra("object", file);
        mContext.startActivity(intent);
    }

    private void deleteFile(File file, int adapterPosition) {
        String fileId = file.getId();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        StorageReference fileReference = mStorageReference.child("files/" + file.getFileName());
        fileReference.delete();

        DatabaseReference remindersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("files").child(uid).child(fileId);
        remindersDatabaseReference.removeValue();
        mFileList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mFileNameItem;
        TextView mFileTypeItem;
        TextView mCreatedOnItem;
        TextView mLastViewedOnItem;
        ImageButton mEditBtn;
        ImageButton mDeleteBtn;
        ArrayList<File> mFiles = new ArrayList<>();
        Context mContext;

        public FileViewHolder(View itemView, Context context, ArrayList<File> files) {
            super(itemView);
            mFiles = files;
            mContext = context;
            itemView.setOnClickListener(this);
            mFileNameItem = (TextView) itemView.findViewById(R.id.fileNameItem);
            mFileTypeItem = (TextView) itemView.findViewById(R.id.fileTypeItem);
            mCreatedOnItem = (TextView) itemView.findViewById(R.id.createdOnItem);
            mLastViewedOnItem = (TextView) itemView.findViewById(R.id.lastViewedOnItem);
            mEditBtn = (ImageButton) itemView.findViewById(R.id.editBtn);
            mDeleteBtn = (ImageButton) itemView.findViewById(R.id.deleteBtn);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            File file = this.mFiles.get(position);
            String[] fileTypeParts = file.getType().split("/");
            String fileType = fileTypeParts[0];
            if (fileType.equals("application")) {
                Intent intent = new Intent(mContext, MediaViewerActivity.class);
                intent.putExtra("fileType", fileTypeParts[1]);
                intent.putExtra("url", file.getUrl());
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, MediaViewerActivity.class);
                intent.putExtra("fileType", fileType);
                intent.putExtra("url", file.getUrl());
                mContext.startActivity(intent);
            }
        }
    }
}