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

    public NetflixAsyncTask(){
        //set context variables if required
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("https://nosqls411.web.illinois.edu/find_user.php");
            System.out.println("32");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write("first=Sameer");
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            String line, response = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line=br.readLine()) != null) {
                response += line;
            }

            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

