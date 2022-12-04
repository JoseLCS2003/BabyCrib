package com.example.babycrib;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.babycrib.Singleton.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button inicial, registrase;
    EditText correo, contra;
    int cont = 0;
    VolleySingleton instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        if(preferences.getString("token","error") != "error"){
            Intent i=new Intent(getApplicationContext(),Home.class);
            startActivity(i);
        }
        inicial = findViewById(R.id.botonLogin);
        registrase = findViewById(R.id.botonRegistra);
        correo = findViewById(R.id.email);
        contra = findViewById(R.id.contraseña);
        instance=VolleySingleton.getInstance(this);
        inicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correo.getText().toString().isEmpty() || contra.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Falta Datos", Toast.LENGTH_SHORT).show();
                } else if (!isEmail(correo.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Correo no valido", Toast.LENGTH_SHORT).show();
                } else if (contra.getText().toString().length() < 8) {
                    Toast.makeText(MainActivity.this, "La contraseña debe ser mayor a 8 caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    inicial.setEnabled(false);
                    inciarSesion();
                    inicial.setEnabled(true);
                }
            }
        });
        registrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registrase = new Intent(getApplicationContext(), Registrase.class);
                startActivity(registrase);
            }
        });

    }
    public void inciarSesion()
    {
        JSONObject datos = new JSONObject();
        try {
            datos.put("email",correo.getText().toString());
            datos.put("password",contra.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "http://192.168.100.180:8000/api/logging",
                datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(MainActivity.this, "Iniciando sesion", Toast.LENGTH_SHORT).show();
                        CountDownTimer t =new CountDownTimer(2000,1000) {
                            @Override
                            public void onTick(long l) {
                                Long d = l / 1000;
                            }

                            @Override
                            public void onFinish() {
                                try {
                                    guardarToken(response.getJSONObject("data").getString("token"),
                                            response.getJSONObject("data").getInt("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Intent i=new Intent(getApplicationContext(),Home.class);
                                startActivity(i);
                            }
                        }.start();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        instance.addToRequestQueue(jsonObjectRequest);
    }

    public void guardarToken(String token,int id)
    {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token",token);
        editor.putInt("id",id);
        editor.commit();
    }

    public boolean isEmail(String cadena) {
        boolean resultado;
        if (Patterns.EMAIL_ADDRESS.matcher(cadena).matches()) {
            resultado = true;
        } else {
            resultado = false;
        }
        return resultado;
    }
}