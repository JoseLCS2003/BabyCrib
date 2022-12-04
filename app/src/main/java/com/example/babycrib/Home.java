package com.example.babycrib;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.babycrib.Singleton.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    VolleySingleton instance;
    Button agg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        instance = VolleySingleton.getInstance(this);
        agg = findViewById(R.id.NuevaCuna);
        agg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),AgregarCuna.class);
                startActivity(i);
            }
        });
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.exit){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,"http://192.168.100.180:8000/api/logout", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            SharedPreferences.Editor editor = getSharedPreferences("credenciales", MODE_PRIVATE).edit();
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
}