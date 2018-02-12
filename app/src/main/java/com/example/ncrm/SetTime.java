package com.example.ncrm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by shameer on 2018-02-12.
 */

class SetTime implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private Context mContext;
    private EditText mTimeEditText;
    private Calendar mCalendar;

    public SetTime(Context context, EditText timeEditText) {
        mContext = context;
        mTimeEditText = timeEditText;
        mCalendar = Calendar.getInstance();
        mTimeEditText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mTimeEditText.setInputType(0);
        new TimePickerDialog(
                mContext,
                this,
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE),
                true).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String timeFormat = "hh:mm aa";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat, Locale.getDefault());
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        mTimeEditText.setText(simpleDateFormat.format(mCalendar.getTime()));
    }
}