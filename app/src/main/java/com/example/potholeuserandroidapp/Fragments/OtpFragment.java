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
public class OtpFragment extends Fragment {

    private Context context;

    private EditText otpEditText;

    private Button otpButton;

    private String firstname="";
    private String lastname="";
    private String email="";
    private String password="";

    private String type = "signup";


    public OtpFragment() {
        // Required empty public constructor
    }


    public OtpFragment(String email){
        this.email = email;

        this.type = "forgot_password";
    }


    public OtpFragment(String firstname, String lastname , String email,String password){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;

        this.type = "signup";
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        otpEditText = view.findViewById(R.id.otpedittextid);

        otpButton = view.findViewById(R.id.otpbuttonid);

        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpEditText.getText().toString().trim();

                if(type.equals("signup")){
                    if(!firstname.equals("") && !lastname.equals("") && !email.equals("") && !password.equals("") && !otp.equals("")){

                        Retrofit retrofit = NetworkHelper.getRetrofitInstance(context);

                        AuthApi authApi = retrofit.create(AuthApi.class);

                        Call<ResponseBody> signupCall = authApi.signup(new User(firstname,lastname,email,password,otp));

                        signupCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if(response.isSuccessful()){
                                    startActivity(new Intent(context, LoginActivity.class));
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

                            }
                        });

                    }else{
                        Toast.makeText(context, "Make sure all the fields are filled", Toast.LENGTH_SHORT).show();
                    }

                }else if(type.equals("forgot_password")){

                }else{
                    Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
