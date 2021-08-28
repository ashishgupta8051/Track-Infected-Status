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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infected.status.adapter.AffectedStateAdapter;
import com.infected.status.apiclient.MySingletonClass;
import com.infected.status.model.StateName;
import com.infected.status.utils.Constants;
import com.infected.status.utils.InternetCheckService;
import com.infected.status.utils.StateClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;

public class AffectedStates extends AppCompatActivity implements StateClickListener {
    private ProgressBar simpleArcLoader;
    private RecyclerView recyclerView;
    private final ArrayList<StateName> arrayList = new ArrayList<>();
    private AffectedStateAdapter affectedStateAdapter;
    private BroadcastReceiver broadcastReceiver;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_state);

        setUpUi();
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
        startActivity(intent);
    }

    @Override
    public void onClickListener(StateName stateName) {
        Intent intent = new Intent(getApplicationContext(), StateCovidInformation.class);
        intent.putExtra("state",stateName.getState());
        intent.putExtra("totalCases",stateName.getConfirmed());
        intent.putExtra("active",stateName.getActive());
        intent.putExtra("death",stateName.getDeaths());
        intent.putExtra("recovered",stateName.getRecovered());
        intent.putExtra("time",stateName.getLastupdatedtime());
        startActivity(intent);
    }

    private void setUpUi() {
        broadcastReceiver = new InternetCheckService();

        Objects.requireNonNull(getSupportActionBar()).setTitle("States");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        simpleArcLoader = findViewById(R.id.loader3);
        recyclerView = findViewById(R.id.indiaStateRec);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        getDataUsingVolley();
    }

    private void getDataUsingVolley() {
        @SuppressLint("NotifyDataSetChanged")
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.STATEWISE, null, response -> {
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
                affectedStateAdapter = new AffectedStateAdapter(arrayList,AffectedStates.this,this);
                recyclerView.setAdapter(affectedStateAdapter);
                affectedStateAdapter.notifyDataSetChanged();
                simpleArcLoader.setVisibility(GONE);
            } catch (JSONException e) {
                simpleArcLoader.setVisibility(GONE);
                e.printStackTrace();
            }
        }, error -> simpleArcLoader.setVisibility(GONE));

        MySingletonClass.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}