package com.cs411.netflix;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by sameervijay on 10/30/18.
 */

public class NetflixAsyncTask extends AsyncTask<String, String, String> {
    public LoginActivity loginActivity;
    public SearchContentActivity searchContentActivity;
    private static final Gson GSON = new GsonBuilder().create();

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
            if (requestMethod.equals("GET")) {
                fullURL += "?" + formatQueryParams(params);
            }
            URL url = new URL(fullURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(20000);
            connection.setConnectTimeout(25000);
            connection.setRequestMethod(requestMethod);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            if (requestMethod.equals("POST") && params.length > 2) {
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String formattedParams = formatQueryParams(params);
                writer.write(formattedParams);
                writer.flush();
                writer.close();
                outputStream.close();
            }

            connection.connect();

            InputStream inputStream;
            int statusCode = connection.getResponseCode();
            if (statusCode >= 200 && statusCode < 400) {
                // Create an InputStream in order to extract the response object
                inputStream = connection.getInputStream();
            }
            else {
                inputStream = connection.getErrorStream();
            }
            String line;
            StringBuilder requestResponse = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                requestResponse.append(line);
            }

            // Return the response
            return endpoint + ":::" + requestResponse.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String formatQueryParams(String... params) throws UnsupportedEncodingException {
        StringBuilder formatted = new StringBuilder();
        for (int i = 2; i < params.length; i++) {
            if (i != 2) {
                formatted.append("&");
            }
            String[] pair = params[i].split("=");
            formatted.append(pair[0]);
            formatted.append("=");
            formatted.append(URLEncoder.encode(pair[1], "UTF-8"));
        }
        return formatted.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result == null) {
            Log.e("Error", "No response from server");
            return;
        }

        String[] resultParts = result.split(":::");
        result = resultParts[1];
        switch (resultParts[0]) {
            case "add_user":
                SimpleResponse simpleResponse = GSON.fromJson(result, SimpleResponse.class);
                loginActivity.handleReponse(simpleResponse);
                break;
            case "get_users":
                System.out.println(result);
                break;
            case "search_content":
                System.out.println(result);
                break;
            default:
                Log.e("Error", "Unexpected response");
        }
    }
}

