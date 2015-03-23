package com.clownqiang.mmlibrary.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.clownqiang.mmlibrary.MyApp;
import com.clownqiang.mmlibrary.R;
import com.clownqiang.mmlibrary.adapter.TransitionAdapter;
import com.clownqiang.mmlibrary.model.BaseJson;
import com.clownqiang.mmlibrary.model.BookMessage;
import com.clownqiang.mmlibrary.util.ApiParams;
import com.clownqiang.mmlibrary.util.ApiUtil;
import com.clownqiang.mmlibrary.util.CustomeUtil;
import com.clownqiang.mmlibrary.util.Handler.BaseTextHttpResponseHandler;
import com.clownqiang.mmlibrary.util.JsonUtil;
import com.clownqiang.mmlibrary.util.UserData;
import com.clownqiang.mmlibrary.util.WidgetInit;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BookDetailActivity extends Activity {

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.loading_failed)
            .showImageForEmptyUri(R.drawable.loading_failed)
            .showStubImage(R.drawable.loading)
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .cacheInMemory(true)
            .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    private static final String TAG = "BookDetailActivity";
    String leftTime;

    BookMessage bookMessage;

    ProgressDialog progressDialog;

    @InjectView(R.id.bookPage)
    ImageView bookPage;
    @InjectView(R.id.leftTime)
    TextView leftTimeTextView;
    @InjectView(R.id.bookName)
    TextView bookNameTextView;
    @InjectView(R.id.autherName)
    TextView autherNameTextView;
    @InjectView(R.id.bookDescription)
    TextView bookDescriptionTextView;
    @InjectView(R.id.borrowedTime)
    TextView borrowedTimeTextView;
    @InjectView(R.id.returnTime)
    TextView returnTimeTextView;
    @InjectView(R.id.bookLocation)
    TextView bookLocationTextView;
    @InjectView(R.id.renewButton)
    Button renewButton;

    @OnClick(R.id.renewButton)
    void renewBook() {
        ApiUtil.get(ApiUtil.REBORROW_BOOK, new ApiParams().withCookie().with("book_id", bookMessage.getBook_id()), new BaseTextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onSuccessDo(BaseJson baseJson, String body) {
                String renewResult = null;
                if (!baseJson.getCode().equals("200")) {
                    Toast.makeText(BookDetailActivity.this, getResources().getString(R.string.renew_fail), Toast.LENGTH_SHORT).show();
                }
                try {
                    renewResult = JsonUtil.getString(new JSONObject(body), "renew_result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(BookDetailActivity.this, renewResult, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.inject(this);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        getIntentText();
        try {
            setText();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        progressDialog = WidgetInit.initProgressDialog(progressDialog, BookDetailActivity.this, "");

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getEnterTransition().addListener(new TransitionAdapter() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onTransitionEnd(Transition transition) {
                    ObjectAnimator color = ObjectAnimator.ofArgb(bookPage.getDrawable(), "tint",
                            getResources().getColor(R.color.photo_tint), 0);
                    color.start();
                    renewButton.animate().scaleX(1.0f);
                    renewButton.animate().scaleY(1.0f);
                    renewButton.animate().alpha(1.0f);
                    getWindow().getEnterTransition().removeListener(this);
                }
            });
        }
    }

    private void getIntentText() {
        Intent intent = getIntent();
        bookMessage = (BookMessage) intent.getSerializableExtra("book_message");
        try {
            leftTime = String.valueOf(CustomeUtil.getLeftDay(bookMessage.getReturn_date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setText() throws ParseException {
        if (CustomeUtil.getLeftDay(bookMessage.getReturn_date()) < UserData.getEarlyDay()) {
            leftTimeTextView.setTextColor(MyApp.getContext().getResources().getColor(android.R.color.holo_red_dark));
            returnTimeTextView.setTextColor(MyApp.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }
        leftTimeTextView.setText(leftTime);
        bookNameTextView.setText(bookMessage.getTitle());
        autherNameTextView.setText(bookMessage.getAuthor());
        bookDescriptionTextView.setText(bookMessage.getDescription());
        bookLocationTextView.setText(bookMessage.getLib_location());
        borrowedTimeTextView.setText(getResources().getString(R.string.borrowed_time) + bookMessage.getBorrowed_date());
        returnTimeTextView.setText(getResources().getString(R.string.return_time) + bookMessage.getReturn_date());

        imageLoader.displayImage(bookMessage.getImg_url(),bookPage,options);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= 21) {
            ObjectAnimator color = ObjectAnimator.ofArgb(bookPage.getDrawable(), "tint",
                    0, getResources().getColor(R.color.photo_tint));
            color.addListener(new AnimatorListenerAdapter() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onAnimationEnd(Animator animation) {
                    finishAfterTransition();
                }
            });
            color.start();
            renewButton.animate().scaleX(0.0f);
            renewButton.animate().scaleY(0.0f);
            renewButton.animate().alpha(0.0f);
            finishAfterTransition();
        }
    }
}
