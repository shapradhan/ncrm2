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
 * Created by shameer on 2018-03-10.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    ArrayList<File> mFileList = new ArrayList<>();
    Context mContext;

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
//        holder.mLastViewedOnItem.setText(file.getLastViewedOn().toString());
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mFileNameItem;
        TextView mFileTypeItem;
        TextView mCreatedOnItem;
        TextView mLastViewedOnItem;
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