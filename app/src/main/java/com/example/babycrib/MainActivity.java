package com.example.babycrib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button inicial, registrase;
    EditText correo, contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicial = findViewById(R.id.botonLogin);
        registrase = findViewById(R.id.botonRegistra);
        correo = findViewById(R.id.email);
        contra = findViewById(R.id.contraseña);

        inicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correo.getText().toString().isEmpty() || contra.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Falta Datos", Toast.LENGTH_SHORT).show();
                }
                else if( contra.getText().toString().length() < 8){
                    Toast.makeText(MainActivity.this, "La contraseña debe ser mayor a 8 caracteres", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent inicialSesion=new Intent(getApplicationContext(),Home.class);
                    startActivity(inicialSesion);
                }
            }
        });
        registrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registrase=new Intent(getApplicationContext(),Registrase.class);
                startActivity(registrase);
            }
        });
        


    }
}