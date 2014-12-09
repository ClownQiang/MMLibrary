package com.clownqiang.mmlibrary.util;

import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by clownqiang on 14/11/24.
 */
public class ApiParams extends RequestParams {

    public ApiParams with(String key, String value) {
        put(key, value);
        return this;
    }

    public ApiParams withFile(String key, File file) {
        try {
            put(key, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ApiParams withCookie(){
        put("cookie",UserData.getUserCookie());
        return this;
    }
}
