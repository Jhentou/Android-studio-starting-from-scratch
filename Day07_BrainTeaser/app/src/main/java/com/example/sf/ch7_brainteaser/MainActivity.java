package com.example.sf.ch7_brainteaser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    String[] queArr = {"甚麼門永遠關不上?","什麼東西沒人愛吃?","甚麼瓜不能吃?","什麼布切不斷?","什麼鼠最愛乾淨?","偷甚麼不犯法?","廁所要放什麼花?"};
    String[] ansArr = {"球門","虧","傻瓜","瀑布","環保署","偷笑","五月花"};
    Toast tos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                queArr);

        ListView lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        tos = Toast.makeText(this, "",Toast.LENGTH_SHORT);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        tos.setText("答案："+ ansArr[i]);
        tos.show();
    }
}

