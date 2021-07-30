package com.infected.status;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infected.status.apiclient.MySingletonClass;
import com.infected.status.utils.InternetCheckService;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private TextView updatedTxt,casesTxt,todayCaseTxt,deathTxt,todayDeathTxt,recoverTxt,
            todayRecoverTxt,activeTxt,criticalTxt,testsTxt,populationTxt,affectedCountryTxt;
    private ProgressBar simpleArcLoader;
    private PieChart pieChart;
    private Button tractCountryBtn,indiaStatusBtn;
    private LinearLayout linearLayout;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private NetworkRequest networkRequest;
    private AlertDialog alertDialog;
    private Button retryBtn;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Check Internet Connection
        checkInternetConnection(this);

        //fetch Data
        fetchData();

        //Create Dialog Box
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.internet_alert_dialog,null);
        builder.setView(view);

        retryBtn = view.findViewById(R.id.retry_btn);

        alertDialog = builder.setCancelable(false).create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Toast;
    }

    private void fetchData() {
        //Fetch Data
        String url = "https://corona.lmao.ninja/v2/all/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
                    linearLayout.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    simpleArcLoader.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MySingletonClass.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);

    }

    private void checkInternetConnection(final Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        networkCallback = new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                //alertDialog.dismiss();
                Log.e(TAG,"Connected "+network.toString());
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                //alertDialog.show();
                Log.e(TAG,"Not Connected "+network.toString());
                Toast.makeText(context, "disconnect", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                Toast.makeText(context, "no network found", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectivityManager.registerNetworkCallback(networkRequest,networkCallback);
        /*IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}