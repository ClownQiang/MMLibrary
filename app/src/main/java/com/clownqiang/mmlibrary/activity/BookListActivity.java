package com.clownqiang.mmlibrary.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clownqiang.mmlibrary.R;
import com.clownqiang.mmlibrary.adapter.BookListAdapter;
import com.clownqiang.mmlibrary.model.BaseJson;
import com.clownqiang.mmlibrary.model.BookMessage;
import com.clownqiang.mmlibrary.util.ApiParams;
import com.clownqiang.mmlibrary.util.ApiUtil;
import com.clownqiang.mmlibrary.util.Constant;
import com.clownqiang.mmlibrary.util.Handler.BaseTextHttpResponseHandler;
import com.clownqiang.mmlibrary.util.NotificationSet;
import com.clownqiang.mmlibrary.util.UserData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class BookListActivity extends Activity {

    private final static String TAG = "MainActivity";

    Gson gson = new Gson();
    BookListAdapter bookListAdapter;
    private List<BookMessage> bookMessageList = new ArrayList<BookMessage>();
    private Type bookMessageType = new TypeToken<List<BookMessage>>() {
    }.getType();

    NumberPicker numberPicker;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;
    @InjectView(R.id.isEmpty)
    TextView isEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklist);
        ButterKnife.inject(this);

        getBookList();

        progressBar.setVisibility(View.INVISIBLE);

        bookListAdapter = new BookListAdapter(bookMessageList, BookListActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bookListAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_booklist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.early_day:
                View dialogView = LayoutInflater.from(BookListActivity.this).inflate(R.layout.dialog_numpicker, null);
                numberPicker = (NumberPicker) dialogView.findViewById(R.id.numberPicker);
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(10);
                numberPicker.setValue(UserData.getEarlyDay());
                Dialog dialog = new AlertDialog.Builder(BookListActivity.this)
                        .setTitle(getResources().getString(R.string.dialog_title))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserData.saveEarlyDay(numberPicker.getValue());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setView(dialogView).create();
                dialog.show();
                return true;
            case R.id.exit:
                toLoginIn();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getBookList() {
        ApiUtil.get(ApiUtil.BOOK_LIST, new ApiParams().withCookie(), new BaseTextHttpResponseHandler(progressBar,BookListActivity.this) {

            @Override
            public void onStart() {
                super.onStart();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccessDo(BaseJson baseJson, String body) {
                if (baseJson.getCode().equals(Constant.FAIL_LOGIN)) {
                    Toast.makeText(BookListActivity.this, getResources().getString(R.string.cookie_fail),
                            Toast.LENGTH_SHORT).show();

                    toLoginIn();
                    return;
                }
                if (body == null) {
                    isEmptyTextView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    isEmptyTextView.setVisibility(View.GONE);
                }
                if (!UserData.getBookMessage().isEmpty()) {
                    List<BookMessage> canclebookMessages = gson.fromJson(UserData.getBookMessage(), bookMessageType);
                    for (BookMessage bookMessage : canclebookMessages)
                        NotificationSet.cancleNotification(BookListActivity.this, bookMessage);
                }
                UserData.saveBookMessage(body);
                List<BookMessage> bookMessages = gson.fromJson(body, bookMessageType);
                for (BookMessage bookMessage : bookMessages) {
                    bookMessageList.add(bookMessage);
                    NotificationSet.setNotification(BookListActivity.this, bookMessage);
                }
                bookListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void toLoginIn() {
        UserData.saveUserCookie("");
        Intent intent = new Intent(BookListActivity.this, LoginActivity.class);
        startActivity(intent);
        BookListActivity.this.finish();
    }
}
