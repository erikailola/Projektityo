package com.example.kuntakatselu2;

import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataRetriever {

    public interface DataFetchListener {
        void onDataFetched(JSONObject result);
        void onError(String error);
    }

    public static void fetchData(String apiUrl, String query, final DataFetchListener listener) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);

                    conn.getOutputStream().write(params[1].getBytes("UTF-8"));

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    return response.toString();
                } catch (Exception e) {
                    return "Error: " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    if (result.startsWith("Error:")) {
                        listener.onError(result);
                    } else {
                        listener.onDataFetched(new JSONObject(result));
                    }
                } catch (Exception e) {
                    listener.onError("Error parsing JSON: " + e.getMessage());
                }
            }
        }.execute(apiUrl, query);
    }
}