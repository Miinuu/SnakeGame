package com.rose.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtEmail, txtPassword, txtName,txtPhone;
    private Button btnRegister;
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

        btnRegister.setOnClickListener(registerEvent);
    }

    /** 회원가입 이벤트 리스너 */
    View.OnClickListener registerEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String userID = txtEmail.getText().toString();
            String userPW = txtPassword.getText().toString();
            String userName = txtName.getText().toString();
            String userPhone = txtPhone.getText().toString();

            Response.Listener<String> responeListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success){
                            Toast.makeText(getApplicationContext(),"회원 가입 성공",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this,startActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"회원 가입에 실패했습니다.",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            };

            RegisterRequest registerRequest = new RegisterRequest(userID,userPW,userName,userPhone,responeListener);
            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
            queue.add(registerRequest);
        }
    };

}