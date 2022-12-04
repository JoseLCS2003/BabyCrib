package com.example.babycrib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivityForResult(i,0);
            }
        }.start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            finish();
        }
    }
}