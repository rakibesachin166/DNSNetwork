package com.sacdev.avnishstatus;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sacdev.avnishstatus.model.Historymodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HistoryFragment extends Fragment {

    ComplaintAdapter adapter;
    RecyclerView recyclerView;
   ProgressDialog pd;
  ArrayList<Historymodel> historymodels ;
  TextView nullHistory;
    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_history, container, false);
         recyclerView = view.findViewById(R.id.statusrecyclerview_id);
         nullHistory = view.findViewById(R.id.nullHistoryText);
        historymodels = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AsyncTaskHistory asyncTask=new AsyncTaskHistory();
        asyncTask.execute();
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.refreshLayout);

       swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        AsyncTaskHistory asyncTask=new AsyncTaskHistory();
                        asyncTask.execute();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
       if (MainActivity.bottomnav.getSelectedItemId()!=R.id.navProfile){
           MenuItem item = MainActivity.bottomnav.getMenu().findItem(R.id.navProfile);
           item.setChecked(true);
       }

        return view;
    }
    private class AsyncTaskHistory extends AsyncTask<Void,Void,Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getContext());
            pd.setMessage("Loading....");
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {


            RequestBody body = RequestBody.create(constants.mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.sncablenetwork.com//complaint-list")
                    .method("POST", body)
                    .addHeader("Authorization", constants.authtoken)
                    .addHeader("Cookie", "ci_session=23558d58bbe76328120b7da37c675dd4c36b230a")
                    .build();

            try {
                Response response = constants.okHttpClient.newCall(request).execute();
                JSONObject jsonObjectr = new JSONObject(response.body().string());
                String status = jsonObjectr.getString("status");
                if (status.equals("200"))
                {
                    if (historymodels!=null){
                        historymodels.clear();
                    }

                     if(jsonObjectr.has("data")){
                         JSONArray jsonArray = jsonObjectr.getJSONArray("data");
                         for (int i = 0; i < jsonArray.length(); i++) {
                             JSONObject json_data = jsonArray.getJSONObject(i);
                             Historymodel model =
                                     new Historymodel(json_data.getString("type"),json_data.getString("description")
                                             ,json_data.getString("status"),json_data.getString("create_date")
                                             ,json_data.getString("create_time"));

                             historymodels.add(model);

                         }
                     }


                }else{
                    constants.toastshow(getActivity(),jsonObjectr.getString("message"));
                }
                Log.d("Login responce ",jsonObjectr.toString());
            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Login responce ",e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (historymodels!=null){
                adapter = new ComplaintAdapter(historymodels , getContext());
                recyclerView.setAdapter(adapter);
                if (nullHistory.getVisibility()==View.VISIBLE){
                      nullHistory.setVisibility(View.GONE);
                }
            }
            else{
                if (nullHistory.getVisibility()==View.GONE){
                    nullHistory.setVisibility(View.VISIBLE);
                }
            }

            pd.hide();
        }

    }

}