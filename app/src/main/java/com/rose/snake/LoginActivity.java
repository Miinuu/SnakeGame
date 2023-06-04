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

public class LoginActivity extends AppCompatActivity {
    EditText txtID,txtPW;
    Button btnLogin;

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

        btnLogin.setOnClickListener(loginEvent);
    }


    /** 회원가입 이벤트 리스너 */
    View.OnClickListener loginEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String userID = txtID.getText().toString();
            String userPW = txtPW.getText().toString();

            Response.Listener<String> responeListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success){
                            String userID = jsonObject.getString("userID");
                            String userPass = jsonObject.getString("userPassword");

                            Toast.makeText(getApplicationContext(),"환영합니다 \uD83D\uDC0D",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("userID",userID);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"로그인에 실패했습니다.",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            };

            LoginRequest loginRequest = new LoginRequest(userID,userPW,responeListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
        }
    };
}