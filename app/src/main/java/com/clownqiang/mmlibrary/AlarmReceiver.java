package com.clownqiang.mmlibrary;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.clownqiang.mmlibrary.activity.BookDetailActivity;
import com.clownqiang.mmlibrary.model.BookMessage;
import com.clownqiang.mmlibrary.util.Constant;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    NotificationManager notificationManager;
    Notification notification;
    BookMessage bookMessage;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        if (action.equals(Constant.MMLIBRARY_ACTION)) {
            bookMessage = (BookMessage) intent.getSerializableExtra("book_message");
            String bookName = bookMessage.getTitle();
            String return_time = bookMessage.getReturn_date();
            int book_id = Integer.parseInt(bookMessage.getBook_id());
            /*
            * book_id 对应了每一个notification
            * */
            Log.d(TAG, bookName);
            Log.d(TAG, return_time);
            Log.d(TAG, String.valueOf(book_id));
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notification = new Notification(R.drawable.ic_notification, "《" + bookName + "》" + "的还书时间要到了", System.currentTimeMillis());
            String title = "何书还提示你";
            String content = "你在南邮图书馆借的图书《" + bookName + "》" + "将于" + return_time + "到期，是否续借或者将书本还掉";

            Intent notificaitonIntent = new Intent(context, BookDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("book_message", bookMessage);
            notificaitonIntent.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, book_id, notificaitonIntent, 0);

            notification.setLatestEventInfo(context, title, content, pendingIntent);
            notification.defaults |= Notification.DEFAULT_SOUND;     //默认的声音
            notification.defaults |= Notification.DEFAULT_VIBRATE;   //默认的震动
            notification.defaults |= Notification.DEFAULT_LIGHTS;    //默认的灯光

            notificationManager.notify(book_id, notification);
        }
    }
}
