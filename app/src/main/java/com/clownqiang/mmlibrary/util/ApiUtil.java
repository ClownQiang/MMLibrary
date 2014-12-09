package com.clownqiang.mmlibrary.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Created by clownqiang on 14/11/30.
 */
public class ApiUtil {

    public static final String HOST = "http://121.40.83.163:1025";
    public static AsyncHttpClient client = new AsyncHttpClient();


    public static final String LOGIN_IN = HOST + File.separator + "login";
    public static final String BOOK_LIST = HOST + File.separator + "borrowed_books";
    public static final String REBORROW_BOOK = HOST + File.separator + "renew_book";

    /**
     * @param url
     * @param params
     * @param handler
     */
    public static void get(String url, ApiParams params, TextHttpResponseHandler handler) {
        client.get(url, params, handler);
    }

    /**
     * @param url
     * @param params
     * @param handler
     */
    public static void post(String url, ApiParams params, TextHttpResponseHandler handler) {
        client.post(url, params, handler);
    }

    public static String getStringFromByte(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "UTF-8");
    }

}
