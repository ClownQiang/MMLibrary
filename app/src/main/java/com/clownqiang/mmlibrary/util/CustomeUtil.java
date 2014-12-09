package com.clownqiang.mmlibrary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by clownqiang on 14/12/5.
 */
public class CustomeUtil {

    public static long getCutTime(String returnTime, String nowTime) throws ParseException {
        Date returnDate = new SimpleDateFormat("yyyy-MM-dd").parse(returnTime);
        Date nowDate = new SimpleDateFormat("yyyy-MM-dd").parse(nowTime);
        long cutTime = returnDate.getTime() - nowDate.getTime();
        return cutTime;
    }

    public static int getLeftDay(String returnTime) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Date nowDate = calendar.getTime();
        Date returnDate = new SimpleDateFormat("yyyy-MM-dd").parse(returnTime);
        long cutTime = returnDate.getTime() - nowDate.getTime();
        int leftDay = (int) (cutTime / (24 * 60 * 60 * 1000));
        return leftDay;
    }
}
