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
        SharedPreferences preferences =getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("arduiono","aio_TZdt143aF1doR4Q8tmLHS0j1ALZU");
        editor.putString("url","http://34.203.216.195/api/");
        editor.commit();
        CountDownTimer t =new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                Long d = l / 1000;
            }

            @Override
            public void onFinish() {
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