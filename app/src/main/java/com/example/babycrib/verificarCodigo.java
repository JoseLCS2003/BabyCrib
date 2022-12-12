package com.example.babycrib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class verificarCodigo extends AppCompatActivity {
    Button btnEnviarCodigo;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_codigo);
        btnEnviarCodigo=findViewById(R.id.btnEnviarCodigo);
        editText=findViewById(R.id.edtCode);
        btnEnviarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciar();
            }
        });
    }
    public void iniciar()
    {
        JSONObject datos,persona;
        datos=new JSONObject();
        try {
            datos.put("code_verf",Integer.parseInt(editText.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,getSharedPreferences("credenciales",MODE_PRIVATE).getString(
                "url","http://0.0.0.0/")+"verf",
                datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(verificarCodigo.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        CountDownTimer t =new CountDownTimer(2000,1000) {
                            @Override
                            public void onTick(long l) {
                                Long d = l / 1000;
                            }

                            @Override
                            public void onFinish() {
                                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);
                            }
                        }.start();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btnEnviarCodigo.setEnabled(true);
                        Toast.makeText(verificarCodigo.this,
                                String.valueOf(error.networkResponse.statusCode), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        VolleySingleton.getInstance(verificarCodigo.this).addToRequestQueue(jsonObjectRequest);
    }
}