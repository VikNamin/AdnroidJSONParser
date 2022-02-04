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
    double valueString;
    String valueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        valueString = Double.parseDouble(getIntent().getStringExtra("value"));
        valueName = getIntent().getStringExtra("valueName");
        setContentView(R.layout.activity_convert_value);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
    }

    public void convertValue(View view)
    {
        double settableText = valueString * Double.parseDouble(editText.getText().toString());
        textView.setText(editText.getText().toString() + " рублей равно: " + Double.toString(settableText) + " " + valueName);
    }
}