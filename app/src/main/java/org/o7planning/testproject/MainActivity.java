package org.o7planning.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button buttonJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.listView = (ListView) this.findViewById(R.id.listView);
        this.buttonJson = (Button) this.findViewById(R.id.button_json);

        this.buttonJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAndShowJson(v);
            }
        });
    }

    // When user click on the "Download Json".
    public void downloadAndShowJson(View view) {
        String jsonUrl = "https://www.cbr-xml-daily.ru/daily_json.js";

        // Create a task to download and display json content.
        DownloadJsonTask task = new DownloadJsonTask(this.listView);

        // Execute task (Pass jsonUrl).
        task.execute(jsonUrl);
    }

}