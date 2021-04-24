package com.covid19tracker.status;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class CountryCovidInformation extends AppCompatActivity {
    private String Value,country,cases,todayCases,death,todayDeath,recovered,todayRecovered,active,critical,tests,population;
    private TextView countryTxt,casesTxt,todayCasesTxt,deathTxt,todayDeathTxt,recoveredTxt,todayRecoveredTxt,activeTxt,criticalTxt,testsTxt,populationTxt;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_covid_information);

        getSupportActionBar().setTitle("Covid Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Value = getIntent().getExtras().get("Value").toString();
        country = getIntent().getExtras().get("country").toString();
        cases = getIntent().getExtras().get("cases").toString();
        todayCases = getIntent().getExtras().get("TodayCases").toString();
        death = getIntent().getExtras().get("death").toString();
        todayDeath = getIntent().getExtras().get("TodayDeath").toString();
        recovered = getIntent().getExtras().get("recovered").toString();
        todayRecovered = getIntent().getExtras().get("TodayRecovered").toString();
        active = getIntent().getExtras().get("active").toString();
        critical = getIntent().getExtras().get("critical").toString();
        tests = getIntent().getExtras().get("tests").toString();
        population = getIntent().getExtras().get("population").toString();

        countryTxt = findViewById(R.id.tvCountry);
        casesTxt = findViewById(R.id.tvCases);
        todayCasesTxt = findViewById(R.id.tvTodayCases);
        deathTxt = findViewById(R.id.tvDeath);
        todayDeathTxt = findViewById(R.id.tvTodayDeath);
        recoveredTxt = findViewById(R.id.tvRecovered);
        todayRecoveredTxt = findViewById(R.id.tvTodayRecovered);
        activeTxt = findViewById(R.id.tvActive);
        criticalTxt = findViewById(R.id.tvCritical);
        testsTxt = findViewById(R.id.tvTests);
        populationTxt = findViewById(R.id.tvPopulation);

        pieChart = findViewById(R.id.piechart2);

        countryTxt.setText(country);
        casesTxt.setText(cases);
        todayCasesTxt.setText(todayCases);
        deathTxt.setText(death);
        todayDeathTxt.setText(todayDeath);
        recoveredTxt.setText(recovered);
        todayRecoveredTxt.setText(todayRecovered);
        activeTxt.setText(active);
        testsTxt.setText(tests);
        criticalTxt.setText(critical);
        populationTxt.setText(population);

        pieChart.addPieSlice(new PieModel("Cases",Integer.parseInt(casesTxt.getText().toString()), Color.parseColor("#FFFF00")));
        pieChart.addPieSlice(new PieModel("Recovered",Integer.parseInt(recoveredTxt.getText().toString()), Color.parseColor("#008000")));
        pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(deathTxt.getText().toString()), Color.parseColor("#FF0000")));
        pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(activeTxt.getText().toString()), Color.parseColor("#87CEEB")));
        pieChart.startAnimation();

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
        Intent intent = new Intent(getApplicationContext(), AffectedCountry.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.putExtra("Value",Value);
        startActivity(intent);
        //finish();
    }
}