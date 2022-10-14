package com.sacdev.avnishstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;
import okhttp3.RequestBody;
//import retrofit2.Response;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    EditText mobileedt, passwordedt;
    TextView loginbtn  , developBy;
    JSONObject obj;
    ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mobileedt = findViewById(R.id.usernameedt_id);
        passwordedt = findViewById(R.id.passwordedt_id);
        loginbtn = findViewById(R.id.buttonlogin_id);
        developBy = findViewById(R.id.developedBy_id);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMethode();
            }
        });

        //create your spannable
        final SpannableString spannable = new SpannableString(getString(R.string.developByS));
        final ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(final View view) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://aayatwebtech.com/")));
            }
        };

        spannable.setSpan(clickableSpan, 13, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        developBy.setMovementMethod(LinkMovementMethod.getInstance());
        developBy.setText(spannable);
    }

    private void newMethode() {
        if (TextUtils.isEmpty(mobileedt.getText())) {
            mobileedt.setError("Enter Username");
            mobileedt.requestFocus();
            return;
        } else if (TextUtils.isEmpty(passwordedt.getText())) {
            passwordedt.setError("Enter Password");
            passwordedt.requestFocus();
            return;
        }
        obj = new JSONObject();
        try {
            obj.put("mobile", mobileedt.getText().toString());
            obj.put("password", passwordedt.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            constants.toastshow(LoginActivity.this,e.getMessage());
            return;
        }

        AsyncTaskExample asyncTask = new AsyncTaskExample();
        asyncTask.execute();

    }


    private class AsyncTaskExample extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(LoginActivity.this);
            p.setMessage("Verifying....");
            p.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            RequestBody body = RequestBody.create(obj.toString(), constants.mediaType);
            Request request = new Request.Builder()
                    .url("https://api.sncablenetwork.com/customer-login")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Cookie", "ci_session=354bdf429d7ebf294944eaad948ccb73acdc5ec8")
                    .build();

            try {
                Response response = constants.okHttpClient.newCall(request).execute();
                JSONObject jsonObjectr = new JSONObject(response.body().string());
                Log.d("Login responce ", jsonObjectr.toString());
                String message = jsonObjectr.getString("message");
                constants.toastshow(LoginActivity.this,message);
                String authToken = jsonObjectr.getString("auth_token");
                String customer = jsonObjectr.getString("data");
                if (authToken != null) {
                    SharedPreferences.Editor editor = getSharedPreferences("LoginContent", MODE_PRIVATE).edit();
                    editor.clear();
                    editor.putString("auth_token", authToken);
                    editor.putString("customer_details", customer);
                    editor.putBoolean("logged_in", true);
                    editor.apply();
                    constants.authtoken = authToken;
                   startActivity( constants.changeActivity(LoginActivity.this,MainActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            p.cancel();
        }
    }

}