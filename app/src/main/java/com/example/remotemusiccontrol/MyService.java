package com.example.remotemusiccontrol;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MyService extends Service {
    static String ip=null;
    MyBroadcastReceiver myBroadcastReceiver;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("TAG","服务启动");
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.RemoteMusicControl.COMMAND");
        registerReceiver(myBroadcastReceiver, filter);
        ip = intent.getStringExtra("ip");
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("service", "被杀");
        unregisterReceiver(myBroadcastReceiver);
    }
    public void exit(){
        this.stopSelf();
    }
    public void send(String command){
        Log.d("TAG", command);
        new Thread(() -> {
            try {
                Socket socket = new Socket(ip, 8888);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.write(command.getBytes());
                dos.flush();
                dos.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("exit")){
                startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra("command", "exit").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                //startActivity(new Intent(getApplicationContext(), MainActivity2.class).putExtra("command", "exit").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                stopSelf();
            }
            else
                send(intent.getStringExtra("command"));
        }
    }
}