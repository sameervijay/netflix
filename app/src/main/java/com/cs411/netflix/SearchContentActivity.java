package com.cs411.netflix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import java.util.ArrayList;

public class SearchContentActivity extends AppCompatActivity {
    EditText attributeEntry;
    EditText attributeValue;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_content);
        attributeEntry = (EditText) findViewById(R.id.attributeEntry);
        attributeValue = (EditText) findViewById(R.id.attributeValue);
        searchButton = (Button) findViewById(R.id.SearchButton);
    }

    public void searchMovies(View view) {
        //hideKeyboard(this);

        NetflixAsyncTask asyncTask = new NetflixAsyncTask();
        asyncTask.searchContentActivity = this;

        ArrayList<String> params = new ArrayList<>();
        params.add("GET");
        params.add("search_content");
        String attribute_text = attributeEntry.getText().toString();
        String attribute_value = attributeValue.getText().toString();

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

}
