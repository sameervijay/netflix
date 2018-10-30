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
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(requestMethod);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            if (params.length > 2) {
                for (int i = 2; i < params.length; i++) {
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(params[i]);
                    writer.flush();
                    writer.close();
                    os.close();
                }
            }

            conn.connect();

            String line, response = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line=br.readLine()) != null) {
                response += line;
            }

            // Do something with the response; printing it for now
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

