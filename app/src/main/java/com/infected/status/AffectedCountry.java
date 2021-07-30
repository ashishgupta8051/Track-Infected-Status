package com.infected.status;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.infected.status.adapter.AffectedCountryAdapter;
import com.infected.status.apiclient.MySingletonClass;
import com.infected.status.model.CountryNameModel;
import com.infected.status.utils.InternetCheckService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AffectedCountry extends AppCompatActivity {
    private String Value;
    private ProgressBar simpleArcLoader;
    private RecyclerView recyclerView;
    public static ArrayList<CountryNameModel> countryNameModelArrayList = new ArrayList<>();
    private AffectedCountryAdapter affectedCountryAdapter;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_country);

        broadcastReceiver = new InternetCheckService();

        getSupportActionBar().setTitle("Affected Country");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Value = getIntent().getExtras().get("Value").toString();

        recyclerView = findViewById(R.id.affectedCountryRecycler);
        simpleArcLoader = findViewById(R.id.loader2);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AffectedCountry.this);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchData();
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
                affectedCountryAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                affectedCountryAdapter.getFilter().filter(newText);
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

    private void fetchData() {
        String url = "https://disease.sh/v3/covid-19/countries/";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    countryNameModelArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("countryInfo");

                        String flag,country,cases,todayCases,deaths,todayDeaths,recovered,todayRecovered,active,critical,tests,population;

                        flag = jsonObject1.getString("flag");
                        country = jsonObject.getString("country");
                        cases = jsonObject.getString("cases");
                        todayCases = jsonObject.getString("todayCases");
                        deaths = jsonObject.getString("deaths");
                        todayDeaths = jsonObject.getString("todayDeaths");
                        recovered = jsonObject.getString("recovered");
                        todayRecovered = jsonObject.getString("todayRecovered");
                        active = jsonObject.getString("active");
                        critical = jsonObject.getString("critical");
                        tests = jsonObject.getString("tests");
                        population = jsonObject.getString("population");

                        CountryNameModel countryNameModel = new CountryNameModel(flag,country,cases,todayCases,deaths,todayDeaths,recovered,todayRecovered,active,critical,tests,population);
                        countryNameModelArrayList.add(countryNameModel);
                    }
                    affectedCountryAdapter = new AffectedCountryAdapter(countryNameModelArrayList,AffectedCountry.this,Value);
                    affectedCountryAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(affectedCountryAdapter);
                    simpleArcLoader.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    simpleArcLoader.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AffectedCountry.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                simpleArcLoader.setVisibility(View.GONE);
            }
        });

        MySingletonClass.getInstance(AffectedCountry.this).addToRequestQueue(jsonArrayRequest);
    }
}