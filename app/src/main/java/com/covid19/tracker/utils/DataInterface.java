package com.covid19.tracker.utils;

import com.covid19.tracker.response.StateResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataInterface {

    @GET("data.json")
    Call<StateResponse> getStatesName();
}
