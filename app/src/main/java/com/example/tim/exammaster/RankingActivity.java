package com.example.tim.exammaster;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class RankingActivity extends Activity implements View.OnClickListener{

    static final int MAX = 10;
    static final String[] FROM = new String[] {"score"};

    Button individual;
    Button online;
    Button menu;
    ListView score;
    ListView index;

    SQLiteDatabase db;
    Cursor cur;

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

        score = findViewById(R.id.scoreList);
        index = findViewById(R.id.indexList);

        db = openOrCreateDatabase(MainActivity.DATABASE_NAME, Context.MODE_PRIVATE, null);
        cur = db.rawQuery("SELECT _id, score" +
                                " FROM " + MainActivity.TABLE_NAME +
                                " ORDER BY score DESC " +
                                "LIMIT " + MAX
                            , null);

        SimpleCursorAdapter cAdapter;
        cAdapter = new SimpleCursorAdapter(this, R.layout.item, cur, FROM, new int[]{R.id.score}, 0);
        score.setAdapter(cAdapter);
        score.setVisibility(ListView.INVISIBLE);


        String[] idx = new String[10];
        for(int i = 0; i < 10; i++) {
            idx[i] = "#"+(i+1);
        }
        ArrayAdapter<String> aAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, idx);
        index.setAdapter(aAdapter);
        index.setVisibility(ListView.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        cur.close();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.INDIVIDUAL) {
            score.setVisibility(ListView.VISIBLE);
            index.setVisibility(ListView.VISIBLE);
        }
        else if(v.getId() == R.id.ONLINE) {
            score.setVisibility(ListView.INVISIBLE);
            index.setVisibility(ListView.INVISIBLE);
        }
        else{
            finish();
        }
    }
}
