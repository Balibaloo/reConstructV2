package com.example.reconstructv2.Helpers;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthenticationHelper {

    public static String hashAndSalt(String masterSalt,String password,String username){
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(masterSalt.getBytes());
            String concatString = password + username;
            String saltedHashedPassword = new String(md.digest(concatString.getBytes()));

            return saltedHashedPassword;
        }

        catch (NoSuchAlgorithmException e) {

            System.out.println("Exception thrown : " + e);
        }
        catch (NullPointerException e) {

            System.out.println("Exception thrown : " + e);
        }
        return null;
    }


    public static String getHeaderB64(String Username,String saltedHashedPassword){
        String AuthHeaderUP = Username + ":" + saltedHashedPassword;
        String base64EncodedUP =  "Basic " + Base64.encodeToString(AuthHeaderUP.getBytes(),Base64.NO_WRAP);

        return base64EncodedUP;

    }
}
