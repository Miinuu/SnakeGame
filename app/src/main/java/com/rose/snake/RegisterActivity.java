package com.rose.snake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtEmail, txtPassword, txtPasswordCheck, txtName,txtPhone;
    private Button btnRegister;
    private ImageButton btnCancle;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView(){
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPasswd);
        txtName= findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        btnRegister = findViewById(R.id.btnRegister);
        btnCancle = findViewById(R.id.btnCancle);
        txtPasswordCheck = findViewById(R.id.txtPasswdCheck);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("snakeGame");

        btnCancle.setOnClickListener(cancleEvent);
        btnRegister.setOnClickListener(registerEvent);
    }

    private void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "인증 이메일이 발송되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "인증 이메일이 발송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static boolean isValid(String password){
        if(password.length() < 8){
            return false;
        }
        String pattern = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";
        if(!Pattern.matches(pattern, password)){
            return false;
        }

        if(password.contains(" ")){
            return false;
        }

        return true;
    }

    private void finishActivity(){
        Intent intent = new Intent(RegisterActivity.this, startActivity.class);
        startActivity(intent);
        finish();
    }

    View.OnClickListener cancleEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishActivity();
        }
    };

    /** 회원가입 이벤트 리스너 */
    View.OnClickListener registerEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String strEmail = txtEmail.getText().toString().trim();
            String strPwd = txtPassword.getText().toString().trim();
            String strPwdCheck = txtPasswordCheck.getText().toString().trim();
            String strName = txtName.getText().toString().trim();
            String strPhone = txtPhone.getText().toString().trim();

            if(strEmail.equals("")){
                Toast.makeText(RegisterActivity.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }else if(strPwd.equals("")){
                Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }else if(strPwdCheck.equals("")){
                Toast.makeText(RegisterActivity.this, "비밀번호 확인을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }else if(strName.equals("")){
                Toast.makeText(RegisterActivity.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }else if(strPhone.equals("")){
                Toast.makeText(RegisterActivity.this, "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }else if(!(strPhone.length() == 11)){
                Toast.makeText(RegisterActivity.this, "전화번호를 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!(strPwd.equals(strPwdCheck))){
                Toast.makeText(RegisterActivity.this, "비밀번호가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!(isValid(strPwd))){
                Toast.makeText(RegisterActivity.this, "비밀번호가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                        sendVerificationEmail(); // 이메일 발송

                        UserAccount account = new UserAccount();
                        account.setIdToken(firebaseUser.getUid());
                        account.setEmailId(firebaseUser.getEmail());
                        account.setName(strName);
                        account.setPhone(strPhone); // 확인 필요함 입력이 안되어 있을 시 빈문자열이 들어가는지 확인

                        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                        Toast.makeText(RegisterActivity.this, "회원가입에 성공 하셨습니다.", Toast.LENGTH_SHORT).show();

                        finishActivity();
                    } else {
                        Toast.makeText(RegisterActivity.this, "이미 회원가입 된 이메일 입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };
}