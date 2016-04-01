package com.infinisense.aswinpj.eyesaver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class DetectScreenUnlockService extends Service {

    int timeDelay = 20;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
            if(intent.getAction().equals("android.intent.action.USER_PRESENT"))
            {
                Log.d("SERVICE","SCHEDULING STUFF..."+timeDelay);
                executor.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("SERVICE", "Showing Notification");
                        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                                .setContentTitle("Time to Look Away!")
                                .setAutoCancel(true)
                                .setSmallIcon(R.drawable.eye)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText("You have been using your phone continuously for quite some time.Please look at a distant object and then resume using the phone."))
                                .build();

                        final NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                        manager.notify(0,notification);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                Log.d("SERVICE","CANCELLING THE NOTIFICATION");
                                manager.cancelAll();

                            }
                        },5000);





                }
                },timeDelay,timeDelay, TimeUnit.MINUTES);

            }
            else
            {
                Log.d("SERVICE","SHUTTING DOWN THE EXECUTOR POOL");
                executor.shutdownNow();
            }

        }
    };

    public DetectScreenUnlockService()
    {

    }

    @Override
    public void onCreate() {


        final IntentFilter screenOnFilter = new IntentFilter();
        screenOnFilter.addAction("android.intent.action.USER_PRESENT");
        this.registerReceiver(receiver, screenOnFilter);

        final IntentFilter screenOffFilter = new IntentFilter();
        screenOffFilter.addAction("android.intent.action.SCREEN_OFF");
        this.registerReceiver(receiver,screenOffFilter);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timeDelay = Integer.parseInt(intent.getExtras().getString("delay","20"));
        Log.d("SERVICE", "STARTING THE SERVICE WITH A TIME DELAY OF " + timeDelay);
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Log.d("SERVICE", "SERVICE STOPPED");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
