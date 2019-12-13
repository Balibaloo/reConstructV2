package com.example.reconstructv2.Helpers;

import android.widget.EditText;

import java.util.List;

public class InputValidator {

    public static boolean validateUsername(EditText field){
        if(true){
            return true;
        }else {
            return false;
        }

    }

    public static boolean validatePassword(EditText passwordField, String username){
        if(true){
            return true;
        }else {
            return false;
        }
    }

    public static boolean validatePasswordsMatch(EditText paswordMain,EditText passwordSecond){

        if (paswordMain.getText().toString().trim().equals(passwordSecond.getText().toString().trim())){
            return true;
        } else {
            passwordSecond.setError("passwords dont match");
            return false;
        }
    }

    public static boolean validateEmail(EditText field){
        String[] splitEmail = field.getText().toString().trim().split("@");
        if(splitEmail.length == 2){
            // check only one @ character is in the string
            String prefix = splitEmail[0];
            String domain = splitEmail[1];

            if (!InputValidator.validatePerfix(prefix, field)) {
                return false;
            }

            if (!InputValidator.validateDomain(domain,field)){
                return false;
            }

            return true;

        }else {
            field.setError("Email can only contain one @ symbol");
            return false;
        }
    }

    private static boolean validatePerfix(String prefix, EditText emailField){
        //Todo add prefix validation
        return true;
    }

    public static boolean validateDomain(String domain, EditText emailField){
        //Todo add domain validation
        return true;
    }

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
