package com.example.babycrib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle parametros = new Bundle();
                parametros.putString("nom",nomCuna.getText().toString());
                Intent i=new Intent(getApplicationContext(),AgregarCuna.class);
                i.putExtras(parametros);
                startActivity(i);
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
                            SharedPreferences sharedPreferences = getSharedPreferences("log",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear().commit();
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
    }
    public void inicio()
    {
        res1=findViewById(R.id.res1);
        res2=findViewById(R.id.res2);
        res3=findViewById(R.id.res3);
        res4=findViewById(R.id.res4);
        res5=findViewById(R.id.res5);
        res6=findViewById(R.id.res6);
        if(sensoresLista.get(0).getValue() == "1"){
        res1.setText("Hay movimiento");
        }else{res1.setText("No hay movimiento");}
        if(sensoresLista.get(1).getValue() == "1"){
        res2.setText("Si hay sonido");
        }else{res2.setText("No hay sonido");}
        res3.setText(sensoresLista.get(2).getValue());
        if(Integer.parseInt(sensoresLista.get(3).getValue()) < 1000){
        res4.setText("Luz apagada");
        }else{res4.setText("Luz encendida");}
        if(sensoresLista.get(4).getValue() == "1"){
        res5.setText("Hay humo");
        }else{res5.setText("No hay humo");}
        res6.setText(sensoresLista.get(5).getValue()+"  Grados");
        getValores();
    }
}