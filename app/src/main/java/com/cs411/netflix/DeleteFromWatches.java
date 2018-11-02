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

public class DeleteFromWatches extends AppCompatActivity {

    String username;
    EditText title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_delete_from_watches);
        title = (EditText) findViewById(R.id.titleText);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }
    }

    public void handleDelete (View v) throws UnsupportedEncodingException {
        NetflixAsyncTask asyncTask = new NetflixAsyncTask();
        String titleS = title.getText().toString();
        System.out.println(titleS);
        ArrayList<String> params = new ArrayList<>();
        params.add("GET");
        params.add("search_content#");
        params.add(titleS);
        params.add(username);
        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);
        asyncTask.execute(paramsArr);
        Toast.makeText(getApplicationContext(), "Deleted Movie!", Toast.LENGTH_LONG).show();
    }

    public void handleResponse (ContentList contentList, String u){
        NetflixAsyncTask asyncTask = new NetflixAsyncTask();

        //Get username, ContentId,  Rating, Language, Date
        String contentId = contentList.getContentList().get(0).getContentId();
        System.out.println("contentId: " + contentId);

        // Post to database
        ArrayList<String> params = new ArrayList<>();
        params.add("GET");
        params.add("delete_watches");
        params.add("username=" + u);
        params.add("contentId=" + contentId);

        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);
        asyncTask.execute(paramsArr);
    }

}
