package com.coronainfected.status;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.coronainfected.status.apiclient.MySingletonClass;
import com.coronainfected.status.utils.Constants;
import com.coronainfected.status.utils.InternetCheckService;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class MainActivity extends AppCompatActivity {
    private TextView updatedTxt,casesTxt,todayCaseTxt,deathTxt,todayDeathTxt,recoverTxt,
            todayRecoverTxt,activeTxt,criticalTxt,testsTxt,populationTxt,affectedCountryTxt;
    private ProgressBar simpleArcLoader;
    private PieChart pieChart;
    private Button tractCountryBtn,indiaStatusBtn;
    private BroadcastReceiver broadcastReceiver;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUI();

        tractCountryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AffectedCountry.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        indiaStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AffectedStates.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
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
    public void onBackPressed() {
        finishAffinity();
    }

    private void setUpUI() {
        broadcastReceiver = new InternetCheckService();

        updatedTxt = findViewById(R.id.updated);
        casesTxt = findViewById(R.id.cases);
        todayCaseTxt = findViewById(R.id.todayCases);
        deathTxt = findViewById(R.id.death);
        todayDeathTxt = findViewById(R.id.todayDeath);
        recoverTxt = findViewById(R.id.recover);
        todayRecoverTxt = findViewById(R.id.todayRecover);
        activeTxt = findViewById(R.id.active);
        criticalTxt = findViewById(R.id.critical);
        testsTxt = findViewById(R.id.tests);
        populationTxt = findViewById(R.id.population);
        affectedCountryTxt = findViewById(R.id.affectedCountry);
        relativeLayout = findViewById(R.id.relativeLayout);

        simpleArcLoader = findViewById(R.id.loader);

        pieChart = findViewById(R.id.piechart);

        tractCountryBtn = findViewById(R.id.btnTrack);
        indiaStatusBtn = findViewById(R.id.btnIndiaStatus);

        //fetch Data
        fetchData();
    }

    private void fetchData() {
        //Fetch Data

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.ALL, null, response -> {
            try {
                updatedTxt.setText(response.getString("updated"));
                casesTxt.setText(response.getString("cases"));
                todayCaseTxt.setText(response.getString("todayCases"));
                deathTxt.setText(response.getString("deaths"));
                todayDeathTxt.setText(response.getString("todayDeaths"));
                recoverTxt.setText(response.getString("recovered"));
                todayRecoverTxt.setText(response.getString("todayRecovered"));
                activeTxt.setText(response.getString("active"));
                criticalTxt.setText(response.getString("critical"));
                testsTxt.setText(response.getString("tests"));
                populationTxt.setText(response.getString("population"));
                affectedCountryTxt.setText(response.getString("affectedCountries"));

                pieChart.addPieSlice(new PieModel("Cases",Integer.parseInt(casesTxt.getText().toString()), Color.parseColor("#FFFF00")));
                pieChart.addPieSlice(new PieModel("Recovered",Integer.parseInt(recoverTxt.getText().toString()), Color.parseColor("#008000")));
                pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(deathTxt.getText().toString()), Color.parseColor("#FF0000")));
                pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(activeTxt.getText().toString()), Color.parseColor("#87CEEB")));
                pieChart.startAnimation();

                simpleArcLoader.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
            }catch (Exception e){
                simpleArcLoader.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }, error -> {
            simpleArcLoader.setVisibility(View.GONE);
        });
        MySingletonClass.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);

    }
}