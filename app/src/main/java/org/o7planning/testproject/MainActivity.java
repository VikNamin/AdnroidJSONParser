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
    public static String[] names = new String[0];

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
                intent.putExtra("valueNum", valueText[valueText.length-1]);
                intent.putExtra("valueName", stringNameJoin(valueText));
                intent.putExtra("valueCount", valueText[0]);
                startActivity(intent);
            }
        });
    }

    public void downloadAndShowJson(View view) {
        String jsonUrl = "https://www.cbr-xml-daily.ru/daily_json.js";
        DownloadJsonTask task = new DownloadJsonTask(this.listView);
        task.execute(jsonUrl);
    }

    private String stringNameJoin(String[] str){
        String tempStr = "";
        for (int i = 1; i<str.length-1; i++){
            tempStr+=str[i] + " ";
        }
        return tempStr;
    }

    class DownloadJsonTask extends AsyncTask<String, Void, String> {

        private ListView listView;

        public DownloadJsonTask(ListView listView) {
            this.listView = listView;
        }

        @Override
        protected String doInBackground(String... params) {
            String textUrl = params[0];

            InputStream in = null;
            BufferedReader br = null;
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
                    br = new BufferedReader(new InputStreamReader(in));

                    StringBuilder sb= new StringBuilder();
                    String s = null;
                    while((s = br.readLine())!= null) {
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
                    int count = valuteObj.getInt("Nominal");
                    String name = valuteObj.getString("Name");
                    double value = valuteObj.getDouble("Value");
                    names[i] = count + " " + name + "\n" + value;
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