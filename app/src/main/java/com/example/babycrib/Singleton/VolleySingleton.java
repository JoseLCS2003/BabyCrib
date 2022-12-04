package com.example.babycrib.Singleton;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton volleySingleton;
    private RequestQueue request;
    private static Context contexto;

    private VolleySingleton(Context context) {
        contexto=context;
        request=getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(request==null){
            request= Volley.newRequestQueue(contexto.getApplicationContext());
        }
        return request;
    }

    public static synchronized VolleySingleton getInstance(Context context){
        if (volleySingleton==null){
            volleySingleton=new VolleySingleton(context);
        }
        return volleySingleton;
    }
    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }

}
