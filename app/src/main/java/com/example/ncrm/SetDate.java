package com.example.ncrm;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by shameer on 2018-02-12.
 */

class SetDate implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private Context mContext;
    private EditText mDateEditText;
    private Calendar mCalendar;

    public SetDate(Context context, EditText dateEditText) {
        mContext = context;
        mDateEditText = dateEditText;
        mCalendar = Calendar.getInstance();
        mDateEditText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mDateEditText.setInputType(0);
        new DatePickerDialog(
                mContext,
                this,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateFormat = "EE, dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        mDateEditText.setText(simpleDateFormat.format(mCalendar.getTime()));
    }
}