package com.rose.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

public class GameStartActivity extends AppCompatActivity {

    private Button btnStartGame,btnLogout,btnRanking,btnRecord;
    private String userID;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        getIntentedData();

        initView();

    }

    private void initView(){
        btnStartGame = findViewById(R.id.btnStartGame);
        btnLogout = findViewById(R.id.btnLogout);
        btnRanking = findViewById(R.id.btnRanking);
        btnRecord = findViewById(R.id.btnRecord);

        btnStartGame.setOnClickListener(startGameEvent);
        btnLogout.setOnClickListener(logoutEvent);
    }

    private void getIntentedData(){
        userID = getIntent().getStringExtra("userID");
    }

    View.OnClickListener startGameEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(GameStartActivity.this,MainActivity.class);
            intent.putExtra("userID",userID);
            startActivity(intent);
        }
    };

    View.OnClickListener logoutEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(GameStartActivity.this,startActivity.class);
            startActivity(intent);
            finish();
        }
    };

    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
            toast.cancel();
        }
    }

}