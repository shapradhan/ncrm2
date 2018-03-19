package com.example.ncrm;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by shameer on 2018-02-20.
 */

public class Utility {
    public static void cleanUpEditText(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText = null;
        }
    }

    public static String getStringFromEditText(EditText editText) {
        String editTextString = editText.getText().toString();
        if (editTextString != null && editTextString.length() > 0) {
            return editTextString;
        } else {
            return "";
        }
    }

    public static DatabaseReference getDatabaseReference(FirebaseDatabase db, String node, String uid) {
        DatabaseReference databaseReference = db.getReference().child(node).child(uid);
        return databaseReference;
    }

    public static void getListViewSize(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // Get total height
        int totalHeight = 0;
        for (int size = 0; size < listAdapter.getCount(); size++) {
            View listItem = listAdapter.getView(size, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void notConnectedToNetworkDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("You are not connected to the Internet. This app needs Internet to work.");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                System.exit(0);
            }
        });
        AlertDialog noInternetDialog = builder.create();
        noInternetDialog.show();
    }
}
