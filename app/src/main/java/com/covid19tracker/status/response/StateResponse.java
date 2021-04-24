package com.covid19tracker.status.response;

import com.covid19tracker.status.model.StateName;

public class StateResponse {

    private StateName[] statewise;

    public StateName[] getStatewise() {
        return statewise;
    }

    public void setStatewise(StateName[] statewise) {
        this.statewise = statewise;
    }
}
