package com.infinisense.aswinpj.eyesaver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }



    public void startService(View view) {
        Toast.makeText(getApplicationContext(),"STARTING SERVICE",Toast.LENGTH_LONG).show();
        Intent starter = new Intent(this,DetectScreenUnlockService.class);
        starter.putExtra("delay","1");
        startService(starter);
    }

    public void stopService(View view) {
        Toast.makeText(getApplicationContext(),"STOPPING SERVICE",Toast.LENGTH_LONG).show();
        stopService(new Intent(this, DetectScreenUnlockService.class));
    }


}
