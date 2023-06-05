package com.rose.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FindAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        Button findId = findViewById(R.id.btnFindId);
        Button findPw = findViewById(R.id.btnFindPw);

        findId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindAccountActivity.this,FindIdActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindAccountActivity.this,FindPwActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private long backKeyPressedTime = 0;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FindAccountActivity.this,startActivity.class);
        startActivity(intent);
        finish();
        backKeyPressedTime = System.currentTimeMillis();
        return;
    }

}