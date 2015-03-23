package com.clownqiang.mmlibrary.util.Handler;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.clownqiang.mmlibrary.model.BaseJson;
import com.clownqiang.mmlibrary.util.JsonUtil;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by clownqiang on 14/11/30.
 */
public abstract class BaseTextHttpResponseHandler extends TextHttpResponseHandler {

    private static final String TAG = "BaseTextHeepResponseHandler";
    ProgressBar progressBar = null;
    Context context;

    Gson gson = new Gson();

    protected BaseTextHttpResponseHandler(String encoding, ProgressBar progressBar) {
        super(encoding);
        this.progressBar = progressBar;
    }

    protected BaseTextHttpResponseHandler(ProgressBar progressBar,Context context) {
        this.progressBar = progressBar;
        this.context = context;
    }

    protected BaseTextHttpResponseHandler() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (progressBar != null)
            progressBar.setIndeterminate(true);
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (progressBar != null)
            progressBar.setIndeterminate(false);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Toast.makeText(context,"网络连接超时",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        if (responseString.isEmpty()) {
            return;
        }
        BaseJson baseJson = gson.fromJson(responseString, BaseJson.class);
        String body = null;
        try {
            body = JsonUtil.getString(new JSONObject(responseString), "body");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        onSuccessDo(baseJson, body);
    }


    public abstract void onSuccessDo(BaseJson baseJson, String body);
}
