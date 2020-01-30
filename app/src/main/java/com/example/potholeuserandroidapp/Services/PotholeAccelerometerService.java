package com.example.potholeuserandroidapp.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.potholeuserandroidapp.Helpers.NetworkHelper;
import com.example.potholeuserandroidapp.Helpers.PotholeAccelerometer;
import com.example.potholeuserandroidapp.Interfaces.PostApi;
import com.example.potholeuserandroidapp.MainActivity;
import com.example.potholeuserandroidapp.Models.ResponseBody;
import com.example.potholeuserandroidapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PotholeAccelerometerService extends Service implements SensorEventListener {

    FusedLocationProviderClient mFusedLocationClient;

    SensorManager sensorManager;
    Sensor accelerometerSensor;


    com.example.potholeuserandroidapp.Models.Location currentLocation;

    public PotholeAccelerometerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("POTHOLE","SERVICE CALLED");

        createNotificationChannel();


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification  = new NotificationCompat.Builder(this, "pothole_accelerometer_channel")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("Detecting Potholes")
                .setContentText("Using Accelerometer To Detect Potholes")
                .setContentIntent(pendingIntent)
                .build();




        startForeground(1,notification);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "PotholeAccelerometer";
            String description = "This Notification Channel is to show Notification about Pothole Detecting Service";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("pothole_accelerometer_channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("MissingPermission")
    protected void getLastLocation(){

            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    currentLocation = new com.example.potholeuserandroidapp.Models.Location(location.getLatitude(),location.getLongitude());
                                    addAutoGeneratedPost();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            currentLocation = new com.example.potholeuserandroidapp.Models.Location(mLastLocation.getLatitude(),mLastLocation.getLongitude());

            Log.d("location","Found Location");
            addAutoGeneratedPost();
        }
    };


    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }



        @Override
    public void onSensorChanged(SensorEvent event) {
        int res=0;

        Log.d("TAG","onSensorChanged:X:"+ event.values[0]+" Y:"+ event.values[1]+" Z:"+ event.values[2]);
        res= PotholeAccelerometer.calc(event.values[0], event.values[1], event.values[2]);
        if(res==1)
        {

            Log.d("POTHOLE DETECTED","POTHOLE DETECTED");

            getLastLocation();
        }
        else {
            //Log.d(TAG,"NOOO");

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void addAutoGeneratedPost(){

        Retrofit retrofit = NetworkHelper.getRetrofitInstance(this);

        PostApi postApi = retrofit.create(PostApi.class);

        Call<ResponseBody> addAutoGeneratedPostCall = postApi.addAutoDetectedPost(currentLocation);

        addAutoGeneratedPostCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("SENT","POTHOLE SENT");
                }else{
                    Log.d("SENT","POTHOLE NOT SENT");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("SENT","POTHOLE SENT FAILED");
            }
        });
    }

}
