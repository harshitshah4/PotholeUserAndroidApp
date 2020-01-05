package com.example.potholeuserandroidapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.potholeuserandroidapp.Helpers.NetworkHelper;
import com.example.potholeuserandroidapp.Helpers.TokenHelper;
import com.example.potholeuserandroidapp.Interfaces.AuthApi;
import com.example.potholeuserandroidapp.Models.ResponseBody;
import com.example.potholeuserandroidapp.Models.User;
import com.example.potholeuserandroidapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;

    Button loginButton;

    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        emailEditText = findViewById(R.id.loginemailedittextid);
        passwordEditText = findViewById(R.id.loginpasswordedittextid);

        loginButton = findViewById(R.id.loginbuttonid);

        signupButton = findViewById(R.id.loginsignupbuttonid);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if(!email.equals("") && !password.equals("")){

                    Retrofit retrofit = NetworkHelper.getRetrofitInstance(LoginActivity.this);

                    AuthApi authApi = retrofit.create(AuthApi.class);

                    Call<ResponseBody> loginCall = authApi.login(new User(email,password));

                    loginCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if(response.isSuccessful()){

                                if(response.body()!=null && response.body().getMsg()!=null){
                                    TokenHelper.putRefreshToken(LoginActivity.this,response.body().getMsg());
                                }

                                SharedPreferences.Editor editor = getSharedPreferences("PREFERENCES",MODE_PRIVATE).edit();

                                editor.putBoolean("isLoggedIn",true);
                                editor.apply();

                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                finish();
                            }else{

                                String message = "Something Went Wrong";


                                if(response.body()!=null && response.body().getMsg() != null){
                                    message = response.body().getMsg();
                                }

                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Toast.makeText(LoginActivity.this, "Make sure Email and Password fields are filled", Toast.LENGTH_SHORT).show();
                }

            }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });
    }
}
