package com.infected.status;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.infected.status.apiclient.MySingletonClass;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private TextView updatedTxt,casesTxt,todayCaseTxt,deathTxt,todayDeathTxt,recoverTxt,
            todayRecoverTxt,activeTxt,criticalTxt,testsTxt,populationTxt,affectedCountryTxt;
    private ProgressBar simpleArcLoader;
    private PieChart pieChart;
    private Button tractCountryBtn,indiaStatusBtn;
    private LinearLayout linearLayout;
    private InterstitialAd mInterstitialAd;
    private static final String AD_UNIT_ID = "ca-app-pub-7419751706380309/8917314068";

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

        //Initialize MobileAds
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadAd();
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,AD_UNIT_ID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;

                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(MainActivity.this);
                        } else {
                            Log.d("TAG", "The interstitial ad wasn't ready yet.");
                        }

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;

                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        Toast.makeText(
                                MainActivity.this, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT)
                                .show();
                    }
                });

    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}