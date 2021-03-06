package com.cs411.netflix;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.cs411.netflix.GsonTemplates.SimpleResponse;

import java.util.ArrayList;

/**
 * Created by sameervijay on 10/31/18.
 */

public class LoginActivity extends AppCompatActivity {
    // FullName | Username | Age | Region | Ethnicity | FBProfile | Language
    EditText nameEntry, usernameEntry, ageEntry, languageEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameEntry = (EditText) findViewById(R.id.nameEntry);
        usernameEntry = (EditText) findViewById(R.id.usernameEntry);
        ageEntry = (EditText) findViewById(R.id.ageEntry);
        languageEntry = (EditText) findViewById(R.id.languageEntry);

//        NetflixAsyncTask asyncTask = new NetflixAsyncTask();
//        asyncTask.execute("GET", "get_users");
    }

    public void addUser(View view) {
        hideKeyboard(this);

        NetflixAsyncTask asyncTask = new NetflixAsyncTask();
        asyncTask.loginActivity = this;

        ArrayList<String> params = new ArrayList<>();
        params.add("POST");
        params.add("add_user");
        if (!nameEntry.getText().toString().equals("")) params.add("name=" + nameEntry.getText().toString());
        if (!usernameEntry.getText().toString().equals("")) params.add("username=" + usernameEntry.getText().toString());
        if (!ageEntry.getText().toString().equals("")) params.add("age=" + ageEntry.getText().toString());
        if (!languageEntry.getText().toString().equals("")) params.add("language=" + languageEntry.getText().toString());

        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);
        asyncTask.execute(paramsArr);
    }

    public void handleReponse(SimpleResponse response) {
        if (response.getStatusCode() == 200) {
            Toast.makeText(this, "User added!", Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(this, SearchContentActivity.class);
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("username", usernameEntry.getText().toString());
            startActivity(intent);
        } else {
            if (response.getStatusCode() == 409) {
                Toast.makeText(this, "Username taken. Please pick another one", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "User not added. Please try again", Toast.LENGTH_LONG).show();
            }
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
