package com.infected.status.response;

import com.google.gson.annotations.SerializedName;
import com.infected.status.model.StateName;

import java.util.List;

public class StateWiseResponse {

    @SerializedName("statewise")
    private List<StateName> stateNames;

    public StateWiseResponse(List<StateName> stateNames) {
        this.stateNames = stateNames;
    }

    public List<StateName> getStateNames() {
        return stateNames;
    }

    public void setStateNames(List<StateName> stateNames) {
        this.stateNames = stateNames;
    }
}
