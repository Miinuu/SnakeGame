package com.rose.snake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private int userScoreData;
    private LinearLayout mainFrame;
    private Typeface mainFont;
    private Typeface mainFontBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        mainFrame = findViewById(R.id.mainFrame);
        mainFont = getResources().getFont(R.font.jamsil_regular);
        mainFontBold = getResources().getFont(R.font.jamsil_bold);

        makeRankingView();
    }


    private void makeRankingView(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        List scoreList = new ArrayList();
        List userList = new ArrayList();
        List dateList = new ArrayList();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("snakeGame");
        mDatabaseRef.child("UserScore").orderByChild("score").limitToLast(20).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String emailId = userSnapshot.child("emailId").getValue(String.class);
                    int score = userSnapshot.child("score").getValue(Integer.class);
                    String userDate = userSnapshot.child("userDate").getValue(String.class);
                    scoreList.add(score);
                    userList.add(emailId);
                    dateList.add(userDate);
                }

                Collections.reverse(scoreList);
                Collections.reverse(userList);
                Collections.reverse(dateList);

                Log.d("email", "email" + scoreList);
                Log.d("score", "score" + userList);


                for (int i = 0; i < scoreList.size(); i++) {
                    String email = (String) userList.get(i);
                    int score = (int) scoreList.get(i);
                    String userDate = (String) dateList.get(i);

                    LinearLayout contentsLayout = new LinearLayout(RankingActivity.this);
                    contentsLayout.setOrientation(LinearLayout.HORIZONTAL);
                    contentsLayout.setGravity(Gravity.CENTER);
                    contentsLayout.setBackground(getResources().getDrawable(R.drawable.ranking_border));

                    LinearLayout.LayoutParams paramsContents = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.MATCH_PARENT, (int) (80 * getResources().getDisplayMetrics().density));
                    paramsContents.setMargins(15, 10, 15, 10);
                    contentsLayout.setLayoutParams(paramsContents);

                    TextView txtRanking = new TextView(RankingActivity.this);
                    txtRanking.setText( (i+1) + " 위");
                    txtRanking.setTypeface(mainFontBold);
                    txtRanking.setTextColor(getResources().getColor(R.color.dark_green));
                    txtRanking.setTextSize(20);

                    LinearLayout.LayoutParams paramsRanking = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    paramsRanking.setMargins(15, 0, 15, 0);
                    txtRanking.setLayoutParams(paramsRanking);

                    TextView txtEmail = new TextView(RankingActivity.this);
                    txtEmail.setText(email);
                    txtEmail.setTypeface(mainFont);
                    txtEmail.setTextColor(getResources().getColor(R.color.black));
                    txtEmail.setTextSize(17);

                    LinearLayout.LayoutParams paramsEmail = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    paramsEmail.setMargins(15, 0, 15, 0);
                    txtEmail.setLayoutParams(paramsEmail);

                    TextView txtScore = new TextView(RankingActivity.this);
                    txtScore.setText(String.valueOf(score) + "점");
                    txtScore.setTypeface(mainFontBold);
                    txtScore.setTextColor(getResources().getColor(R.color.bright_green));
                    txtScore.setTextSize(20);

                    LinearLayout.LayoutParams paramsScore = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    paramsScore.setMargins(15, 0, 15, 0);
                    txtScore.setLayoutParams(paramsScore);

                    TextView txtUserDate = new TextView(RankingActivity.this);
                    txtUserDate.setText(userDate);
                    txtUserDate.setTypeface(mainFont);
                    txtUserDate.setTextColor(getResources().getColor(R.color.black));
                    txtUserDate.setTextSize(17);

                    LinearLayout.LayoutParams paramsDate = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    paramsDate.setMargins(15, 0, 15, 0);
                    txtUserDate.setLayoutParams(paramsDate);

                    contentsLayout.addView(txtRanking);
                    contentsLayout.addView(txtEmail);
                    contentsLayout.addView(txtUserDate);
                    contentsLayout.addView(txtScore);


                    mainFrame.addView(contentsLayout);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {            }
        });
    }
}