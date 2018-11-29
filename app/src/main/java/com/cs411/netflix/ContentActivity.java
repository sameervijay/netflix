package com.cs411.netflix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cs411.netflix.GsonTemplates.Content;
import com.cs411.netflix.GsonTemplates.ContentList;
import com.cs411.netflix.GsonTemplates.Watches;
import com.cs411.netflix.GsonTemplates.WatchesList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentActivity extends AppCompatActivity {
    private EditText nameEntry, genreEntry, directorEntry, languageEntry, ratingEntry;
    private Map<Integer, Watches> watches = new HashMap<>();
    private List<Content> contentList = new ArrayList<>();
    private ContentAdapter adapter;
    private String username;
    private Watches currentDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        nameEntry = findViewById(R.id.contentNameEntry);
        genreEntry = findViewById(R.id.genreEntryView);
        directorEntry = findViewById(R.id.directorEntryView);
        languageEntry = findViewById(R.id.languageCD);
        ratingEntry = findViewById(R.id.ratingEntryView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            String name = extras.getString("name");
            if (name != null) {
                nameEntry.setText(name);
            }
        }
        if (!nameEntry.getText().toString().equals("")) {
            searchContent(null);
        }
        retrieveWatches(username);

        // Initializes RecyclerView with adapter
        RecyclerView mainRecyclerView = findViewById(R.id.contentRecyclerView);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContentAdapter(this);
        mainRecyclerView.setAdapter(adapter);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*
     * Hits get_watches.php and retrieves all the shows watched by the user
     */
    public void retrieveWatches(String username) {
        if (username == null) return;

        ArrayList<String> params = new ArrayList<>();
        params.add("GET");
        params.add("get_watches");
        if (!username.equals("")) params.add("username=" + username);
        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);

        NetflixAsyncTask asyncTask = new NetflixAsyncTask();
        asyncTask.contentActivity = this;
        asyncTask.execute(paramsArr);
    }
    public void handleWatchesResponse(WatchesList watchesList) {
        watches.clear();
        for (Watches w : watchesList.getWatches()) {
            watches.put(w.getContentId(), w);
        }
        adapter.notifyDataSetChanged();
    }
    public Watches getIfWatched(Integer contentId) {
        if (watches.containsKey(contentId)) return watches.get(contentId);
        return null;
    }

    public void handleContentResponse(ContentList list) {
        contentList = list.getContentList();
        adapter.notifyDataSetChanged();
    }

    public void searchContent(View view) {
        hideKeyboard(this);
        String name = nameEntry.getText().toString();
        String genre = genreEntry.getText().toString();
        String director = directorEntry.getText().toString();
        String language = languageEntry.getText().toString();
        String rating = ratingEntry.getText().toString();

        ArrayList<String> params = new ArrayList<>();
        params.add("GET");
        params.add("search_content");
        if (!name.equals("")) params.add("name=" + name);
        if (!genre.equals("")) params.add("genre=" + genre);
        if (!director.equals("")) params.add("director=" + director);
        if (!language.equals("")) params.add("language=" + language);
        if (!rating.equals("")) params.add("avg_rating=" + rating);

        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);

        NetflixAsyncTask asyncTask = new NetflixAsyncTask();
        asyncTask.contentActivity = this;
        asyncTask.execute(paramsArr);
    }

    public void clearFilters(View view) {
        nameEntry.setText("", TextView.BufferType.NORMAL);
        genreEntry.setText("", TextView.BufferType.NORMAL);
        directorEntry.setText("", TextView.BufferType.NORMAL);
        languageEntry.setText("", TextView.BufferType.NORMAL);
        ratingEntry.setText("", TextView.BufferType.NORMAL);
        contentList.clear();
        adapter.notifyDataSetChanged();
    }

    public void watchedContent(View view) {
        hideKeyboard(this);

        Intent detailedIntent = new Intent(this, ContentDetailActivity.class);
        detailedIntent.putExtra("username", username);

        int row = Integer.parseInt(view.getContentDescription().toString());
        if (view.getId() == R.id.changeWatchesButton1 || view.getId() == R.id.cellThumbnail1) {
            Content content = contentList.get(row * 3);
            detailedIntent.putExtra("content", content);
            Integer contentId = Integer.parseInt(content.getContentId());
            if (watches.containsKey(contentId)) {
                detailedIntent.putExtra("watches", watches.get(contentId));
                currentDetail = watches.get(contentId);
            }
        } else if (view.getId() == R.id.changeWatchesButton2 || view.getId() == R.id.cellThumbnail2) {
            Content content = contentList.get(row * 3 + 1);
            detailedIntent.putExtra("content", content);
            Integer contentId = Integer.parseInt(content.getContentId());
            if (watches.containsKey(contentId)) {
                detailedIntent.putExtra("watches", watches.get(contentId));
                currentDetail = watches.get(contentId);
            }
        } else if (view.getId() == R.id.changeWatchesButton3 || view.getId() == R.id.cellThumbnail3) {
            Content content = contentList.get(row * 3 + 2);
            detailedIntent.putExtra("content", content);
            Integer contentId = Integer.parseInt(content.getContentId());
            if (watches.containsKey(contentId)) {
                detailedIntent.putExtra("watches", watches.get(contentId));
                currentDetail = watches.get(contentId);
            }
        }
        startActivityForResult(detailedIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Watches w = extras.getParcelable("watches");
                    watches.put(w.getContentId(), w);
                    adapter.notifyDataSetChanged();
                    currentDetail = null;
                }
            }
            if (currentDetail != null) {
                watches.remove(currentDetail.getContentId());
                currentDetail = null;
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    public List<Content> getContentList() {
        return contentList;
    }

    public void setContentList(List<Content> contentList) {
        this.contentList = contentList;
    }
}