package com.example.potholeuserandroidapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.potholeuserandroidapp.Fragments.HomeFragment;
import com.example.potholeuserandroidapp.Fragments.ProfileFragment;
import com.example.potholeuserandroidapp.MainActivity;
import com.example.potholeuserandroidapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {


    BottomNavigationView homeBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES",MODE_PRIVATE);

        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);

        if(!isLoggedIn){
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }







        homeBottomNavigationView  = findViewById(R.id.homebottomnavigationviewid);





        homeBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homemenuid:
                        replaceFragment(new HomeFragment());
                        return true;
                    case R.id.writemenuid:
                        startActivity(new Intent(HomeActivity.this,AddActivity.class));
                        return true;
                    case R.id.accountmenuid:
                        replaceFragment(new ProfileFragment());
                        return true;
                    default:
                        return false;
                }

            }
        });

        homeBottomNavigationView.setSelectedItemId(R.id.homemenuid);
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.homefragmentcontainerid,fragment);
        fragmentTransaction.commit();


    }

}
