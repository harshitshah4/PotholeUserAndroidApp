package com.example.potholeuserandroidapp.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.potholeuserandroidapp.Interfaces.PostApi;
import com.example.potholeuserandroidapp.Models.Field;
import com.example.potholeuserandroidapp.Models.Post;
import com.example.potholeuserandroidapp.Models.Signed;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MediaUploader {

    public static void uploadMedia(Context context , Bitmap bitmap , final MediaUploaderListener mediaUploaderListener){

        Retrofit retrofit = NetworkHelper.getRetrofitInstance(context);

        final PostApi postApi = retrofit.create(PostApi.class);

        final File file = saveBitmap(context,bitmap);


        Call<Signed> signedCall = postApi.getSignedUpload(file.getName());
        signedCall.enqueue(new Callback<Signed>() {
            @Override
            public void onResponse(Call<Signed> call, Response<Signed> response) {

                if(response.isSuccessful()){

                    final String image = response.body().getId();
                    Field field = response.body().getFields();



                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*0"),file);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("file",file.getName(),requestBody);

                    Call<String> mediaCall = postApi.uploadMedia(response.body().getUrl(),getRequestBody(field.getKey()),getRequestBody(field.getContentDisposition()),getRequestBody(field.getContentType()),getRequestBody(field.getBucket()),getRequestBody(field.getXAMZAlgorithm()),getRequestBody(field.getXAMZCredentials()),getRequestBody(field.getXAMZDate()),getRequestBody(field.getPolicy()),getRequestBody(field.getXAMZSignature()),filePart);

                    mediaCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){

                                mediaUploaderListener.onSuccess(image);

                            }else{
                                mediaUploaderListener.onFailure();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            mediaUploaderListener.onFailure();
                        }
                    });




                }else{
                    mediaUploaderListener.onFailure();
                }


            }

            @Override
            public void onFailure(Call<Signed> call, Throwable t) {
                mediaUploaderListener.onFailure();
            }
        });

    }

    private static RequestBody getRequestBody(String value){
        return RequestBody.create(MediaType.parse("multipart/form-data"),value);

    }

    private static File saveBitmap(Context context, Bitmap bitmap){


        String filename = "pippo.png";
        File sd = context.getCacheDir();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dest;
    }

    public interface MediaUploaderListener{

        void onSuccess(String image);
        void onFailure();

    }

}
