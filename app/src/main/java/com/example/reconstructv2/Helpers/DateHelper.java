package com.example.reconstructv2.Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {


    public static String formatTextDate(String dateStringVar){
        String dateString = "";
        String endDateString = dateStringVar;
        SimpleDateFormat startDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try {
            System.out.println("trying to  changed");
            Date tempDate = startDateFormat.parse(endDateString);
            dateString = endDateFormat.format(tempDate);
            System.out.println("date changed");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }
}
