package com.example.sf.ch07_dialogshow;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder bdr = new AlertDialog.Builder(this);
        bdr.setMessage("交談窗示範繳學!\n"+"請按返回鍵關閉交談窗");
        bdr.setTitle("歡迎");
        bdr.setIcon(android.R.mipmap.sym_def_app_icon);
        bdr.setCancelable(true);
        bdr.show();
    }
}
