package com.cs411.netflix;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class activity_update_watches extends AppCompatActivity {

    String username, languageString, ratingString, dateString;
    EditText title, language, rating, date;
    private final String emptyString = "DEFAULT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity_update_watches);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = (EditText) findViewById(R.id.titleText);
        language = (EditText) findViewById(R.id.langText);
        rating = (EditText) findViewById(R.id.ratingText);
        date = (EditText) findViewById(R.id.dateText);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }
    }

    public void handleDoneUpdate(View v) throws UnsupportedEncodingException {
        NetflixAsyncTask asyncTask = new NetflixAsyncTask();
        languageString = language.getText().toString();
        if(languageString == null || languageString.equals("")) languageString = emptyString;
        ratingString = rating.getText().toString();
        if(ratingString == null || ratingString.equals("")) ratingString = emptyString;
        dateString = date.getText().toString();
        if(dateString == null || dateString.equals("")) dateString = emptyString;
        String titleS = title.getText().toString();
        if(titleS==null){
            Toast.makeText(getApplicationContext(), "title field cannot be blank", Toast.LENGTH_LONG).show();
            return;
        }
        else if(titleS.equals("")){
            Toast.makeText(getApplicationContext(), "title field cannot be blank", Toast.LENGTH_LONG).show();
            return;
        }
        System.out.println(titleS);
        ArrayList<String> params = new ArrayList<>();
        params.add("GET");
        params.add("search_content^"); //Remember to change in Async Task - done
        params.add(titleS);
        params.add(username + ":::" + languageString + ":::" + ratingString + ":::" + dateString);

        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);
        asyncTask.execute(paramsArr);
        Toast.makeText(getApplicationContext(), "Updated Movie!", Toast.LENGTH_LONG).show();
    }

    /**TODO: add function that returns field values and call in async. if any fields are blank then don't update those
     * */

    public void handleResponse (ContentList contentList, String u, String l, String r, String d){
        NetflixAsyncTask asyncTask = new NetflixAsyncTask();
//just assume that the request succeeded and the movie was there
        //Get username, ContentId,  Rating, Language, Date
        String contentId = contentList.getContentList().get(0).getContentId();
        System.out.println("contentId: " + contentId);
        System.out.println("language: " + l);
        System.out.println("rating: " + r);
        System.out.println("date: " + d);


        // Post to database
        ArrayList<String> params = new ArrayList<>();
        params.add("POST");
        params.add("update_watches");
        params.add("username=" + u);
        params.add("contentId=" + contentId);
        if(!r.equals(emptyString)) params.add("rating=" + r);
        if(!l.equals(emptyString)) params.add("language=" + l);
        if(!d.equals(emptyString)) params.add("date=" + d);


        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);
        asyncTask.execute(paramsArr);
    }

}
