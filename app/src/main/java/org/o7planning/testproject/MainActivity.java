package org.o7planning.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button buttonJson;
    public static String names[] = new String[0];

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(listView.getContext(), ConvertValueActivity.class);
                String[] valueText = ((TextView) itemClicked).getText().toString().split(" |\n");
                System.out.println(valueText[valueText.length-1]);
                intent.putExtra("value", valueText[valueText.length-1]);
                intent.putExtra("valueName", valueText[0]);
                startActivity(intent);
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

    class DownloadJsonTask
            // AsyncTask<Params, Progress, Result>
            extends AsyncTask<String, Void, String> {

        private ListView listView;

        public DownloadJsonTask(ListView listView) {
            this.listView = listView;
        }

        @Override
        protected String doInBackground(String... params) {
            String textUrl = params[0];

            InputStream in = null;
            BufferedReader br= null;
            try {
                URL url = new URL(textUrl);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                int resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                    br= new BufferedReader(new InputStreamReader(in));

                    StringBuilder sb= new StringBuilder();
                    String s= null;
                    while((s= br.readLine())!= null) {
                        sb.append(s);
                        sb.append("\n");
                    }
                    return sb.toString();
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(br);
            }
            return null;
        }

        // When the task is completed, this method will be called
        // Download complete. Lets update UI
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                JSONObject valute = json.getJSONObject("Valute");
                JSONArray valuteNames = valute.names();
//                String names[] = new String[valuteNames.length()];
                names = Arrays.copyOf(names, valuteNames.length());
                for (int i = 0; i<valuteNames.length(); i++){
                    JSONObject valuteObj = valute.getJSONObject((String) valuteNames.get(i));
                    String name = valuteObj.getString("Name");
                    double value = valuteObj.getDouble("Value");
                    names[i] = name + "\n" + value;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(listView.getContext(),
                        android.R.layout.simple_list_item_1, names);
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}