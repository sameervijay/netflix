package com.cs411.netflix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs411.netflix.GsonTemplates.Content;
import com.cs411.netflix.GsonTemplates.SimpleResponse;
import com.cs411.netflix.GsonTemplates.Watches;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContentDetailActivity extends AppCompatActivity {
    private TextView nameView, directorView, genreView, durationView, releaseView;
    private EditText ratingEntry, languageEntry;
    private Button watchesButton;
    private ImageView thumbnailView;
    private String username;
    private Content content;
    private Watches watches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);

        thumbnailView = findViewById(R.id.thumbnailCD);
        nameView = findViewById(R.id.nameCD);
        directorView = findViewById(R.id.directorCD);
        genreView = findViewById(R.id.genreCD);
        durationView = findViewById(R.id.durationCD);
        releaseView = findViewById(R.id.releaseCD);
        ratingEntry = findViewById(R.id.ratingCD);
        languageEntry = findViewById(R.id.languageCD);
        watchesButton = findViewById(R.id.markWatchedCD);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            content = extras.getParcelable("content");
            username = extras.getString("username");
            watches = extras.getParcelable("watches");
        }
        // Sets the watches button and surround UI based on whether watches variable is populated
        changeWatchedButton();

        nameView.setText(content.getName());
        directorView.setText(content.getDirector());
        genreView.setText("Genre: " + content.getGenre());
        durationView.setText("Duration: " + content.getDuration() + " mins");
        releaseView.setText("Released: " + content.getReleaseDate());
        Picasso.with(this).load(content.getThumbnail()).resize(3000, 4438).onlyScaleDown().into(thumbnailView);

    }
    @Override
    public void finish() {
        Intent intent = new Intent();
        if (watches != null) {
            watches.setUserRating(Float.parseFloat(ratingEntry.getText().toString()));
            watches.setViewingLanguage(languageEntry.getText().toString());
            intent.putExtra("watches", watches);
            setResult(RESULT_OK, intent);
            watches = null;
            markWatched(watchesButton);
        }
        super.finish();
    }

    private void changeWatchedButton() {
        if (watches == null) {
            watchesButton.setBackgroundColor(getResources().getColor(R.color.green));
            watchesButton.setText("Mark Watched");
            languageEntry.setText("");
            ratingEntry.setText("");
        } else {
            watchesButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            watchesButton.setText("Mark Unwatched");
            if (watches != null) {
                languageEntry.setText(watches.getViewingLanguage());
                ratingEntry.setText(Float.toString(watches.getUserRating()));
            }
        }
    }

    public void markWatched(View view) {
        hideKeyboard(this);

        if (watches == null) {
            // Mark show as watched
            ArrayList<String> params = new ArrayList<>();
            params.add("POST");
            params.add("update_watches");

            String language = languageEntry.getText().toString();
            String rating = ratingEntry.getText().toString();
            if (!username.equals("")) params.add("username=" + username);
            if (!language.equals("")) params.add("language=" + language);
            if (!rating.equals("")) params.add("rating=" + rating);
            params.add("contentId=" + content.getContentId());

            String[] paramsArr = new String[params.size()];
            paramsArr = params.toArray(paramsArr);
            NetflixAsyncTask asyncTask = new NetflixAsyncTask();
            asyncTask.contentDetailActivity = this;
            asyncTask.execute(paramsArr);
        } else {
            // Mark show as not watched
            ArrayList<String> params = new ArrayList<>();
            params.add("POST");
            params.add("delete_watches");

            if (!username.equals("")) params.add("username=" + username);
            params.add("contentId=" + content.getContentId());

            String[] paramsArr = new String[params.size()];
            paramsArr = params.toArray(paramsArr);
            NetflixAsyncTask asyncTask = new NetflixAsyncTask();
            asyncTask.contentDetailActivity = this;
            asyncTask.execute(paramsArr);
        }
    }

    public void handleAddResponse(SimpleResponse response) {
        if (response.getStatusCode() == 200) {
            Toast.makeText(this, "Show added to your viewing history!", Toast.LENGTH_LONG).show();
            finish();
        } else if (response.getStatusCode() != 201) { // status code 201 means watches table was updated
            Toast.makeText(this, "Show could not be added. Please try again.", Toast.LENGTH_LONG).show();
        }

    }
    public void handleDeleteResponse(SimpleResponse response) {
        if (response.getStatusCode() == 200) {
            watches = null;
            changeWatchedButton();
            Toast.makeText(this, "Show removed from your viewing history!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Show could not be removed. Please try again.", Toast.LENGTH_LONG).show();
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
