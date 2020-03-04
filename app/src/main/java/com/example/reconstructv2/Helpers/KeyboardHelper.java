package com.example.reconstructv2.Helpers;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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


    public static void submitOnEnterKey(EditText textField, Button submitButton){

        textField.setOnEditorActionListener((v, actionId, event) -> {
            Boolean handled = false;

            // check if the key pressed was the enter key
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                // call on click listener
                submitButton.callOnClick();
                handled = true;
            }

            return handled;
        });

    }

}

