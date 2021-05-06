package com.covidtracker.status;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class StateCovidInformation extends AppCompatActivity {
    private TextView totalCasesTxt,state,activeCasesTxt,death,recovered,lastUpdateTime;
    private PieChart pieChart;
    private String stateName,totalCases,activeCases,deaths,recovere,updatedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_covid_information);

        getSupportActionBar().setTitle("India Covid Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        state = findViewById(R.id.tvState);
        totalCasesTxt = findViewById(R.id.tvTotalCases);
        activeCasesTxt = findViewById(R.id.tvActiveCases);
        death = findViewById(R.id.tv_Death);
        recovered = findViewById(R.id.recovered);
        lastUpdateTime = findViewById(R.id.tvUpdateTime);

        pieChart = findViewById(R.id.indiaPieChart);

        stateName = getIntent().getExtras().get("state").toString();
        totalCases = getIntent().getExtras().get("totalCases").toString();
        activeCases = getIntent().getExtras().get("active").toString();
        deaths = getIntent().getExtras().get("death").toString();
        recovere = getIntent().getExtras().get("recovered").toString();
        updatedTime = getIntent().getExtras().get("time").toString();

        state.setText(stateName);
        totalCasesTxt.setText(totalCases);
        activeCasesTxt.setText(activeCases);
        death.setText(deaths);
        recovered.setText(recovere);
        lastUpdateTime.setText(updatedTime);

        pieChart.addPieSlice(new PieModel("Cases",Integer.parseInt(totalCasesTxt.getText().toString()), Color.parseColor("#FFFF00")));
        pieChart.addPieSlice(new PieModel("Recovered",Integer.parseInt(recovered.getText().toString()), Color.parseColor("#008000")));
        pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(death.getText().toString()), Color.parseColor("#FF0000")));
        pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(activeCasesTxt.getText().toString()), Color.parseColor("#87CEEB")));
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
        Intent intent = new Intent(getApplicationContext(),AffectedStates.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}