package com.example.tim.exammaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameActivity extends Activity implements View.OnClickListener {
    ImageView profile;
    AlertDialog.Builder quitGame;
    TextView time;
    CountDownTimer timer;
    TextView count;
    TextView question;
    Button[] answer;
    SQLiteDatabase db;

    public class QA {
        String question;
        String answer[]; // answer[0] is the only one correct answer
    }
    List<QA> exam;
    int rightID;
    int questionNumber = 0;
    int rightNumber = 0;
    int wrongNumber = 0;
    long millisRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // profileImage init
        profile = findViewById(R.id.profileImage);
        profile.setOnClickListener(this);
        //quitGame setup
        quitGame = new AlertDialog.Builder(this);
        quitGame.setMessage("結束遊戲?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(true);
        // timeText init
        time = findViewById(R.id.timeText);

        // countText init
        count = findViewById(R.id.countText);
        // questionText init
        question = findViewById(R.id.questionText);
        // answerButton init
        answer = new Button[4];
        int[] answerButton_id = {R.id.answerButton1, R.id.answerButton2, R.id.answerButton3, R.id.answerButton4};
        for(int i = 0; i < 4; i++) {
            answer[i] = findViewById(answerButton_id[i]);
            answer[i].setOnClickListener(this);
        }

        db = openOrCreateDatabase(MainActivity.DATABASE_NAME, Context.MODE_PRIVATE, null);

        setup();
        nextQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        timer = new CountDownTimer(millisRemaining+100,1000){
            @Override
            public void onFinish() {
                time.setTextColor(Color.RED);
                time.setText("0");
                gameOver();
            }
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText("" + millisUntilFinished/1000);
                millisRemaining = millisUntilFinished;
                Log.d("onTick", millisUntilFinished + ""); //  DEBUG only
            }
        };
        timer.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
    }
    @Override
    public void onBackPressed() {
        quitGame.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        db.close();
    }

    // IMCOMPLETE
    public void setup() {
        String[][] data = {
                {"取消、廢除", "abrogate", "exonerate", "pugnacious", "contrition"},
                {"同情、憐憫", "commiserate" , "altruism", "voluble", "ambivalent"},
                {"吹毛求疵", "cavil", "quandary", "reprimand", "censure"},
                {"倉促的、魯莽的", "impetuous", "nettle", "repudiate", "assuage"},
                {"無所不在的", "ubiquitous", "odious", "impalpable", "tenable"},
                {"謹慎的", "prudent", "commodious", "jargon", "wry"},
                {"征服、支配", "subjugate", "flippant", "cantankerous", "warrant"},
                {"形成、起源", "genesis", "lacerate", "omnipotent", "obstinate"},
                {"易怒的", "peevish", "creed", "arduous", "resolute"},
                {"冷漠的、孤立的", "aloof", "concur", "vanguard", "adamant"},
                {"怪誕的、神秘的", "uncanny", "candor", "morose", "digress"},
                {"朦朧的、模糊的", "nebulous", "poignant", "delegate", "spear"},
                {"鼓勵、支持", "abet", "defunct", "acme", "tenuous"},
                {"疲累的、憔悴的", "haggard", "carnal", "waive", "sanction"},
                {"激怒、惹惱", "nettle", "repudiate", "frugal", "corroborate"},
                {"貪婪的", "avaricious", "embellish", "cursory", "paragon"},
                {"有利可圖的", "lucrative", "clement", "vacillate", "pretext"},
                {"延長、拖延", "protract", "fervent", "foible", "quiescent"},
                {"窮困潦倒的", "indigent", "brevity", "stigma", "adroit"},
                {"清楚的、易懂的", "lucid", "salient", "vendetta", "cataclysm"}
        };
        /*

         */
        exam = new ArrayList<>();
        for(String[] set: data) {
            QA qa = new QA();
            qa.question = set[0];
            qa.answer = new String[4];
            for(int j = 0; j < 4; j++) {
                qa.answer[j] = set[j + 1];
            }
            exam.add(qa);
        }
        questionNumber = 0;
        rightNumber = 0;
        wrongNumber = 0;
        millisRemaining = 10000;
    }

    public void nextQuestion() {
        QA next;
        List<Integer> list;

        Random random = new Random();
        int ranNum = random.nextInt(exam.size());

        next = exam.get(ranNum);
        exam.remove(ranNum);

        questionNumber++;
        list = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(list); // randomize the order of 4 answers

        count.setText("第"+questionNumber+"題");
        question.setText(next.question);

        for(int i = 0; i < 4; i++) {
            answer[i].setBackgroundColor(Color.WHITE);
            answer[i].setText(next.answer[list.get(i)]);
            answer[i].setEnabled(true);
            if(list.get(i) == 0) {
                rightID = answer[i].getId();
            }
        }
    }
    // INCOMPLETE
    private void gameOver() {
        int score = rightNumber*10 - wrongNumber*5;
        // update the ranking
        ContentValues cv = new ContentValues(1);
        cv.put("score", score);
        db.insert(MainActivity.TABLE_NAME, null, cv);

        AlertDialog.Builder result;
        result = new AlertDialog.Builder(this);
        result.setTitle("遊戲結束")
                .setMessage("您總共\n答對 "+rightNumber+" 題\n答錯 "+wrongNumber+" 題\n分數: "+score)
                .setNegativeButton("返回首頁", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("再玩一場", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setCancelable(false);

        result.show();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.profileImage) {
            quitGame.show();
        }
        else {
            for(int i = 0; i < 4; i++) {
                answer[i].setEnabled(false);
            }
            Button chose = findViewById(v.getId());
            Button right = findViewById(rightID);
            if (v.getId() == rightID) {
                rightNumber++;
                chose.setBackgroundColor(Color.GREEN);
            } else {
                wrongNumber++;
                chose.setBackgroundColor(Color.RED);
                right.setBackgroundColor(Color.GREEN);
            }
            // CAN NOT FULLY UNDERSTAND
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                public void run(){
                    nextQuestion();
                }
            }, 1000); // delay 1 second

        }
    }
}
