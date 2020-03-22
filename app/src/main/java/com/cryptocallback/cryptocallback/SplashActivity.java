package com.cryptocallback.cryptocallback;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cryptocallback.cryptocallback.LogIn.SignIn;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView splash_image;
    private TextView splash_text;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash_image = (ImageView) findViewById(R.id.splash_image);
        splash_text = (TextView) findViewById(R.id.splash_text);



        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.push_down);
        splash_image.setAnimation(animation);

        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.push_right);
        splash_text.setAnimation(animation);

        Thread thread = new Thread(){
            @Override
            public void run() {

                try {
                    sleep(2000);
                    startActivity(new Intent(getApplicationContext(), SignIn.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                super.run();
            }
        };
        thread.start();
    }
}
