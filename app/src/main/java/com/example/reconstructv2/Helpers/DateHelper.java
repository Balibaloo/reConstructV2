package com.example.reconstructv2.Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    // convert / separated date string to sql format
    public static String formatTextDate(String dateStringVar){
        
        String dateString = "";

        // define input date format
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        // define output date format
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try {
            // parse the string into a temporary date
            Date tempDate = inputDateFormat.parse(dateStringVar);

            // convert the date into sql readable date
            dateString = outputDateFormat.format(tempDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }
}
