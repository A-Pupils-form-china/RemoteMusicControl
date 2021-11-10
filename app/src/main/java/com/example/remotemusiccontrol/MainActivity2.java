package com.example.remotemusiccontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    String ip = null;
    Button[] btn = new Button[5];
    NotificationManagerCompat notificationManager;


    @SuppressLint({"UnspecifiedImmutableFlag", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        btn[0] = findViewById(R.id.btn_last);
        btn[1] = findViewById(R.id.btn_next);
        btn[2] = findViewById(R.id.btn_up);
        btn[3] = findViewById(R.id.btn_down);
        btn[4] = findViewById(R.id.btn_play);
        createNotificationChannel();



        Intent previousi = new Intent("com.example.RemoteMusicControl.COMMAND");
        Intent playi = new Intent("com.example.RemoteMusicControl.COMMAND");
        Intent nexti = new Intent("com.example.RemoteMusicControl.COMMAND");
        Intent self_intent = new Intent(this, MainActivity2.class);
        previousi.putExtra("command", "lastsong");
        playi.putExtra("command", "pause");
        nexti.putExtra("command", "nextsong");
        PendingIntent previous = PendingIntent.getBroadcast(this, 1, previousi,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent play = PendingIntent.getBroadcast(this, 2, playi,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent next = PendingIntent.getBroadcast(this, 3, nexti,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent self = PendingIntent.getActivity(this, 4, self_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "emm")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.makefg)
                .setContentIntent(self)
                .addAction(R.drawable.previous, "Previous", previous)
                .addAction(R.drawable.makefg, "Pause", play)
                .addAction(R.drawable.next, "Next", next)
                .setContentTitle("RemoteMusicControl")
                .setContentText("emm")
                .setOngoing(true);
        notificationManager.notify(1,builder.build());
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ControlPane";
            String description = "A notification for music control";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("emm", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void LastSong(View view){
        send("lastsong");
    }
    public void NextSong(View view){
        send("nextsong");
    }
    public void VolumeUp(View view){
        send("increasevolume");
    }
    public void VolumeDown(View view){
        send("decreasevolume");
    }
    public void play(View view){
        send("pause");
    }
    public void send(String command){
        Intent intent = new Intent("com.example.RemoteMusicControl.COMMAND");
        intent.putExtra("command", command);
        sendBroadcast(intent);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        Log.d("keytest", String.valueOf(keyCode));
        switch(keyCode){
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                send("decreasevolume");
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                send("increasevolume");
                return true;
        }
        return true;
    }
    protected void onDestroy() {
        Log.d("main1", "destroy");
        notificationManager.cancel(1);
        super.onDestroy();
    }
    public void exit(View view){
        send("exit");
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("main1","exit");
        if(intent.hasExtra("command")){
            if(intent.getStringExtra("command").equals("exit")) {
                finish();
            }
        }
    }
}