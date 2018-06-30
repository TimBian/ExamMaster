package com.example.tim.exammaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameActivity extends Activity implements View.OnClickListener {

    ImageView profile;
    AlertDialog.Builder quitGame;
    TextView time;
    CountDownTimer timer;
    TextView count;
    TextView question;
    Button[] answer;
    Button right;
    AlertDialog.Builder result;

    public class QA {
        String question;
        String answer[]; // answer[0] is the only one correct answer
    };
    List<QA> exam;
    int questionNumber = 0;
    int rightNumber = 0;
    int wrongNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // profileImage initialization
        profile = findViewById(R.id.profileImage);
        profile.setOnClickListener(this);

        quitGame = new AlertDialog.Builder(this);

        // timeText initialization
        time = findViewById(R.id.timeText);

        timer = new CountDownTimer(10000,1000){
            @Override
            public void onFinish() {
                time.setTextColor(Color.RED);
                gameOver();
            }
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText("" + millisUntilFinished/1000);
            }
        };
        // countText initialization
        count = findViewById(R.id.countText);
        // question
        question = findViewById(R.id.questionText);
        // answer
        answer = new Button[4]; // not necessary?
        answer[0] = findViewById(R.id.answerButton1);
        answer[1] = findViewById(R.id.answerButton2);
        answer[2] = findViewById(R.id.answerButton3);
        answer[3] = findViewById(R.id.answerButton4);
        for(int i = 0; i < 4; i++) {
            answer[i].setOnClickListener(this);
        }
        // result
        result = new AlertDialog.Builder(this);


        setup(); // load questions
        timer.start();
        nextQuestion();
    }
    // IMCOMPLETE
    public void setup() {
        String[][] data = {
                {"1+1 = ?" , "1", "2", "3", "4"},
                {"java", "咖啡", "蟒蛇", "咖啡廳", "城市"},
                {"advertisement", "廣告", "建議", "目錄", "促銷"}
        };

        exam = new ArrayList<>();
        for(int i = 0; i < data.length; i++) {
            QA qa = new QA();
            qa.question = new String(data[i][0]);
            qa.answer = new String[4];
            for(int j = 0; j < 4; j++) {
                qa.answer[j] = new String(data[i][j+1]);
            }
            exam.add(qa);
        }
        questionNumber = 0;
        rightNumber = 0;
        wrongNumber = 0;
    }

    public void nextQuestion() {
        QA next;
        List<Integer> list;

        next = exam.get(questionNumber++);
        list = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(list); // randomize the order of 4 answers

        count.setText("第"+questionNumber+"題");
        question.setText(next.question);
        for(int i = 0; i < 4; i++) {
            answer[i].setBackgroundColor(Color.WHITE);
            answer[i].setText(next.answer[list.get(i)]);
            if(list.get(i) == 0) {
                right = answer[i];
            }
        }
    }
    // INCOMPLETE
    private void gameOver() {
        // update the ranking
        result.setTitle("遊戲結束")
                .setMessage("您總共\n答對 "+rightNumber+" 題\n答錯 "+wrongNumber+"題!")
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
                .setCancelable(true);
        result.show();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.profileImage) {
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
            quitGame.show();
        }
        else {
            Button chose = findViewById(v.getId());
            QA now = exam.get(questionNumber);
            if (chose.equals(right)) {
                rightNumber++;
                chose.setBackgroundColor(Color.GREEN);
            } else {
                wrongNumber++;
                chose.setBackgroundColor(Color.RED);
                right.setBackgroundColor(Color.GREEN);
            }
            // NOT SURE IT IS RIGHT
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                public void run(){
                    nextQuestion();
                }
            }, 1000);

        }
    }
    /*
    public void onClick(DialogInterface dialog, int which) {
        if (quitGame.equals(dialog)) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                finish();
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                dialog.cancel();
            }
        } else if (result.equals(dialog)) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                finish();
                startActivity(getIntent());
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                finish();
            }
        }
    }
    */
}
