package org.o7planning.testproject;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// A task with String input parameter, and returns the result as String.
public class DownloadJsonTask
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
            String names[] = new String[valuteNames.length()];
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