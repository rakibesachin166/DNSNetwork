package com.sacdev.avnishstatus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    Spinner spinner;
    EditText descriptionedt;
    TextView submitbtn, name_txt;
    ProgressDialog progressDialog;
    ImageView logout;
    JSONObject obj;

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        spinner = view.findViewById(R.id.spinner_complainttype_id);
        descriptionedt = view.findViewById(R.id.descriptionedttext_id);
        submitbtn = view.findViewById(R.id.buttonsubmitcomplaint_id);
        logout = view.findViewById(R.id.logoutbtn);
        name_txt = view.findViewById(R.id.nametextid);

        try {
            JSONObject jsonObject = new JSONObject(constants.preferences.getString("customer_details", null));
            name_txt.setText(String.format("Hi, %s", jsonObject.getString("name")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callsubmitapi();
            }
        });
       logout.setOnClickListener(new View.OnClickListener() {
           @SuppressLint("CommitPrefEdits")
           @Override
           public void onClick(View view) {
               constants.preferences.edit().clear().commit();
               getContext().startActivity(constants.changeActivity(getContext(),LoginActivity.class));
           }
       });
        //selected item will look like a spinner set from XML
        ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.complaintType));
        spinner.setAdapter(spinnerCountShoesArrayAdapter);
        return view;
    }

    private void callsubmitapi() {
        if (TextUtils.isEmpty(descriptionedt.getText())) {
            descriptionedt.setError("Enter Description");
            descriptionedt.requestFocus();
            return;
        }
        obj = new JSONObject();
        try {
            obj.put("type", spinner.getSelectedItem().toString());
            obj.put("description", descriptionedt.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error in  Data Processing", Toast.LENGTH_SHORT).show();
            return;
        }
        AsyncTaskHome asyncTask = new AsyncTaskHome();
        asyncTask.execute();

    }

    private class AsyncTaskHome extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Submitting....");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            RequestBody body = RequestBody.create(obj.toString(), constants.mediaType);
            Request request = new Request.Builder()
                    .url("https://api.sncablenetwork.com//complaint-create")
                    .method("POST", body)
                    .addHeader("Authorization", constants.authtoken)
                    .addHeader("Content-Type", "application/json")
                    .build();
            try {
                Response response = constants.okHttpClient.newCall(request).execute();
                JSONObject jsonObjectr = new JSONObject(response.body().string());
                Log.d("Login responce ", jsonObjectr.toString());
                String message = jsonObjectr.getString("message");
                String status = jsonObjectr.getString("status");

                if (status.equals("200")) {
                    constants.toastshow(getActivity(), "Please Try After 24 hours for Next Complaint");
                }else if(status.equals("201")){
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.framentholder_id, new HistoryFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
                constants.toastshow(getActivity(), message);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressDialog.hide();
            descriptionedt.setText(null);


        }
    }

}