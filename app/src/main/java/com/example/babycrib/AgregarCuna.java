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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.babycrib.Singleton.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AgregarCuna extends AppCompatActivity {
    Button agregar;
    EditText nombre,descripcion;
    CheckBox s1,s2,s3,s4,s5,s6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cuna);
        iniciar();
    }
    private void iniciar()
    {
        agregar = findViewById(R.id.AgregarCuna);
        nombre = findViewById(R.id.edtNomCuna);
        descripcion = findViewById(R.id.edtDesCuna);
        s1 = findViewById(R.id.Sensor1); s2 = findViewById(R.id.Sensor2); s3 = findViewById(R.id.Sensor3);
        s4 = findViewById(R.id.Sensor4); s5 = findViewById(R.id.Sensor5); s6 = findViewById(R.id.Sensor6);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nombre.getText().toString().isEmpty() || descripcion.getText().toString().isEmpty())
                {
                    Toast.makeText(AgregarCuna.this, "Llene el nombre y la descripcion", Toast.LENGTH_SHORT).show();
                }else if(s1.isChecked()||s2.isChecked()||s3.isChecked()||s4.isChecked()||s5.isChecked()||s6.isChecked()){
                    agregar.setEnabled(false);
                    agregarCuna();
                }else {
                    Toast.makeText(AgregarCuna.this, "Seleccione minimo un sensor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void  agregarCuna()
    {
        JSONObject cuna=new JSONObject(),sensores=new JSONObject(),datos=new JSONObject();
        try {
            cuna.put("name",nombre.getText().toString());
            cuna.put("description",descripcion.getText().toString());
            sensores.put("sensor1",s1.isChecked());
            sensores.put("sensor2",s2.isChecked());
            sensores.put("sensor3",s3.isChecked());
            sensores.put("sensor4",s4.isChecked());
            sensores.put("sensor5",s5.isChecked());
            sensores.put("sensor6",s6.isChecked());
            datos.put("sensores",sensores);
            datos.put("cuna",cuna);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Integer id=getSharedPreferences("credenciales",Context.MODE_PRIVATE).getInt("id",0);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                "http://192.168.100.180:8000/api/create/"+id.toString(),
                datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(AgregarCuna.this, "Nueva cuna Agregada", Toast.LENGTH_SHORT).show();
                        CountDownTimer t =new CountDownTimer(2000,1000) {
                            @Override
                            public void onTick(long l) {
                                Long d = l / 1000;
                            }
                            @Override
                            public void onFinish() {
                                Intent i=new Intent(getApplicationContext(),Home.class);
                                startActivity(i);
                            }
                        }.start();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        agregar.setEnabled(true);
                        if(error.networkResponse.statusCode == 406)
                        {
                            Toast.makeText(AgregarCuna.this, "Nombre ya utilizado", Toast.LENGTH_SHORT).show();
                        }else if(error.networkResponse.statusCode == 404)
                        {
                            Toast.makeText(AgregarCuna.this, "No se encontro el usuario", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AgregarCuna.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
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
                            Toast.makeText(AgregarCuna.this, "Error", Toast.LENGTH_SHORT).show();
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
            VolleySingleton.getInstance(AgregarCuna.this).addToRequestQueue(jsonObjectRequest);
        } else if(id == R.id.perfil)
        {
            Toast.makeText(AgregarCuna.this, "Hola", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}