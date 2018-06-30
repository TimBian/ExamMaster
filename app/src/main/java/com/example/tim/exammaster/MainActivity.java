package com.example.tim.exammaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    Button play;
    Button login;
    Button score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.playButton);
        login = findViewById(R.id.loginButton);
        score = findViewById(R.id.rankingButton);

        play.setOnClickListener(this);
        login.setOnClickListener(this);
        score.setOnClickListener(this);
    }
    // INCOMPLETE
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.playButton) {
            Intent it = new Intent(this, GameActivity.class);
            startActivity(it);
        }
        else if(v.getId() == R.id.loginButton) {

        }
        else if(v.getId() == R.id.rankingButton) {

        }
    }
}
