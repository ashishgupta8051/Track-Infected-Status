package com.infected.status;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.infected.status.adapter.AffectedStateAdapter;
import com.infected.status.apiclient.APIClient;
import com.infected.status.response.StateWiseResponse;
import com.infected.status.model.StateName;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class AffectedStates extends AppCompatActivity {
    private ProgressBar simpleArcLoader;
    private RecyclerView recyclerView;
    private List<StateName> arrayList;
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

        getData();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getData() {
        Call<StateWiseResponse> call = APIClient.getApiClient().getDataInterface().getStatesName();
        call.enqueue(new Callback<StateWiseResponse>() {
            @Override
            public void onResponse(Call<StateWiseResponse> call, Response<StateWiseResponse> response) {
                if (response.isSuccessful()){
                    simpleArcLoader.setVisibility(GONE);
                    arrayList = response.body().getStateNames();
                    affectedStateAdapter = new AffectedStateAdapter(arrayList,AffectedStates.this);
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