package com.rose.snake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText txtID,txtPW;
    Button btnLogin;

    private SignInButton btn_google;
    private GoogleApiClient googleApiClient;
    private static final int REQ_SIGN_GOOGLE = 100; //구글 로그인 결과 코드
    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증 변수
    private DatabaseReference mDatabaseRef; // 실시간 DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView(){
        txtID = findViewById(R.id.txtEmail);
        txtPW = findViewById(R.id.txtPasswd);
        btnLogin = findViewById(R.id.btnLogin);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("snakeGame").child("UserAccount");

        btnLogin.setOnClickListener(loginEvent);
    }


    /** 회원가입 이벤트 리스너 */
    View.OnClickListener loginEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String strEmail = txtID.getText().toString();
            String strPasswd = txtPW.getText().toString();

            if(strEmail.equals("")){
                Toast.makeText(LoginActivity.this,"이메일을 입력 해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }else if(strPasswd.equals("")){
                Toast.makeText(LoginActivity.this,"비밀번호를 입력 해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }


            mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean emailCheck = false; // 이메일 존재 여부 체크 변수

                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        UserAccount accounts = snapshot1.getValue(UserAccount.class);

                        if(strEmail.equals(accounts.getEmailId())){
                            emailCheck = true;
                            mFirebaseAuth.signInWithEmailAndPassword(accounts.getEmailId(),strPasswd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        boolean isEmail = user.isEmailVerified();
                                        if(isEmail){
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    intent.putExtra("userID", strEmail);

                                                    startActivity(intent);
                                                    finish(); //현재 엑티비티 파괴
                                                }
                                            },3000);
                                        }else {
                                            Toast.makeText(LoginActivity.this,"이메일 인증을 해주세요.",Toast.LENGTH_SHORT).show();
                                            mFirebaseAuth.signOut();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this,"비밀번호를 잘못 입력 하였습니다",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    if(!emailCheck){
                        Toast.makeText(LoginActivity.this,"존재하지 않는 이메일 입니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LoginActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }
            });

        }
    };
}