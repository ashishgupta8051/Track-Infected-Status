package com.infected.status.response;

import com.google.gson.annotations.SerializedName;
import com.infected.status.model.StateName;

public class StateResponse {

    @SerializedName("statewise")
    private StateName[] stateNames;

    public StateName[] getStateNames() {
        return stateNames;
    }

    public void setStateNames(StateName[] stateNames) {
        this.stateNames = stateNames;
    }
}
