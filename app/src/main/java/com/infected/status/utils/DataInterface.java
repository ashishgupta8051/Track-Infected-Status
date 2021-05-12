package com.infected.status.utils;

import com.infected.status.response.StateResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataInterface {

    @GET("data.json")
    Call<StateResponse> getStatesName();
}
