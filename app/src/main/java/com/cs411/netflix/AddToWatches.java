package com.cs411.netflix;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AddToWatches extends AppCompatActivity {
    String username, languageString, ratingString, dateString;
    EditText title, language, rating, date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_watches);
        title = (EditText) findViewById(R.id.titleText);
        language = (EditText) findViewById(R.id.language);
        rating = (EditText) findViewById(R.id.rating);
        date = (EditText) findViewById(R.id.date);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            //below was added due to Dashboard Activity movie dialog
            if(!extras.getString("movie_name").equals("")){
                title.setText(extras.getString("movie_name"));
            }
        }
    }

    public void handleDone (View v) throws UnsupportedEncodingException {
        NetflixAsyncTask asyncTask = new NetflixAsyncTask();
        languageString = language.getText().toString();
        ratingString = rating.getText().toString();
        dateString = date.getText().toString();
        String titleS = title.getText().toString();
        System.out.println(titleS);
        ArrayList<String> params = new ArrayList<>();
        params.add("GET");
        params.add("search_content!"); //Remember to change in Async Task
        params.add(titleS);
        params.add(username + ":::" + languageString + ":::" + ratingString + ":::" + dateString);

        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);
        asyncTask.execute(paramsArr);
        Toast.makeText(getApplicationContext(), "Added Movie!", Toast.LENGTH_LONG).show();
    }

    public void handleResponse (ContentList contentList, String u, String l, String r, String d){
        NetflixAsyncTask asyncTask = new NetflixAsyncTask();

        //Get username, ContentId,  Rating, Language, Date
        String contentId = contentList.getContentList().get(0).getContentId();
        System.out.println("contentId: " + contentId);
        System.out.println("language: " + l);
        System.out.println("rating: " + r);
        System.out.println("dare: " + d);


        // Post to database
        ArrayList<String> params = new ArrayList<>();
        params.add("POST");
        params.add("add_watches");
        params.add("username=" + u);
        params.add("contentId=" + contentId);
        params.add("rating=" + r);
        params.add("language=" + l);
        params.add("date=" + d);


        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);
        asyncTask.execute(paramsArr);
    }

//    public void handleReponseFinished(SimpleResponse response) {
//        System.out.println("status code: " + response.getStatusCode());
//        if (response.getStatusCode() == 200) {
//            Toast.makeText(getApplicationContext(), "Added Movie!", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "Error in adding user", Toast.LENGTH_LONG).show();
//        }
//    }
//

}
