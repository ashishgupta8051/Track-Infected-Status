package com.covidtracker.status;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.covidtracker.status.adapter.AffectedStateAdapter;
import com.covidtracker.status.apiclient.APIClient;
import com.covidtracker.status.response.StateResponse;
import com.covidtracker.status.model.StateName;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class AffectedStates extends AppCompatActivity {
    private SimpleArcLoader simpleArcLoader;
    private RecyclerView recyclerView;
    private ArrayList<StateName> arrayList;
    private AffectedStateAdapter affectedStateAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_state);

        getSupportActionBar().setTitle("States");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        simpleArcLoader = findViewById(R.id.loader3);
        recyclerView = findViewById(R.id.indiaStateRec);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //fetchDate();
        getData();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getData() {
        simpleArcLoader.start();
       /* DataInterface dataInterface = null;
        dataInterface = APIClient.getApiClient().create(DataInterface.class);
        Call<StateResponse> call = dataInterface.getStatesName();*/

        Call<StateResponse> call = APIClient.getApiClient().getDataInterface().getStatesName();

        call.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(GONE);
                StateResponse stateResponse = response.body();
                arrayList = new ArrayList<>(Arrays.asList(stateResponse.getStatewise()));
                affectedStateAdapter = new AffectedStateAdapter(arrayList,AffectedStates.this);
                recyclerView.setAdapter(affectedStateAdapter);
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(GONE);
                Toast.makeText(AffectedStates.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void fetchDate() {
        String URL = "https://api.covid19india.org/data.json";
        simpleArcLoader.start();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                    arrayList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray statewise = jsonObject.getJSONArray("statewise");
                    for (int i = 0; i < statewise.length(); i++){
                        JSONObject jsonObject2 = statewise.getJSONObject(i);
                        String state;
                        state = jsonObject2.getString("state");

                        StateName stateName = new StateName(state);
                        arrayList.add(stateName);
                    }
                    affectedStateAdapter = new AffectedStateAdapter(arrayList, AffectedStates.this);
                    recyclerView.setAdapter(affectedStateAdapter);
                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);
                } catch (JSONException e) {
                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                Toast.makeText(AffectedStates.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/

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
}