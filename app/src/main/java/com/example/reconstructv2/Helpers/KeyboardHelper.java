package com.example.reconstructv2.Helpers;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

// contains soft keyboard functions
public class KeyboardHelper {

    // close a keyoboard
    public static void hideSoftKeyboard(Activity activity) {

        // check if the app is focused on a field
        if (activity.getCurrentFocus() != null) {

            // get inputMethodManager
            InputMethodManager inputMethodManager = 
                (InputMethodManager) activity.getSystemService( Activity.INPUT_METHOD_SERVICE);

            // use inputMethodManager to close the soft keyboard
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
