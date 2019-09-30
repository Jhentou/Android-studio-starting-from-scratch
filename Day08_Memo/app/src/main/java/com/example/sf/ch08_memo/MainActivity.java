package com.example.sf.ch08_memo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    String[] aMemo = {
            "1. 按一下可以編輯備忘","2. 長按可以清除備忘","3.","4.","5.","6."};
    ListView lv;
    ArrayAdapter<String> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.listView);
        aa = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,aMemo);

        lv.setAdapter(aa);
        lv.setOnItemLongClickListener(this);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
        Intent it = new Intent(this, Edit.class);
        it.putExtra("備忘", aMemo[pos]);              //附加備忘項目的內容
        startActivityForResult(it, pos);              //啟動 Edit 並以項目位置為識別碼
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            aMemo [i] = (i+1) +".";
        aa.notifyDataSetChanged();
        return true;
    }

    protected void onActivityResult(int requestCode,
        int resultCode, Intent it) {
        if(resultCode == RESULT_OK) {
            aMemo[requestCode] = it.getStringExtra("備忘");
                                                    // 使用傳回的資料更新陣列內容
            aa.notifyDataSetChanged();              // 通知 Adapter 陣列內容有更新
        }
    }
}
