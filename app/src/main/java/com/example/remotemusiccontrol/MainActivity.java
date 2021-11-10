package com.example.remotemusiccontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText ed1;
    String ip = null;
    SharedPreferences readdata;
    Boolean isServiceAlive = false;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("main","exit");
        if(intent.hasExtra("command")){
            if(intent.getStringExtra("command").equals("exit")) {
                finish();
            }
        }
    }
    protected void onDestroy() {
        Log.d("main", "destroy");
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        readdata = getSharedPreferences("data", 0);
        Log.d("main","create");
        if (readdata.getString("ip", null) != null && !isServiceAlive) {
            ip = readdata.getString("ip", null);
            Intent intent = new Intent(this, MyService.class);
            intent.putExtra("ip", ip);
            startActivity(new Intent(this, MainActivity2.class));
            startService(intent);
            isServiceAlive=true;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1 = findViewById(R.id.ed1);
    }

    public void confirm(View view) {
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor writedata = getSharedPreferences("data", 0).edit();
        ip = ed1.getText().toString();
        if (ip.length() < "192.168.0.1".length()) {
            Toast.makeText(this, "ip输入错误", Toast.LENGTH_LONG).show();
            return;
        }
        startActivity(new Intent(this, MainActivity2.class));
        if(!isServiceAlive){
            Intent intent = new Intent(this, MyService.class);
            intent.putExtra("ip", ip);
            writedata.putString("ip", ip);
            writedata.apply();
            startService(intent);
        }
    }
}