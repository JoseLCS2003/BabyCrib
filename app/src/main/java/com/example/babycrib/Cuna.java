package com.example.babycrib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.babycrib.Modelos.Sensores;
import com.example.babycrib.Singleton.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class Cuna extends AppCompatActivity {
    List<Sensores> sensoresLista;
    TextView res1,res2,res3,res4,res5,res6,nomCuna,desCuna;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuna);
        nomCuna=findViewById(R.id.nomCuna);
        desCuna=findViewById(R.id.desCuna);
        getValores();
    }
    public void getValores()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,getSharedPreferences("credenciales",MODE_PRIVATE).getString(
                        "url","none")+"cuna/"+String.valueOf(getSharedPreferences("credenciales",
                MODE_PRIVATE).getInt("id",0))+"/"+nomCuna.getText().toString(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SharedPreferences sharedPreferences = getSharedPreferences("log",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear().commit();
                            Gson gson = new Gson();
                            final Type tipoListSensores = new TypeToken<List<Sensores>>(){}.getType();
                            sensoresLista = gson.fromJson(String.valueOf(response.getJSONArray("data")),
                                    tipoListSensores);
                            inicio(sensoresLista);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        VolleySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);
    }
    public void inicio(List<Sensores> senso)
    {
        res1=findViewById(R.id.res1);
        res1=findViewById(R.id.res2);
        res1=findViewById(R.id.res3);
        res1=findViewById(R.id.res4);
        res1=findViewById(R.id.res5);
        res1=findViewById(R.id.res6);
        res1.setText(senso.get(0).getValue());
        res2.setText(senso.get(1).getValue());
        res3.setText(senso.get(2).getValue());
        res4.setText(senso.get(3).getValue());
        res5.setText(senso.get(4).getValue());
        res6.setText(senso.get(5).getValue());
    }
}