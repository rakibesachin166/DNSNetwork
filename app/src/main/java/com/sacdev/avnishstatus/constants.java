package com.sacdev.avnishstatus;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class constants {
    public static String authtoken;
    public static OkHttpClient okHttpClient= new OkHttpClient().newBuilder().build(); ;
    public  static MediaType mediaType =MediaType.parse("application/json");
   public static SharedPreferences preferences;
    public static Intent changeActivity(Context a , Class b)
    {
        Intent intent = new Intent(a,b);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
    public static void toastshow(Activity activity , String s){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, ""+s, Toast.LENGTH_SHORT).show();
            }
        });

    }



}
