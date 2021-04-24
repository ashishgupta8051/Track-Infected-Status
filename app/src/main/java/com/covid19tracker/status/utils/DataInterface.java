package com.covid19tracker.status.utils;

import com.covid19tracker.status.response.StateResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataInterface {

    @GET("data.json")
    Call<StateResponse> getStatesName();
}
