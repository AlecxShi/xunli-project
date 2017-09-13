package com.xunli.manager.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shihj on 2017/9/11.
 */
public class DateUtil {
    private final static DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final static Calendar CALENDAR = Calendar.getInstance();

    public static String getDate(String dateStr,Integer minus)
    {
        String ret = dateStr;
        try
        {
            Date date = FORMAT.parse(dateStr);
            CALENDAR.setTime(date);
            CALENDAR.add(Calendar.YEAR,minus);
            ret = FORMAT.format(CALENDAR.getTime());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return ret;
    }
}