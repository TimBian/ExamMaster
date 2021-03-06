package com.example.tim.exammaster;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    static final String DATABASE_NAME = "GameDB";
    static final String TABLE_NAME = "RankingList";

    Button play;
    Button login;
    Button score;
    SQLiteDatabase db;


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

        db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, score INTEGER)";
        db.execSQL(createTable);
        ContentValues cv = new ContentValues(1);
        cv.put("score", 0);
        for(int i = 0; i < 7; i++) {
            db.insert(MainActivity.TABLE_NAME, null, cv);
        }
        db.close();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.playButton) {
            Intent it = new Intent(this, GameActivity.class);
            startActivity(it);
        }
        else if(v.getId() == R.id.loginButton) {

        }
        else if(v.getId() == R.id.rankingButton) {
            Intent it = new Intent(this, RankingActivity.class);
            startActivity(it);
        }
    }
}
