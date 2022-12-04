package com.example.babycrib;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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
                        CountDownTimer t =new CountDownTimer(2000,1000) {
                            @Override
                            public void onTick(long l) {
                                Long d = l / 1000;
                            }

                            @Override
                            public void onFinish() {
                                Intent i=new Intent(getApplicationContext(),Home.class);
                                startActivityForResult(i,0);
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

    public boolean isEmail(String cadena) {
        boolean resultado;
        if (Patterns.EMAIL_ADDRESS.matcher(cadena).matches()) {
            resultado = true;
        } else {
            resultado = false;
        }
        return resultado;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            super.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            finish();
        }
    }
}