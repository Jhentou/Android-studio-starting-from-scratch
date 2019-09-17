package com.example.sf.pokemon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner porkemon;
    ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9;
    String[] name ={"Everywhere","Virtually Everwhere","Very Common",
    "Common","Uncommon","Ununcommon","Rare","Very Rare","Special",
    "Epic","Myths","Still Not Convinced It Exists"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        porkemon = (Spinner)findViewById(R.id.pokemon);
        img1 = (ImageView)findViewById(R.id.img1);
        img2 = (ImageView)findViewById(R.id.img2);
        img3 = (ImageView)findViewById(R.id.img3);
        img4 = (ImageView)findViewById(R.id.img4);
        img5 = (ImageView)findViewById(R.id.img5);
        img6 = (ImageView)findViewById(R.id.img6);
        img7 = (ImageView)findViewById(R.id.img7);
        img8 = (ImageView)findViewById(R.id.img8);
        img9 = (ImageView)findViewById(R.id.img9);
        porkemon.setOnItemSelectedListener(this);

        ArrayAdapter<String>tempAd = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,name);
        tempAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        porkemon.setAdapter(tempAd);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    switch (i){
        case 0:
            img1.setImageResource(R.drawable.poke1);
            img2.setImageResource(R.drawable.poke2);
            img3.setImageResource(R.drawable.poke3);
            img4.setImageResource(R.drawable.poke0);
            img5.setImageResource(R.drawable.poke0);
            img6.setImageResource(R.drawable.poke0);
            img7.setImageResource(R.drawable.poke0);
            img8.setImageResource(R.drawable.poke0);
            img9.setImageResource(R.drawable.poke0);
            break;
        case 1:
            img1.setImageResource(R.drawable.poke4);
            img2.setImageResource(R.drawable.poke5);
            img3.setImageResource(R.drawable.poke6);
            img4.setImageResource(R.drawable.poke7);
            img5.setImageResource(R.drawable.poke0);
            img6.setImageResource(R.drawable.poke0);
            img7.setImageResource(R.drawable.poke0);
            img8.setImageResource(R.drawable.poke0);
            img9.setImageResource(R.drawable.poke0);
            break;
        case 2:
            img1.setImageResource(R.drawable.poke8);
            img2.setImageResource(R.drawable.poke9);
            img3.setImageResource(R.drawable.poke10);
            img4.setImageResource(R.drawable.poke11);
            img5.setImageResource(R.drawable.poke12);
            img6.setImageResource(R.drawable.poke13);
            img7.setImageResource(R.drawable.poke14);
            img8.setImageResource(R.drawable.poke0);
            img9.setImageResource(R.drawable.poke0);
            break;
        case 3:
            img1.setImageResource(R.drawable.poke15);
            img2.setImageResource(R.drawable.poke16);
            img3.setImageResource(R.drawable.poke17);
            img4.setImageResource(R.drawable.poke18);
            img5.setImageResource(R.drawable.poke19);
            img6.setImageResource(R.drawable.poke20);
            img7.setImageResource(R.drawable.poke21);
            img8.setImageResource(R.drawable.poke22);
            img9.setImageResource(R.drawable.poke0);
            break;
        case 4:
            img1.setImageResource(R.drawable.poke23);
            img2.setImageResource(R.drawable.poke24);
            img3.setImageResource(R.drawable.poke25);
            img4.setImageResource(R.drawable.poke26);
            img5.setImageResource(R.drawable.poke27);
            img6.setImageResource(R.drawable.poke28);
            img7.setImageResource(R.drawable.poke29);
            img8.setImageResource(R.drawable.poke30);
            img9.setImageResource(R.drawable.poke0);
            break;
        case 5:
            img1.setImageResource(R.drawable.poke31);
            img2.setImageResource(R.drawable.poke32);
            img3.setImageResource(R.drawable.poke33);
            img4.setImageResource(R.drawable.poke34);
            img5.setImageResource(R.drawable.poke35);
            img6.setImageResource(R.drawable.poke36);
            img7.setImageResource(R.drawable.poke37);
            img8.setImageResource(R.drawable.poke0);
            img9.setImageResource(R.drawable.poke0);
            break;
        case 6:
            img1.setImageResource(R.drawable.poke38);
            img2.setImageResource(R.drawable.poke39);
            img3.setImageResource(R.drawable.poke40);
            img4.setImageResource(R.drawable.poke41);
            img5.setImageResource(R.drawable.poke42);
            img6.setImageResource(R.drawable.poke43);
            img7.setImageResource(R.drawable.poke44);
            img8.setImageResource(R.drawable.poke0);
            img9.setImageResource(R.drawable.poke0);
            break;
        case 7:
            img1.setImageResource(R.drawable.poke45);
            img2.setImageResource(R.drawable.poke46);
            img3.setImageResource(R.drawable.poke47);
            img4.setImageResource(R.drawable.poke48);
            img5.setImageResource(R.drawable.poke49);
            img6.setImageResource(R.drawable.poke50);
            img7.setImageResource(R.drawable.poke51);
            img8.setImageResource(R.drawable.poke0);
            img9.setImageResource(R.drawable.poke0);
            break;
        case 8:
            img1.setImageResource(R.drawable.poke52);
            img2.setImageResource(R.drawable.poke53);
            img3.setImageResource(R.drawable.poke54);
            img4.setImageResource(R.drawable.poke55);
            img5.setImageResource(R.drawable.poke56);
            img6.setImageResource(R.drawable.poke57);
            img7.setImageResource(R.drawable.poke58);
            img8.setImageResource(R.drawable.poke59);
            img9.setImageResource(R.drawable.poke0);
            break;
        case 9:
            img1.setImageResource(R.drawable.poke60);
            img2.setImageResource(R.drawable.poke61);
            img3.setImageResource(R.drawable.poke62);
            img4.setImageResource(R.drawable.poke63);
            img5.setImageResource(R.drawable.poke64);
            img6.setImageResource(R.drawable.poke65);
            img7.setImageResource(R.drawable.poke66);
            img8.setImageResource(R.drawable.poke0);
            img9.setImageResource(R.drawable.poke0);
            break;
        case 10:
            img1.setImageResource(R.drawable.poke67);
            img2.setImageResource(R.drawable.poke68);
            img3.setImageResource(R.drawable.poke69);
            img4.setImageResource(R.drawable.poke70);
            img5.setImageResource(R.drawable.poke0);
            img6.setImageResource(R.drawable.poke0);
            img7.setImageResource(R.drawable.poke0);
            img8.setImageResource(R.drawable.poke0);
            img9.setImageResource(R.drawable.poke0);
            break;
        case 11:
            img1.setImageResource(R.drawable.poke71);
            img2.setImageResource(R.drawable.poke0);
            img3.setImageResource(R.drawable.poke0);
            img4.setImageResource(R.drawable.poke0);
            img5.setImageResource(R.drawable.poke0);
            img6.setImageResource(R.drawable.poke0);
            img7.setImageResource(R.drawable.poke0);
            img8.setImageResource(R.drawable.poke0);
            img9.setImageResource(R.drawable.poke0);
            break;
    }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
