package org.o7planning.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConvertValueActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
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
    }

    public void convertValue(View view)
    {
        double settableText = (valueNum/valueCount) * Double.parseDouble(editText.getText().toString());
        textView.setText(editText.getText().toString() + " " + valueName + "равно " + Double.toString(settableText) + " Рублей");
    }
}