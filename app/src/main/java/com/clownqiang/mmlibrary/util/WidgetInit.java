package com.clownqiang.mmlibrary.util;

import android.app.Activity;
import android.app.ProgressDialog;

import com.clownqiang.mmlibrary.MyApp;
import com.clownqiang.mmlibrary.R;

/**
 * Created by clownqiang on 14/11/30.
 */
public class WidgetInit {

    //ProgressDialog初始化
    public static ProgressDialog initProgressDialog(ProgressDialog progressDialog,Activity activity,String message) {
        progressDialog = new ProgressDialog(activity);
        if (message.isEmpty()) {
            progressDialog.setMessage(MyApp.getContext().getResources().getString(R.string.progressdialog_content));
        } else {
            progressDialog.setMessage(message);
        }
        return progressDialog;
    }

}
