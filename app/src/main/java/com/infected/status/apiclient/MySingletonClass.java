package com.infected.status.apiclient;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingletonClass {
    private static MySingletonClass instance;
    private RequestQueue requestQueue;
    private Context context;

    private MySingletonClass(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized MySingletonClass getInstance(Context context) {
        if (instance == null) {
            instance = new MySingletonClass(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
