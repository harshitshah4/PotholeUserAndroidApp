package com.example.potholeuserandroidapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.potholeuserandroidapp.Helpers.MediaUploader;
import com.example.potholeuserandroidapp.Helpers.NetworkHelper;
import com.example.potholeuserandroidapp.Interfaces.PostApi;
import com.example.potholeuserandroidapp.Models.Field;
import com.example.potholeuserandroidapp.Models.Location;
import com.example.potholeuserandroidapp.Models.Post;
import com.example.potholeuserandroidapp.Models.ResponseBody;
import com.example.potholeuserandroidapp.Models.Signed;
import com.example.potholeuserandroidapp.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddActivity extends AppCompatActivity {

    EditText postEditText;
    Button postImageButton;

    Button postLocationButton;
    EditText postLocationDescription;

    ImageView postImageView;

    Button postSubmitButton;


    Location location = new Location("Current Location","",15.61290748629416,73.7385424253531);
    String text;
    Bitmap bitmap;

    int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        String apiKey = "AIzaSyCH5Esn42epQB4jI98DMS7IukVt5ZPKGBE";

        Places.initialize(getApplicationContext(), apiKey);

// Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);

        postEditText = findViewById(R.id.addpostedittextid);

        postImageButton = findViewById(R.id.addpostimagebuttonid);

        postLocationButton = findViewById(R.id.addpostlocationbuttonid);

        postLocationDescription = findViewById(R.id.addpostlocationdescriptionid);

        postImageView = findViewById(R.id.addpostimageviewid);

        postSubmitButton = findViewById(R.id.addpostbuttonid);

        postSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                text = postEditText.getText().toString().trim();

                postSubmitButton.setEnabled(false);
                //postUploadProgressBar.setVisibility(View.VISIBLE);

                if(bitmap!=null && text!=null){

                    MediaUploader.uploadMedia(AddActivity.this , bitmap, new MediaUploader.MediaUploaderListener() {
                        @Override
                        public void onSuccess(String image) {
                            location.setDescription(postLocationDescription.getText().toString());
                            Post post = new Post(text,image,location);
                            uploadPost(post);
                        }

                        @Override
                        public void onFailure() {

                            postSubmitButton.setEnabled(true);
                            //Toast.makeText(AddActivity.this, "HERE:Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{

                    Toast.makeText(AddActivity.this, "Make sure all fields are filled", Toast.LENGTH_SHORT).show();

                }


            }
        });


        postLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);

                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(AddActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

            }
        });


        postImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });




    }

    public void uploadPost(Post post){

        Retrofit retrofit = NetworkHelper.getRetrofitInstance(AddActivity.this);
        PostApi postApi = retrofit.create(PostApi.class);

        Call<ResponseBody> postCall = postApi.addPost(post);

        postCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                    Toast.makeText(AddActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    //postUploadProgressBar.setVisibility(View.GONE);

                }else{
                    postSubmitButton.setEnabled(true);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                postSubmitButton.setEnabled(true);
            }
        });

        //postUploadProgressBar.setVisibility(View.INVISIBLE);


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0  && resultCode == Activity.RESULT_OK){

            if(data!=null && data.getExtras().get("data")!=null){
                bitmap = (Bitmap) data.getExtras().get("data");
                postImageView.setImageBitmap(bitmap);
            }


        }else if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                location.setTitle(place.getName());
                location.setLatitude(place.getLatLng().latitude);
                location.setLongitude(place.getLatLng().longitude);
                postLocationButton.setText(location.getTitle());
                Log.i("TESTING", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TESTING", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }


    }

}

