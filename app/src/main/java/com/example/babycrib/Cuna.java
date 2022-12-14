package com.example.babycrib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cuna extends AppCompatActivity {
    List<Sensores> sensoresLista;
    String val;
    Button eliminar,editar;
    TextView res1,res2,res3,res4,res5,res6,nomCuna,desCuna;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuna);
        nomCuna=findViewById(R.id.nomCuna);
        desCuna=findViewById(R.id.desCuna);
        editar = findViewById(R.id.editCuna);
        eliminar=findViewById(R.id.eliminarCuna);
        editar.setEnabled(true);
        eliminar.setEnabled(true);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val="ON";
                eliminar.setEnabled(true);
                editar.setEnabled(false);
                apagarled();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val="OFF";
                editar.setEnabled(true);
                eliminar.setEnabled(false);
                apagarled();
            }
        });
        Bundle parametros = this.getIntent().getExtras();
        String datos[]=parametros.getStringArray("datos");
        nomCuna.setText(datos[0]);
        desCuna.setText(datos[1]);
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
                            Gson gson = new Gson();
                            final Type tipoListSensores = new TypeToken<List<Sensores>>(){}.getType();
                            sensoresLista = gson.fromJson(String.valueOf(response.getJSONArray("data")),
                                    tipoListSensores);
                            inicio();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Cuna.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+getSharedPreferences("credenciales",
                        Context.MODE_PRIVATE).getString("token","null"));
                headers.put("aioKey",getSharedPreferences("credenciales",MODE_PRIVATE).
                        getString("arduino","none"));
                return headers;
            }
        };
        VolleySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);
        CountDownTimer t =new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                Long d = l / 1000;
            }
            @Override
            public void onFinish() {
                getValores();
            }
        }.start();
    }
    public void inicio()
    {
        res1=findViewById(R.id.res1);
        res2=findViewById(R.id.res2);
        res3=findViewById(R.id.res3);
        res4=findViewById(R.id.res4);
        res5=findViewById(R.id.res5);
        res6=findViewById(R.id.res6);
        if(!sensoresLista.get(0).getValue().equals("none")){
            if(sensoresLista.get(0).getValue().equals("1")){
                res1.setText("Hay movimiento");
            }else{res1.setText("No hay movimiento");}
            if(sensoresLista.get(1).getValue().equals("1")){
                res2.setText("Si hay sonido");
            }else{res2.setText("No hay sonido");}
            res3.setText("");
            if(Integer.parseInt(sensoresLista.get(3).getValue()) < 1000){
                res4.setText("Luz apagada");
            }else{res4.setText("Luz encendida");}
            if(sensoresLista.get(4).getValue().equals("1")){
                res5.setText("Hay humo");
            }else{res5.setText("No hay humo");}
            res6.setText(sensoresLista.get(5).getValue()+"  Grados");
        }
    }

    public void apagarled()
    {
        JSONObject datos;
        datos = new JSONObject();
        try {
            datos.put("value",val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, "https://io.adafruit.com/api/v2/Leoncio030203/feeds/led/data",
                datos, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(val == "ON"){
                    Toast.makeText(Cuna.this, "LED Encendido", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Cuna.this, "LED Apagado", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Cuna.this, "ERROR!", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap <String,String> headre = new HashMap<String,String>();
                headre.put("X-AIO-Key","aio_XuDR855LzMrt1MK6HjiXRQdBw1za");
                return headre;
            }
        };
        VolleySingleton.getInstance(Cuna.this).addToRequestQueue(jsonObjectRequest);
    }

}