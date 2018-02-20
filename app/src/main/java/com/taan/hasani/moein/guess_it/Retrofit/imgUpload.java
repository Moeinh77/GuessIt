//package com.taan.hasani.moein.guess_it.Retrofit;
//
//import android.support.v7.app.AppCompatActivity;
//import android.app.Activity;
//import android.content.Intent;
//import android.provider.MediaStore;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import java.io.File;
//import okhttp3.MediaType;import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.RequestBody;
//import okhttp3.ResponseBody;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//
//public class imgUpload {
//    String url = "http://mamadgram.tk/guessIt.php";
//
//    public static final int PICK_IMAGE = 100;
//
//    Service service;
//
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//
//        // Change base URL to your upload server URL.
//        service = new Retrofit.Builder().baseUrl(url).client(client).build().create(Service.class);
//
//        if (btn != null) {
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
//                }
//            });
//        }
//
//    File file = new File(filePath);
//
//            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
//            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
//
////            Log.d("THIS", data.getData().getPath());
//
//            retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(body, name);
//            req.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) { }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    t.printStackTrace();
//                }
//            });
//    }
