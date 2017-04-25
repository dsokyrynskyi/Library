package com.softserveinc.dsoky.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatValidator {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static boolean checkDateFormat(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        try{
            LocalDate.parse(date, formatter);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
