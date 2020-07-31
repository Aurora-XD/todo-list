package com.thoughtworks.todo_list.repository.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {

    public static Date stringToDate(String value) {
        SimpleDateFormat format =   new SimpleDateFormat( "yyyy年MM月dd日" );
        Date date = null;
        try {
            date = format.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
