package com.rose.snake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private LinearLayout mainFrame;
    private Typeface mainFont;
    private Typeface mainFontBold;

    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initView();

        makeRankingView();
    }

    private void initView(){
        mainFrame = findViewById(R.id.mainFrame);
        mainFont = getResources().getFont(R.font.jamsil_regular);
        mainFontBold = getResources().getFont(R.font.jamsil_bold);
    }
    private void makeRankingView(){
        mFirebaseAuth = FirebaseAuth.getInstance();

        List scoreList = new ArrayList();
        List userList = new ArrayList();
        List dateList = new ArrayList();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            userID = currentUser.getEmail();
        } else {
            Toast.makeText(getApplicationContext(), "정상적이지 않는 접근입니다!", Toast.LENGTH_LONG).show();
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
        Log.d("email", "email" + userID);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("snakeGame");
//        if(mDatabaseRef.child("UserScore").equalTo(userID) != null) {
            mDatabaseRef.child("UserScore").orderByChild("score").limitToLast(50).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String emailId = userSnapshot.child("emailId").getValue(String.class);
                        if (emailId.equals(userID)) {
                            int score = userSnapshot.child("score").getValue(Integer.class);
                            String userDate = userSnapshot.child("userDate").getValue(String.class);
                            scoreList.add(score);
                            dateList.add(userDate);
                        }
                    }

                    Collections.reverse(scoreList);
                    Collections.reverse(dateList);

                    Log.d("email", "email" + scoreList);
                    Log.d("score", "score" + userList);

                    for (int i = 0; i < scoreList.size(); i++) {
                        int score = (int) scoreList.get(i);
                        String userDate = (String) dateList.get(i);

                        LinearLayout contentsLayout = new LinearLayout(RecordActivity.this);
                        contentsLayout.setOrientation(LinearLayout.HORIZONTAL);
                        contentsLayout.setGravity(Gravity.CENTER);
                        contentsLayout.setBackground(getResources().getDrawable(R.drawable.ranking_border));

                        LinearLayout.LayoutParams paramsContents = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.MATCH_PARENT, (int) (80 * getResources().getDisplayMetrics().density));
                        paramsContents.setMargins(15, 10, 15, 10);
                        contentsLayout.setLayoutParams(paramsContents);

                        TextView txtScore = new TextView(RecordActivity.this);
                        txtScore.setText(String.valueOf(score) + "점");
                        txtScore.setTypeface(mainFontBold);
                        txtScore.setTextColor(getResources().getColor(R.color.bright_green));
                        txtScore.setTextSize(20);

                        LinearLayout.LayoutParams paramsScore = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        paramsScore.setMargins(15, 0, 15, 0);
                        txtScore.setGravity(Gravity.LEFT);
                        txtScore.setLayoutParams(paramsScore);

                        TextView txtUserDate = new TextView(RecordActivity.this);
                        txtUserDate.setText(userDate);
                        txtUserDate.setTypeface(mainFont);
                        txtUserDate.setTextColor(getResources().getColor(R.color.black));
                        txtUserDate.setTextSize(17);

                        LinearLayout.LayoutParams paramsDate = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        paramsDate.setMargins(15, 0, 15, 0);
                        txtUserDate.setLayoutParams(paramsDate);

                        contentsLayout.addView(txtUserDate);
                        contentsLayout.addView(txtScore);

                        mainFrame.addView(contentsLayout);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
//        } else {
//            TextView txtWarning = new TextView(RecordActivity.this);
//            txtWarning.setText("사용자의 기록이 없습니다!");
//            txtWarning.setTypeface(mainFontBold);
//            txtWarning.setTextColor(getResources().getColor(R.color.dark_green));
//            txtWarning.setTextSize(20);
//
//            mainFrame.addView(txtWarning);
//
//        }
    }
}