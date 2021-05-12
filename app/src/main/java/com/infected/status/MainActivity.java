package com.infected.status;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView updatedTxt,casesTxt,todayCaseTxt,deathTxt,todayDeathTxt,recoverTxt,todayRecoverTxt,activeTxt,criticalTxt,testsTxt,populationTxt,affectedCountryTxt;
    private ProgressBar simpleArcLoader;
    private PieChart pieChart;
    private Button tractCountryBtn,indiaStatusBtn;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        simpleArcLoader = findViewById(R.id.loader);

        pieChart = findViewById(R.id.piechart);

        tractCountryBtn = findViewById(R.id.btnTrack);
        indiaStatusBtn = findViewById(R.id.btnIndiaStatus);

        linearLayout = findViewById(R.id.linearLayout);

        tractCountryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AffectedCountry.class);
                //intent.putExtra("Value","M");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
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

        fetchData();
    }

    private void fetchData() {
        String url = "https://corona.lmao.ninja/v2/all/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    updatedTxt.setText(jsonObject.getString("updated"));
                    casesTxt.setText(jsonObject.getString("cases"));
                    todayCaseTxt.setText(jsonObject.getString("todayCases"));
                    deathTxt.setText(jsonObject.getString("deaths"));
                    todayDeathTxt.setText(jsonObject.getString("todayDeaths"));
                    recoverTxt.setText(jsonObject.getString("recovered"));
                    todayRecoverTxt.setText(jsonObject.getString("todayRecovered"));
                    activeTxt.setText(jsonObject.getString("active"));
                    criticalTxt.setText(jsonObject.getString("critical"));
                    testsTxt.setText(jsonObject.getString("tests"));
                    populationTxt.setText(jsonObject.getString("population"));
                    affectedCountryTxt.setText(jsonObject.getString("affectedCountries"));

                    pieChart.addPieSlice(new PieModel("Cases",Integer.parseInt(casesTxt.getText().toString()), Color.parseColor("#FFFF00")));
                    pieChart.addPieSlice(new PieModel("Recovered",Integer.parseInt(recoverTxt.getText().toString()), Color.parseColor("#008000")));
                    pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(deathTxt.getText().toString()), Color.parseColor("#FF0000")));
                    pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(activeTxt.getText().toString()), Color.parseColor("#87CEEB")));
                    pieChart.startAnimation();

                    simpleArcLoader.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    simpleArcLoader.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}