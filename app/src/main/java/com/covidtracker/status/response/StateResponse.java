package com.covidtracker.status.response;

import com.covidtracker.status.model.StateName;

public class StateResponse {

    private StateName[] statewise;

    public StateName[] getStatewise() {
        return statewise;
    }

    public void setStatewise(StateName[] statewise) {
        this.statewise = statewise;
    }
}
