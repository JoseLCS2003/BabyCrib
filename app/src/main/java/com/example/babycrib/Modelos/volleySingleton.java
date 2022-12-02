package com.example.babycrib.Modelos;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class volleySingleton {
    private static  volleySingleton volleySingleton;
    private RequestQueue request;
    private static Context contexto;

    private volleySingleton(Context context) {
        contexto=context;
        request=getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(request==null){
            request= Volley.newRequestQueue(contexto.getApplicationContext());
        }
        return request;
    }

    public static synchronized volleySingleton getInstance(Context context){
        if (volleySingleton==null){
            volleySingleton=new volleySingleton(context);
        }
        return volleySingleton;
    }
    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
