package com.example.babycrib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.babycrib.Adaptadores.CunaAdaptador;
import com.example.babycrib.Modelos.Cuna;
import com.example.babycrib.Singleton.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {
    VolleySingleton instance;
    Button agg;
    Boolean res=false;
    List<Cuna> cunaList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        instance = VolleySingleton.getInstance(this);
        agg = findViewById(R.id.NuevaCuna);
        obtenerCunas();
        agg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle parametros = new Bundle();
                parametros.putString("nom","");
                Intent i=new Intent(getApplicationContext(),AgregarCuna.class);
                i.putExtras(parametros);
                startActivity(i);
            }
        });
    }

    private void iniciar()
    {
        CunaAdaptador listAdapter = new CunaAdaptador(cunaList);
        RecyclerView recyclerView = findViewById(R.id.Cunas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }
    public boolean onCreateOptionsMenu(android.view.Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.exit){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,getSharedPreferences("credenciales",MODE_PRIVATE).getString(
                            "url","http://34.226.147.180/")+"logout", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            SharedPreferences.Editor editor = getSharedPreferences("credenciales",
                                    MODE_PRIVATE).edit();
                            editor.clear().commit();
                            Intent i=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
            ){
                public Map<String,String> getHeaders() throws AuthFailureError{
                    Map<String,String> headers = new HashMap<>();
                    headers.put("Authorization","Bearer "+getSharedPreferences("credenciales",
                            Context.MODE_PRIVATE).getString("token","null"));
                    return headers;
                }
            };
            VolleySingleton.getInstance(Home.this).addToRequestQueue(jsonObjectRequest);
        } else if(id == R.id.perfil)
        {
            Toast.makeText(Home.this, "Hola", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void obtenerCunas()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,getSharedPreferences("credenciales",MODE_PRIVATE).getString(
                        "url","http://34.226.147.180/")+"cuna/"+String.valueOf(getSharedPreferences(
                                "credenciales",MODE_PRIVATE).getInt("id",0)),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            final Type tipoListCuna = new TypeToken<List<Cuna>>(){}.getType();
                            cunaList = gson.fromJson(String.valueOf(response.getJSONArray("data")),
                                    tipoListCuna);
                            iniciar();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Home.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            public Map<String,String> getHeaders() throws AuthFailureError{
                Map<String,String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+getSharedPreferences("credenciales",
                        Context.MODE_PRIVATE).getString("token","null"));
                headers.put("aioKey",getSharedPreferences("credenciales",MODE_PRIVATE).
                        getString("arduino","none"));
                return headers;
            }
        };
        instance.getRequestQueue().add(jsonObjectRequest);
    }
}