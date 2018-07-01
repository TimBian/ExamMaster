package com.example.tim.exammaster;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RankingActivity extends AppCompatActivity implements View.OnClickListener{

    Button individual;
    Button online;
    Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        individual = findViewById(R.id.INDIVIDUAL);
        online = findViewById(R.id.ONLINE);
        menu = findViewById(R.id.MENU);

        individual.setOnClickListener(this);
        online.setOnClickListener(this);
        menu.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.INDIVIDUAL) {
            //The ranking of individual
        }
        else if(v.getId() == R.id.ONLINE) {
            //The ranking of online
        }
        else{
            finish();
        }
    }
}
