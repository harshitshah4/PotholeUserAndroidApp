package com.example.potholeuserandroidapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.potholeuserandroidapp.Fragments.HomeFragment;
import com.example.potholeuserandroidapp.Fragments.ProfileFragment;
import com.example.potholeuserandroidapp.MainActivity;
import com.example.potholeuserandroidapp.R;
import com.example.potholeuserandroidapp.Services.PotholeAccelerometerService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    int permission_request_code = 1;

    SharedPreferences sharedPreferences;

    BottomNavigationView homeBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences("PREFERENCES",MODE_PRIVATE);

        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);

        if(!isLoggedIn){
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }


        boolean allowDetectingPotholeAccelerometer = sharedPreferences.getBoolean("allowDetectingPotholeAccelerometer",false);

        if(allowDetectingPotholeAccelerometer){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("POTHOLE","POTHOLE SERVICE");
                Intent potholeAcceleromterServiceIntent = new Intent(this, PotholeAccelerometerService.class);
                startForegroundService(potholeAcceleromterServiceIntent);
            }
        }

        boolean isPotholeAccelerometerDialogShown=sharedPreferences.getBoolean("isPotholeAccelerometerDialogShown",false);

        if(!isPotholeAccelerometerDialogShown){

            new AlertDialog.Builder(this)
                    .setTitle("Allow Auto Detection of Pothole")
                    .setMessage("Do You Want To Allow This App To Detect Pothole Even When Your App Is In Foreground Whenever You Travel Through A Pothole , If Yes, Accelerometer Permission Are Required")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions();
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();

            sharedPreferences.edit().putBoolean("isPotholeAccelerometerDialogShown",true).apply();
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

    private void requestPermissions(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},permission_request_code);

        }else{
            sharedPreferences.edit().putBoolean("allowDetectingPotholeAccelerometer",true).apply();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent potholeAcceleromterServiceIntent = new Intent(this, PotholeAccelerometerService.class);
                startForegroundService(potholeAcceleromterServiceIntent);
            }
        }

    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.homefragmentcontainerid,fragment);
        fragmentTransaction.commit();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == permission_request_code){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                sharedPreferences.edit().putBoolean("allowDetectingPotholeAccelerometer",true).apply();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent potholeAcceleromterServiceIntent = new Intent(this, PotholeAccelerometerService.class);
                    startForegroundService(potholeAcceleromterServiceIntent);
                }

            }else{
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
