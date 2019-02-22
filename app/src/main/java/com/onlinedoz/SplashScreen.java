package com.onlinedoz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedP;
    boolean FirstRun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView imageView = findViewById(R.id.img_gif);

       // GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this)
                .load(R.drawable.loader)
                .into(imageView);



        sharedP = getSharedPreferences(MainActivity.USERDATA, Context.MODE_PRIVATE);
        FirstRun = sharedP.getBoolean("BareAval",true);

        if (FirstRun){

            // Toast.makeText(this, "این اجرای اول برنامه ست", Toast.LENGTH_SHORT).show();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent Myintent = new Intent(SplashScreen.this,Login.class);
                    startActivity(Myintent);
                    finish();

                }
            },6000);
        }else {

            //Toast.makeText(this, "بیش از یک اجرا", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent Myintent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(Myintent);
                    finish();

                }
            },6000);


        }


    }
}
