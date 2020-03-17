package com.example.reconstructv2.Helpers;

import android.widget.EditText;
import android.widget.TextView;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;
import java.util.List;


// used to validate user input
public class InputValidator {

    public static boolean validateUsername(EditText field){
        if(field.getText().toString().length() > 2){
            return true;
        }else {

            field.setError("the username must be at least 3 characters");
            return false;
        }
    }

    public static boolean validatePassword(EditText passwordField, String username){

        // splits the username on ,.-_#
        String[] username_parts = username.split(",|.|-|_|#");

        String password = passwordField.getText().toString();

        int password_feature_count = 0;


        // check if the password contains a capital letter
        if (password.matches(".*[A-Z].*")){
            password_feature_count ++;
        }

        // check if the password contains a lowercase letter
        if (password.matches(".*[a-z].*")){
            password_feature_count ++;
        }

        // check if the password contains a digit
        if (password.matches(".*[0-9].*")){
            password_feature_count ++;
        }

        // check if the password contains a non letter or digit character like @ or -
        if (password.matches(".*[^a-z|A-Z|0-9].*")){
            password_feature_count ++;

        }

        // checks if the password contains parts of the username
        // ignoring case
        boolean username_in_password = false;
        for (String str : username_parts){
            username_in_password = username_in_password || password.toLowerCase().contains(str.toLowerCase());
        }


        if(password_feature_count >= 3 && !username_in_password){
            return true;

        } else if (username_in_password) {
            passwordField.setError("the password contains parts of the username");

        }else {

            passwordField.setError("please check the password requirements");
        }

        return false;
    }

    public static boolean validatePasswordsMatch(EditText paswordMain,EditText passwordSecond){

        // check that the values in both EditTexts match
        if (paswordMain.getText().toString().trim().equals(passwordSecond.getText().toString().trim())){
            return true;

        } else {

            // set an error on the second EditText
            passwordSecond.setError("passwords dont match");
            return false;
        }
    }

    // validate an email input
    public static boolean validateEmail(EditText field){

        // separate the email into two parts
        String[] splitEmail = field.getText().toString().trim().split("@");

        // check only one @ character is in the string
        if(splitEmail.length == 2){
            String prefix = splitEmail[0];
            String domain = splitEmail[1];

            return
                InputValidator.validatePerfix(prefix, field)
                &&
                InputValidator.validateDomain(domain,field);

        }else {
            // set an error on the TextEdit
            field.setError("Email can only contain one @ symbol");
            return false;
        }
    }

    private static boolean validatePerfix(String prefix, EditText emailField){


        // ensures that the prefix
        // starts with a letter
        // if it contains a . or - or _ character, they must have letters either side

        if (prefix.matches("^[a-zA-Z0-9]+([.\\-_]?[a-zA-Z0-9]+)+$")){
            return true;
        } else {

            emailField.setError("email prefix invalid");
            return false;
        }



    }

    public static boolean validateDomain(String domain, EditText emailField){

        // ensures that the domain
        // starts with a letter
        // if it contains a . or - or _ character, they must have letters either side
        // ends with a . followed by at least 2 letters

        if (domain.matches("^[a-z|A-Z]+([.\\-_]?[a-z|A-Z]+)+.[a-z|A-Z]{2,}$")){
            return true;
        } else {

            emailField.setError("email domain invalid");
            return false;
        }
    }

    public static boolean validateName(EditText field, String attributeName) {

        // extract and trim the first name 
        String firstName = field.getText().toString().trim();

        // capitalise the first letter of the name
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
            // clear the error on the TextEdit
            field.setError(null);
            return true;
        }

    }

    public static boolean validatePhone(EditText phoneEditText, String attributeName) {

        // extract and trim the phone number
        String phoneNumber = phoneEditText.getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            phoneEditText.setError("Field cannot be empty");
            return false;

        } else if (!(phoneNumber.length() > 8)) {
            phoneEditText.setError(attributeName + " needs to be more than 8 digits");
            return false;

        } else if (!(phoneNumber.length() < 12)) {
            phoneEditText.setError(attributeName + " needs to be less than 12 digits");
            return false;

        } else {

            // clear the error on the EditText
            phoneEditText.setError(null);
            return true;
        }
    }




    public static boolean validateItemText(EditText editText, String attributeName){

        if (editText.getText().length() == 0){
            editText.setError(attributeName + " field cannot be empty");
            return false;

        } else {
            return false;
        }
    }

    // validate a field via an EditText
    public static boolean validateItemText(TextView editText, String attributeName){

        if (editText.getText().length() == 0){
            editText.setError(attributeName + " field cannot be empty");
            return false;

        } else {
            return false;
        }
    }

    // validate a filed with only its text
    public static boolean validateItemText(String text){

        if (text.length() == 0){
            return false;

        } else {
            return true;
        }

    }


}
