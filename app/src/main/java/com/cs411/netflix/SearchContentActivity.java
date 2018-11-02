package com.cs411.netflix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchContentActivity extends AppCompatActivity {
    EditText attributeEntry;
    EditText attributeValue;
    Button searchButton, addButton;
    TextView movieView1, movieView2, directedView1, directedView2;
    TextView genreView1, genreView2, languageView1, languageView2;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username = getIntent().getStringExtra("username");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_content);
        attributeEntry = (EditText) findViewById(R.id.attributeEntry);
        attributeValue = (EditText) findViewById(R.id.attributeValue);
        searchButton = (Button) findViewById(R.id.SearchButton);
        addButton = (Button) findViewById(R.id.addButton);

        movieView1 = (TextView) findViewById(R.id.movieView1);
        movieView2 = (TextView) findViewById(R.id.movieView2);
        directedView1 = (TextView) findViewById(R.id.directedView1);
        directedView2 = (TextView) findViewById(R.id.directedView2);
        genreView1 = (TextView) findViewById(R.id.genreView1);
        genreView2 = (TextView) findViewById(R.id.genreView2);
        languageView1 = (TextView) findViewById(R.id.languageView1);
        languageView2 = (TextView) findViewById(R.id.languageView2);
    }

    public void searchMovies(View view) {
        hideKeyboard(this);
        addButton.setVisibility(View.INVISIBLE);
        NetflixAsyncTask asyncTask = new NetflixAsyncTask();
        asyncTask.searchContentActivity = this;

        ArrayList<String> params = new ArrayList<>();
        params.add("GET");
        params.add("search_content");
        String attribute_text = attributeEntry.getText().toString();
        String attribute_value = attributeValue.getText().toString();
        System.out.println(attribute_text);
        System.out.println(attribute_value);

        if(attribute_text.equals("Genre")){
            params.add("genre=" + attribute_value);
        }
        else if(attribute_text.equals("Language")){
            params.add("language=" + attribute_value);
        }
        else if(attribute_text.equals("Director")){
            params.add("director=" + attribute_value);
        }
        else {
            //Error msg
        }
        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);
        asyncTask.execute(paramsArr);
    }

    /*
    * Onclick for Add Show/Movie button
    * */
    public void addMovies(View v)
    {
        Intent intent = new Intent(this, AddToWatches.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void handleReponse(ContentList contentList) {
        ArrayList<Content> queryContent = contentList.getContentList();
        movieView1.setVisibility(View.INVISIBLE);
        directedView1.setVisibility(View.INVISIBLE);
        genreView1.setVisibility(View.INVISIBLE);
        languageView1.setVisibility(View.INVISIBLE);
        movieView2.setVisibility(View.INVISIBLE);
        directedView2.setVisibility(View.INVISIBLE);
        genreView2.setVisibility(View.INVISIBLE);
        languageView2.setVisibility(View.INVISIBLE);

        if (queryContent.size() > 0) {
            Content first = queryContent.get(0);
            movieView1.setText(first.getName());
            directedView1.setText(first.getDirector());
            genreView1.setText(first.getGenre());
            languageView1.setText(first.getLanguage());
            movieView1.setVisibility(View.VISIBLE);
            directedView1.setVisibility(View.VISIBLE);
            genreView1.setVisibility(View.VISIBLE);
            languageView1.setVisibility(View.VISIBLE);
        }
        if (queryContent.size() > 1) {
            Content second = queryContent.get(1);
            movieView2.setText(second.getName());
            directedView2.setText(second.getDirector());
            genreView2.setText(second.getGenre());
            languageView2.setText(second.getLanguage());
            movieView2.setVisibility(View.VISIBLE);
            directedView2.setVisibility(View.VISIBLE);
            genreView2.setVisibility(View.VISIBLE);
            languageView2.setVisibility(View.VISIBLE);
        }
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
