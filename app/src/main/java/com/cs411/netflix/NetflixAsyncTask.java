package com.cs411.netflix;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sameervijay on 10/30/18.
 */

public class NetflixAsyncTask extends AsyncTask<String, String, String> {
    public NetflixAsyncTask() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /*
     * Expects params in the format:
     * params[0] = requestMethod (i.e. "GET" or "POST")
     * params[1] = endpoint (i.e. "get_users")
     * OPTIONAL params[2] ... params[i] are query parameters (i.e. "username=sameerv2")
     */
    @Override
    protected String doInBackground(String... params) {
        String requestMethod = params[0];
        String endpoint = params[1];
        String fullURL = "https://nosqls411.web.illinois.edu/" + endpoint + ".php";

        try {
            URL url = new URL(fullURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(20000);
            connection.setConnectTimeout(25000);
            connection.setRequestMethod(requestMethod);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            if (params.length > 2) {
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                for (int i = 2; i < params.length; i++) {
                    writer.write(params[i]);
                }
                writer.flush();
                writer.close();
                outputStream.close();
            }

            connection.connect();

            String line;
            StringBuilder requestResponse = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                requestResponse.append(line);
            }

            // Return the response
            return requestResponse.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // Can do things with the response here; printing it for now
        System.out.println("Response: " + result);
    }
}

