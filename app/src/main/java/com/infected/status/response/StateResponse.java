package com.infected.status.response;

import com.infected.status.model.StateName;

public class StateResponse {

    private StateName[] statewise;

    public StateName[] getStatewise() {
        return statewise;
    }

    public void setStatewise(StateName[] statewise) {
        this.statewise = statewise;
    }
}
