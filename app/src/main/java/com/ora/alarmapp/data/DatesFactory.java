package com.ora.alarmapp.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ora.alarmapp.util.Constants.DEFAULT_SERVER_DATE_FORMAT;
import static com.ora.alarmapp.util.Constants.MAIN_DATE_FORMAT_PATTERN;

public class DatesFactory {
    private static SimpleDateFormat serverFormat = new SimpleDateFormat(DEFAULT_SERVER_DATE_FORMAT);
    private static SimpleDateFormat oldLocalFormat = new SimpleDateFormat(MAIN_DATE_FORMAT_PATTERN);

    public static Date getDateFormat(String inputDate){
        inputDate = convertDateString(inputDate);

        if(inputDate == null)
            return null;

        try {
            return serverFormat.parse(inputDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getStringFormat(Date inputDate){
        return serverFormat.format(inputDate);
    }

    public static String getStringFormat(String inputDate){
        return convertDateString(inputDate);
    }

    public static String getStringFormat(Long inputDate){
        return new SimpleDateFormat(MAIN_DATE_FORMAT_PATTERN).format(new Date(inputDate));
        //return serverFormat.format(inputDate);
    }

    private static String convertDateString(String inputDate){
        if(isThisDateValid(inputDate, DEFAULT_SERVER_DATE_FORMAT)){
            return inputDate;
        }else if(isThisDateValid(inputDate, MAIN_DATE_FORMAT_PATTERN)){
            //convert date to new format
            try {
                //read date from old format
                Date date = oldLocalFormat.parse(inputDate);
                //convert date to new format
                return serverFormat.format(date);
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }

    private static boolean isThisDateValid(String dateToValidate, String dateFormatPattern){
        if(dateToValidate == null){
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        //dateFormat.setLenient(false);
        try {
            dateFormat.parse(dateToValidate);
        }catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
