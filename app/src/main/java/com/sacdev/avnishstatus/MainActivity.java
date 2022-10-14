package com.sacdev.avnishstatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static BottomNavigationView bottomnav;
    Fragment currentfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomnav = findViewById(R.id.bottomnavigationbar_id);
        if (savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.framentholder_id, new HomeFragment()).commit();
        }
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navProfile:
                        currentfragment = new HistoryFragment();
                        fragmentchanger(currentfragment);
                        break;

                    default:
                        currentfragment = new HomeFragment();
                        fragmentchanger(currentfragment);
                        break;
                }
                return true;

            }
        });
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        setbottomview();
                    }
                });

    }

    private void fragmentchanger(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.framentholder_id, fragment).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    public void setbottomview() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framentholder_id);
        if (f instanceof HomeFragment) {
            MenuItem item = bottomnav.getMenu().findItem(R.id.navMain);
            item.setChecked(true);
        } else if (f instanceof HistoryFragment) {
            MenuItem item = bottomnav.getMenu().findItem(R.id.navProfile);
            item.setChecked(true);
        }

    }


}