package com.aryan.uperr.Fragments;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        //Toast.makeText(getActivity(), "Time from calendar at frag is" + hour + " : " + min, Toast.LENGTH_LONG).show();
        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hour, min, android.text.format.DateFormat.is24HourFormat(getActivity()));
    }
    //TODO: mimic the dialog close handler or on dialog fragment to update time
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {

    }
}
