package com.example.ncrm;

import android.widget.EditText;

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
}
