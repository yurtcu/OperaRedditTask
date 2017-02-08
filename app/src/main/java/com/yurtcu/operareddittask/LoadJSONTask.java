package com.yurtcu.operareddittask;

/**
 * Created by Baris on 04.02.2017.
 */
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yurtcu.operareddittask.model.OutmostData;
import com.yurtcu.operareddittask.model.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadJSONTask extends AsyncTask<String, Void, Response> {
    public interface Listener {
        void onLoaded(OutmostData data);
        void onError();
    }

    private Listener mListener;

    public LoadJSONTask(Listener listener) {
        mListener = listener;
    }

    @Override
    protected Response doInBackground(String... strings) {
        try {
            // TODO: Check if any new data available
            String stringResponse = loadJSON(strings[0]);
            Gson gson = new Gson();

            return gson.fromJson(stringResponse, Response.class);
        } catch (IOException e) {
            // TODO: Error handling
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            // TODO: Error handling
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Response response) {
        if (response != null) {
            mListener.onLoaded(response.getData());
        } else {
            // TODO: Error handling
            mListener.onError();
        }
    }

    private String loadJSON(String jsonURL) throws IOException {
        URL url = new URL(jsonURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = in.readLine()) != null) {
            response.append(line);
        }

        in.close();
        return response.toString();
    }
}
