package org.o7planning.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ConvertValueActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    TextView textView2;
    EditText editText2;
    double valueNum;
    String valueName;
    int valueCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        valueNum = Double.parseDouble(getIntent().getStringExtra("valueNum"));
        valueName = getIntent().getStringExtra("valueName");
        valueCount = Integer.parseInt(getIntent().getStringExtra("valueCount"));
        setContentView(R.layout.activity_convert_value);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        textView2 = (TextView) findViewById(R.id.textView2);
        editText2 = (EditText) findViewById(R.id.editText2);
    }

    public void convertValue(View view)
    {
        double settableText = (valueNum/valueCount) * Double.parseDouble(editText.getText().toString());
        textView.setText(editText.getText().toString() + " " + valueName + "равно " + (settableText) + " Рублей");
    }

    public void convertValueRubles(View view)
    {
        double settableText = (valueCount/valueNum) * Double.parseDouble(editText2.getText().toString());
        System.out.println(settableText);
        textView2.setText(editText2.getText().toString() + " Рублей равно " + (settableText) + " " + valueName);
    }
}