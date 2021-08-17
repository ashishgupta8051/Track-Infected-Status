package com.infected.status;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.infected.status.adapter.AffectedStateAdapter;
import com.infected.status.apiclient.APIClient;
import com.infected.status.apiclient.MySingletonClass;
import com.infected.status.response.StateWiseResponse;
import com.infected.status.model.StateName;
import com.infected.status.utils.InternetCheckService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;

public class AffectedStates extends AppCompatActivity {
    private ProgressBar simpleArcLoader;
    private RecyclerView recyclerView;
    private List<StateName> arrayList = new ArrayList<>();
    private AffectedStateAdapter affectedStateAdapter;
    private BroadcastReceiver broadcastReceiver;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_state);

        broadcastReceiver = new InternetCheckService();

        getSupportActionBar().setTitle("States");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        simpleArcLoader = findViewById(R.id.loader3);
        recyclerView = findViewById(R.id.indiaStateRec);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();
        //getDataUsingRetrofit();
        getDataUsingVolley();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_countries,menu);
        MenuItem menuItem = menu.findItem(R.id.searchCountry);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search here !!");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                affectedStateAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                affectedStateAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDataUsingRetrofit() {
        Call<StateWiseResponse> call = APIClient.getApiClient().getDataInterface().getStatesName();
        call.enqueue(new Callback<StateWiseResponse>() {
            @Override
            public void onResponse(Call<StateWiseResponse> call, Response<StateWiseResponse> response) {
                if (response.isSuccessful()){
                    simpleArcLoader.setVisibility(GONE);
                    arrayList = response.body().getStateNames();
                    affectedStateAdapter = new AffectedStateAdapter(arrayList,AffectedStates.this);
                    affectedStateAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(affectedStateAdapter);
                }else {
                    Toast.makeText(AffectedStates.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StateWiseResponse> call, Throwable t) {
                simpleArcLoader.setVisibility(GONE);
                Toast.makeText(AffectedStates.this, "Some thing is wrong !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataUsingVolley() {
        String url = "https://api.covid19india.org/data.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    arrayList.clear();
                    JSONArray jsonArray = response.getJSONArray("statewise");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String active = jsonObject.getString("active");
                        String confirmed = jsonObject.getString("confirmed");
                        String deaths = jsonObject.getString("deaths");
                        String deltaconfirmed = jsonObject.getString("deltaconfirmed");
                        String deltadeaths = jsonObject.getString("deltadeaths");
                        String deltarecovered = jsonObject.getString("deltarecovered");
                        String lastupdatedtime = jsonObject.getString("lastupdatedtime");
                        String migratedother = jsonObject.getString("migratedother");
                        String recovered = jsonObject.getString("recovered");
                        String state = jsonObject.getString("state");
                        String statecode = jsonObject.getString("statecode");
                        String statenotes = jsonObject.getString("statenotes");
                        StateName stateName = new StateName(active,confirmed,deaths,deltaconfirmed,deltadeaths,
                                deltarecovered,lastupdatedtime,migratedother,recovered,state,statecode,
                                statenotes);
                        arrayList.add(stateName);
                    }
                    simpleArcLoader.setVisibility(GONE);
                    affectedStateAdapter = new AffectedStateAdapter(arrayList,AffectedStates.this);
                    affectedStateAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(affectedStateAdapter);
                } catch (JSONException e) {
                    simpleArcLoader.setVisibility(GONE);
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader.setVisibility(GONE);
                //Toast.makeText(AffectedStates.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        MySingletonClass.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}