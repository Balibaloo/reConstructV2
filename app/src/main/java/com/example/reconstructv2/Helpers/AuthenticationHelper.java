package com.example.reconstructv2.Helpers;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class AuthenticationHelper {

    // hash a pasword salted with a salt and the users username
    public static String hashAndSalt(String masterSalt,String password,String username){

        try {

            // get an instance of an MD5 hasher
            MessageDigest md = MessageDigest.getInstance("MD5");

            // set the hash salt to the master salt
            md.update(masterSalt.getBytes());

            // concatenate the usernae and password
            String concatString = password + username;

            // hash the concatenated string with the master salt
            return new String(md.digest(concatString.getBytes()));
        }

        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown : " + e);

        } catch (NullPointerException e) {
            System.out.println("Exception thrown : " + e);
        }

        return null;
    }

    // generate a base 64 encoded authentication header
    public static String getHeaderB64(String Username,String saltedHashedPassword){

        // concatenate the username and password with a colon
        String AuthHeaderUP = Username + ":" + saltedHashedPassword;

        // base 64 encode the header string

        return "Basic " + Base64.encodeToString(AuthHeaderUP.getBytes(),Base64.NO_WRAP);

    }
}
