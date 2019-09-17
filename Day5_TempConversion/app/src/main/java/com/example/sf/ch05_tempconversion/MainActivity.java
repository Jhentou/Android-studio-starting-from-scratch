package com.example.sf.ch05_tempconversion;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener,TextWatcher {

    RadioGroup unit;
    EditText value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unit = (RadioGroup)findViewById(R.id.unit);
        unit.setOnCheckedChangeListener(this);
        value = (EditText) findViewById(R.id.value);
        value.addTextChangedListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        calc();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        calc();
    }

    protected void calc(){
        TextView degF =(TextView)findViewById(R.id.degF);
        TextView degC =(TextView)findViewById(R.id.degC);

        double f,c;

        if(unit.getCheckedRadioButtonId()==R.id.unitF){
            f = Double.parseDouble(value.getText().toString());
            c = (f-32)*5/9;
        }
        else
        {
            c = Double.parseDouble(value.getText().toString());
            f = c*9/5+32;
        }

        degC.setText(String.format("%.lf",c)+getResources().getString(R.string.charC));
        degF.setText(String.format("%.lf",f)+getResources().getString(R.string.charF));
    }
}
