package com.clownqiang.mmlibrary.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clownqiang.mmlibrary.R;
import com.clownqiang.mmlibrary.model.BaseJson;
import com.clownqiang.mmlibrary.util.ApiParams;
import com.clownqiang.mmlibrary.util.ApiUtil;
import com.clownqiang.mmlibrary.util.Constant;
import com.clownqiang.mmlibrary.util.Handler.BaseTextHttpResponseHandler;
import com.clownqiang.mmlibrary.util.JsonUtil;
import com.clownqiang.mmlibrary.util.UserData;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    private final static String TAG = "LoginActivity";

    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.loginButton)
    Button loginButton;
    @InjectView(R.id.userName)
    MaterialEditText userNameEditText;
    @InjectView(R.id.passWord)
    MaterialEditText passWordEditText;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;

    @OnClick(R.id.loginButton)
    void loginIn() {
        final String userName = userNameEditText.getText().toString().trim();
        final String passWord = passWordEditText.getText().toString().trim();
        if (userName.isEmpty() || passWord.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.input_is_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        ApiUtil.get(ApiUtil.LOGIN_IN, new ApiParams().with("user_number", userName).with("user_passwd", passWord), new BaseTextHttpResponseHandler(progressBar) {


            @Override
            public void onSuccessDo(BaseJson baseJson, String body) {
                String cookie = null;
                if (baseJson.getCode().equals(Constant.FAIL_LOGIN)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.fail_login),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    cookie = JsonUtil.getString(new JSONObject(body), "cookie");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UserData.saveUserCookie(cookie);
                UserData.saveUserLoginName(userName);
                UserData.saveUserPassWord(passWord);
                Intent intent = new Intent(LoginActivity.this, BookListActivity.class);

                if (Build.VERSION.SDK_INT == 21) {
                    startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                } else {
                    startActivity(intent);
                }

                LoginActivity.this.finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(LoginActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        if (!UserData.getUserCookie().isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, BookListActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }

        ButterKnife.inject(this);

        progressBar.setVisibility(View.INVISIBLE);
        textView.setHeight(getTextViewHeight());
        userNameEditText.setText(UserData.getUserLoginName());
        passWordEditText.setText(UserData.getUserPassWord());
    }

    /**
     * 设置宽度为黄金分割宽度0.618
     */
    private int getTextViewHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;// 宽度
        return (int) (height * (1 - Constant.CONSTANT));
    }
}
