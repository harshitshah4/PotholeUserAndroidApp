package com.example.potholeuserandroidapp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.potholeuserandroidapp.Activities.LoginActivity;
import com.example.potholeuserandroidapp.Activities.SignupActivity;
import com.example.potholeuserandroidapp.Helpers.NetworkHelper;
import com.example.potholeuserandroidapp.Interfaces.AuthApi;
import com.example.potholeuserandroidapp.Models.ResponseBody;
import com.example.potholeuserandroidapp.Models.User;
import com.example.potholeuserandroidapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private Context context;

    private EditText firstnameEditText;
    private EditText lastnameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    private Button registerButton;

    private Button loginButton;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();


        firstnameEditText = view.findViewById(R.id.registerfirstnameedittextid);
        lastnameEditText = view.findViewById(R.id.registerlastnameedittextid);
        emailEditText = view.findViewById(R.id.registeremailedittextid);
        passwordEditText = view.findViewById(R.id.registerpasswordedittextid);
        confirmPasswordEditText = view.findViewById(R.id.registerconfirmpasswordedittextid);

        registerButton = view.findViewById(R.id.registerbuttonid);

        loginButton = view.findViewById(R.id.registerloginbuttonid);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstname = firstnameEditText.getText().toString().trim();
                final String lastname = lastnameEditText.getText().toString().trim();
                final String email = emailEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if(!firstname.equals("") && !lastname.equals("") && !email.equals("") && !password.equals("") && !confirmPassword.equals("")){
                    if(password.equals(confirmPassword)){


                        Retrofit retrofit = NetworkHelper.getRetrofitInstance(context);

                        AuthApi authApi = retrofit.create(AuthApi.class);

                        Call<ResponseBody> otpCall = authApi.getSignupOtp(new User(email));

                        otpCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if(response.isSuccessful()){
                                    ((SignupActivity) context).replaceFragment(new OtpFragment(firstname,lastname,email,password));
                                }else{
                                    String message = "Something Went Wrong";


                                    if(response.body()!=null && response.body().getMsg() != null){
                                        message = response.body().getMsg();
                                    }

                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else{
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Make sure all the fields are filled", Toast.LENGTH_SHORT).show();
                }


            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginActivity.class));
            }
        });

    }
}
