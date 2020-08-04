package com.thoughtworks.todo_list.repository.utils;

import android.util.Log;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class DateTrans {
    public static final String TAG = "Date transform";

    @TypeConverter
    public static Date stringToDate(String value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = null;
        if (!Pattern.matches("\\d{4}.{1}\\d{2}.{1}\\d{2}.{1}",value)) {
            Log.d(TAG, "No date selected by current user!");
            return date;
        } else {
            try {
                date = format.parse(value);
            } catch (ParseException e) {
                Log.d(TAG, "Transform failed!");
                e.printStackTrace();
            }
        }
        return date;
    }

    @TypeConverter
    public static String dataToString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return year + "年" + ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "月" + (day < 10 ? "0" + day : day) + "日";
    }
}
