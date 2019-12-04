package com.example.reconstructv2.Helpers;

import android.widget.EditText;

public class InputValidator {


    public static boolean validateName(EditText field, String attributeName) {
        String firstName = field.getText().toString().trim();
        String capitalisedName = attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1).toLowerCase();

        if (firstName.isEmpty()) {
            field.setError("Field cannot be empty");
            return false;

        } else if (!(firstName.length() < 20)) {

            field.setError(capitalisedName + " must be less than 20 characters long");
            return false;

        } else if (!(firstName.length() > 2)) {
            field.setError(capitalisedName + " must be at least 3 characters long");
            return false;
        } else if (firstName.contains(" ")) {
            field.setError(capitalisedName + " cannot contain spaces");
            return false;
        } else {
            field.setError(null);
            return true;
        }

    }

    public static boolean validatePhone(EditText phoneEditText, String attributeName) {
        String phoneNumber = phoneEditText.getText().toString().trim();
        String capitalisedName = attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1).toLowerCase();
        if (phoneNumber.isEmpty()) {
            phoneEditText.setError("Field cannot be empty");
            return false;

        } else if (!(phoneNumber.length() < 12)) {
            phoneEditText.setError(capitalisedName + " needs to be less than 12 digits");
            return false;


        } else if (!(phoneNumber.length() > 8)) {
            phoneEditText.setError(capitalisedName + " needs to be more than 8 digits");
            return false;

        } else {
            phoneEditText.setError(null);
            return true;
        }

    }


}
