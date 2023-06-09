package com.rose.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private static final int FPS = 60;
    private static final int SPEED = 15;

    private static final int STATUS_PAUSED = 1;
    private static final int STATUS_START = 2;
    private static final int STATUS_OVER = 3;
    private static final int STATUS_PLAYING = 4;

    private GameView mGameView;
    private TextView mGameStatusText;
    private TextView mGameScoreText;
    private Button mGameBtn;

    private final AtomicInteger mGameStatus = new AtomicInteger(STATUS_START);

    private final Handler mHandler = new Handler();

    private String userID;


    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private int userScoreData;
    private String userNickname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGameView = findViewById(R.id.game_view);
        mGameStatusText = findViewById(R.id.game_status);
        mGameBtn = findViewById(R.id.game_control_btn);
        mGameScoreText = findViewById(R.id.game_score);
        mGameView.init();
        mGameView.setGameScoreUpdatedListener(score -> {
            userScoreData = score;
           mHandler.post(() -> mGameScoreText.setText("Score: " + score));
        });

        findViewById(R.id.up_btn).setOnClickListener(v -> {
            if (mGameStatus.get() == STATUS_PLAYING) {
                mGameView.setDirection(Direction.UP);
            }
        });
        findViewById(R.id.down_btn).setOnClickListener(v -> {
            if (mGameStatus.get() == STATUS_PLAYING) {
                mGameView.setDirection(Direction.DOWN);
            }
        });
        findViewById(R.id.left_btn).setOnClickListener(v -> {
            if (mGameStatus.get() == STATUS_PLAYING) {
                mGameView.setDirection(Direction.LEFT);
            }
        });
        findViewById(R.id.right_btn).setOnClickListener(v -> {
            if (mGameStatus.get() == STATUS_PLAYING) {
                mGameView.setDirection(Direction.RIGHT);
            }
        });

        mGameBtn.setOnClickListener(v -> {
            if (mGameStatus.get() == STATUS_PLAYING) {
                setGameStatus(STATUS_PAUSED);
            } else {
                setGameStatus(STATUS_PLAYING);
            }
        });

        setGameStatus(STATUS_START);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGameStatus.get() == STATUS_PLAYING) {
            setGameStatus(STATUS_PAUSED);
        }
    }

    private void makePopup(){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.popup_saving_data, null);
                builder.setView(dialogView);

                Button btnSavingYes = dialogView.findViewById(R.id.btnSavingOK);
                Button btnSavingNo = dialogView.findViewById(R.id.btnSavingNO);

                AlertDialog alertDialog = builder.create();
                btnSavingNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnSavingYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText edtUserNickname = dialogView.findViewById(R.id.edtUserNickname);
                        userNickname = edtUserNickname.getText().toString().trim();

                        if(!userNickname.isEmpty()) {
                            saveScoreData();
                        } else {
                            Toast.makeText(getApplicationContext(), "닉네임을 입력하세요!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });


    }

    private void saveScoreData(){

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("snakeGame");

        UserScore userScore = new UserScore();
        userScore.setIdToken(firebaseUser.getUid());
        userScore.setEmailId(firebaseUser.getEmail());
        userScore.setScore(Integer.parseInt(String.valueOf(userScoreData)));
        userScore.setUserNickname(userNickname);

        LocalDate today = LocalDate.now();
        userScore.setUserDate(String.valueOf(today));

        String tableKey = mDatabaseRef.child("UserScore").push().getKey();
        mDatabaseRef.child("UserScore").child(tableKey).setValue(userScore);
    }


    private void setGameStatus(int gameStatus) {
        int prevStatus = mGameStatus.get();
        mGameStatusText.setVisibility(View.VISIBLE);
        mGameBtn.setText("start");
        mGameStatus.set(gameStatus);
        switch (gameStatus) {
            case STATUS_OVER:
                mGameStatusText.setText("GAME OVER");
                break;
            case STATUS_START:
                mGameView.newGame();
                mGameStatusText.setText("START GAME");
                break;
            case STATUS_PAUSED:
                mGameStatusText.setText("GAME PAUSED");
                break;
            case STATUS_PLAYING:
                if (prevStatus == STATUS_OVER) {
                    mGameView.newGame();
                }
                startGame();
                mGameStatusText.setVisibility(View.INVISIBLE);
                mGameBtn.setText("pause");
                break;
        }
    }

    private void startGame() {
        final int delay = 1000 / FPS;
        new Thread(() -> {
            int count = 0;
            while (!mGameView.isGameOver() && mGameStatus.get() != STATUS_PAUSED) {
                try {
                    Thread.sleep(delay);
                    if (count % SPEED == 0) {
                        mGameView.next();
                        mHandler.post(mGameView::invalidate);
                    }
                    count++;
                } catch (InterruptedException ignored) {
                }
            }
            if (mGameView.isGameOver()) {
                makePopup();
                mHandler.post(() -> setGameStatus(STATUS_OVER));
            }
        }).start();
    }
}