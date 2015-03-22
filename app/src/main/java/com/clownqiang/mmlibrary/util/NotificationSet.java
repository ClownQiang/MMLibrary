package com.clownqiang.mmlibrary.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.clownqiang.mmlibrary.AlarmReceiver;
import com.clownqiang.mmlibrary.model.BookMessage;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by clownqiang on 14/12/7.
 */
public class NotificationSet {

    private static final String TAG = "NotificationSet";


    public static void setNotification(Activity activity, BookMessage bookMessage) {
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, AlarmReceiver.class);
        intent.setAction(Constant.MMLIBRARY_ACTION);
        Bundle bundle = new Bundle();
        bundle.putSerializable("book_message", bookMessage);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, Integer.parseInt(bookMessage.getBook_id()), intent, 0);

        Calendar calendar = Calendar.getInstance();
        /*
        * 将获取的还书时间减去现在的时间，得到的为毫秒，利用这个可以实现还书提醒
        * */
        try {
            int day = CustomeUtil.getLeftDay(bookMessage.getReturn_date());
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, (day-UserData.getEarlyDay())*3600*60);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            //Toast.makeText(activity, "提示" + day + "秒后开启", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void cancleNotification(Activity activity, BookMessage bookMessage) {
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, AlarmReceiver.class);
        intent.setAction(Constant.MMLIBRARY_ACTION);
        Bundle bundle = new Bundle();
        bundle.putSerializable("book_message", bookMessage);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, Integer.parseInt(bookMessage.getBook_id()), intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}
