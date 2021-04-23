package com.covid19.tracker.response;

import com.covid19.tracker.model.StateName;

public class StateResponse {

    private StateName[] statewise;

    public StateName[] getStatewise() {
        return statewise;
    }

    public void setStatewise(StateName[] statewise) {
        this.statewise = statewise;
    }
}
