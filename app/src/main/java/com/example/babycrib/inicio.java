package com.example.babycrib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        splash();
    }
    public void splash(){
        CountDownTimer t =new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                Long d = l / 1000;
            }

            @Override
            public void onFinish() {
                SharedPreferences preferences =getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                if(preferences.getString("token","error") != "error"){
                    Intent i=new Intent(getApplicationContext(),Home.class);
                    startActivity(i);
                }
                else {
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                }
            }
        }.start();
    }
}