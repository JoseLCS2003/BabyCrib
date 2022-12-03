package com.example.babycrib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Patterns;
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

public class Registrase extends AppCompatActivity {

    EditText nom,mail,telefono,contra;
    Button registro;
    VolleySingleton instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrase);
        nom=findViewById(R.id.name);
        mail=findViewById(R.id.email);
        telefono=findViewById(R.id.telefono);
        contra=findViewById(R.id.password);
        registro=findViewById(R.id.botonRegistro);
        instance=VolleySingleton.getInstance(this);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nom.getText().toString().isEmpty() || mail.getText().toString().isEmpty() || telefono.getText().toString().isEmpty() || contra.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Falta Datos", Toast.LENGTH_SHORT).show();
                }
                else if(!isEmail(mail.getText().toString())){
                    Toast.makeText(Registrase.this, "Correo no Valido", Toast.LENGTH_SHORT).show();
                }
                else if (contra.getText().toString().length() < 8) {
                    Toast.makeText(Registrase.this, "La contraseña debe ser mayor a 8 caracteres", Toast.LENGTH_SHORT).show();
                }
                else{
                    registro.setEnabled(false);
                    registrar();
                    registro.setEnabled(true);
                }
            }
        });
    }
    public void registrar()
    {
        JSONObject datos,persona;
        datos=new JSONObject();
        persona= new JSONObject();
        try {
            datos.put("name",nom.getText().toString());
            datos.put("email",mail.getText().toString());
            datos.put("password",contra.getText().toString());
            persona.put("persona",datos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url="http://192.168.100.180:8000/api/logup";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                persona,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(Registrase.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Registrase.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        instance.addToRequestQueue(jsonObjectRequest);
    }
    public  boolean isEmail(String cadena) {
        boolean resultado;
        if (Patterns.EMAIL_ADDRESS.matcher(cadena).matches()) {
            resultado = true;
        } else {
            resultado = false;
        }
        return resultado;
    }
}