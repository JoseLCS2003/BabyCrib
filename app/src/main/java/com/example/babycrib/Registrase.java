package com.example.babycrib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registrase extends AppCompatActivity {

    EditText nombre,correo,telefono,contra;
    Button registro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrase);
        nombre=findViewById(R.id.name);
        correo=findViewById(R.id.email);
        telefono=findViewById(R.id.telefono);
        contra=findViewById(R.id.password);
        registro=findViewById(R.id.botonRegistro);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmail(correo.getText().toString())){
                    Toast.makeText(Registrase.this, "Correo no Valido", Toast.LENGTH_SHORT).show();
                }
                else if (contra.getText().toString().length() < 8) {
                    Toast.makeText(Registrase.this, "La contraseña debe ser mayor a 8 caracteres", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Registrase.this, "Registrado", Toast.LENGTH_SHORT).show();
                }
            }
        });

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