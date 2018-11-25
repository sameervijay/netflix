package com.cs411.netflix;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.security.spec.ECField;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    ImageButton recMovie1, recMovie2, recMovie3;
    TextView friend1, friend2, friend3;
    Button browseContentBtn, deleteBtn, updateBtn, addBtn;

    //movie_info dialog views
    //RatingBar stars;
    //ImageView tnViewMD;
    //TextView durationView, dateView, titleView, directorView;



    String username;
    //TODO: this ArrayList will initially contain the user's top3 movies until recommendation functionality is impl.ed
    ArrayList<Integer> recommendedMovies; //populated in handleRecomMovieResponse
    //make array of ImageButtons to set the correct image when retrieving thumbnails
    ImageButton[] imgBtnRefs = new ImageButton[3];
    String ThumbnailURL;
    //save thumbnail urls here
    String[] urls = new String[3];
    //array of strings that contains the movie info data that will be supplied to the movie_info dialog on image button click
    String[] movie_data = new String[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recMovie1 = (ImageButton) findViewById(R.id.recomMovie1ImgBtn);
        recMovie2 = (ImageButton) findViewById(R.id.recomMovie2ImgBtn);
        recMovie3 = (ImageButton) findViewById(R.id.recomMovie3ImgBtn);

        //initially make the buttons unclickable and only re-enable them after the thumbnail querying is complete
        recMovie1.setEnabled(false);
        recMovie2.setEnabled(false);
        recMovie3.setEnabled(false);

        imgBtnRefs[0] = recMovie1;
        imgBtnRefs[1] = recMovie2;
        imgBtnRefs[2] = recMovie3;

        for(int i = 0; i < 3; i++){
            imgBtnRefs[i].setImageResource(R.drawable.blank_movie_thumbnail);
            urls[i] = "default";
        }

        friend1 = (TextView) findViewById(R.id.friend1TextViewDash);
        friend2 = (TextView) findViewById(R.id.friend2TextViewDash);
        friend3 = (TextView) findViewById(R.id.friend3TextViewDash);

        browseContentBtn = (Button) findViewById(R.id.browseContentDash);
        deleteBtn = (Button) findViewById(R.id.deleteButtonDash);
        updateBtn = (Button) findViewById(R.id.updateMovieDash);
        addBtn = (Button) findViewById(R.id.addMovieDash);

        //movie_info dialog views
        //stars = (RatingBar) findViewById(R.id.ratingBarMD);
        //tnViewMD = (ImageView) findViewById(R.id.thumbnailViewMD);
        //durationView = (TextView) findViewById(R.id.durationViewMD);
        //dateView = (TextView) findViewById(R.id.releaseDateViewMD);
        //titleView = (TextView) findViewById(R.id.movieTitleViewMD);
        //directorView = (TextView) findViewById(R.id.directorViewMD);

        recommendedMovies = new ArrayList<Integer>();
        ThumbnailURL = "default";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        browseContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, SearchContentActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DeleteFromWatches.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, activity_update_watches.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, AddToWatches.class);
                intent.putExtra("username", username);
                intent.putExtra("movie_name", "");
                startActivity(intent);
            }
        });

        //get images for recommended movies
        //TODO: note that the below methods currently pull the top3 rated movies for the user
        getRecommendedMovies();

        //set onClickListener for each of the imageButtons to direct to movie dialog
        //do not open dialog if no contentId associated (i.e. if movieIdx < recommendedMovies.size())
        recMovie1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: put movie retrieval script call here (prob just use search_content)
                if(recommendedMovies==null || recommendedMovies.size() == 0) {
                    return;
                }
                Log.d("recmovie1 onclick", "got past the first null check");
                int contentId = recommendedMovies.get(0);

                searchForMovie(Integer.toString(contentId), "0");

                //TODO: move all of below code into responseHandler
                //This is such a f*cked way of doing this. each activity should just have its own async task, but its too late now

                //TODO: right now it queues every time, but to improve performance consider making the movie_data array a 2D array that contains
                // 3x the space so that we can just set the movie data once, and if it has already been populated once, then we don't need to query again,
                // and can just open the dialog from here

            }
        });

        recMovie2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: put movie retrieval script call here (prob just use search_content)
                if(recommendedMovies==null || recommendedMovies.size() < 2) {
                    return;
                }
                int contentId = recommendedMovies.get(1);

                //we use waitRes here to force the main thread to wait for the async task to complete before proceeding
                searchForMovie(Integer.toString(contentId), "1");

                //TODO: right now it queues every time, but to improve performance consider making the movie_data array a 2D array that contains
                // 3x the space so that we can just set the movie data once, and if it has already been populated once, then we don't need to query again,
                // and can just open the dialog from here

            }
        });

        recMovie3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: put movie retrieval script call here (prob just use search_content)
                if(recommendedMovies==null || recommendedMovies.size() < 3) {
                    return;
                }
                int contentId = recommendedMovies.get(2);

                //we use waitRes here to force the main thread to wait for the async task to complete before proceeding
                searchForMovie(Integer.toString(contentId), "2");

                //TODO: right now it queues every time, but to improve performance consider making the movie_data array a 2D array that contains
                // 3x the space so that we can just set the movie data once, and if it has already been populated once, then we don't need to query again,
                // and can just open the dialog from here

            }
        });


        //set friend text views as clickable if friend is able to be pulled for that view. if not, then set not clickable
        //friend1.setClickable(true); friend2.setClickable(true); friend3.setClickable(true);
        //if no friend available to populate
        //friend1.setClickable(false); friend2.setClickable(false); friend3.setClickable(false);

        //for creating alert dialog like in PrimaryLoginActivity but with custom xml see:
        //https://stackoverflow.com/questions/4279787/how-can-i-pass-values-between-a-dialog-and-an-activity
        //find way to dismiss dialog on dialog button click (probably just need to extract onclick method and make dialog final)
        //https://stackoverflow.com/questions/5713312/closing-a-custom-alert-dialog-on-button-click


    }

    /**
     *  This method handles the querying for the movie image buttons and should be called in onCreate
     *  TODO: note that this activity currently implements the user's top 3 rated movies rather than the actual recommended movies since that functionality has yet to be implemented to the database
     *  TODO: update this method to call the correct php script once it has been written
     *  It currently queries the database for the user's top 3 movies by contentId and returns it as an array
     *  where we will handle it in handleRecomMovieResponse below
     *  It will call NetflixAsyncTask
     * */
    public void getRecommendedMovies(){
        NetflixAsyncTask task = new NetflixAsyncTask();
        task.dashboardActivity = this;

        ArrayList<String> params = new ArrayList<>();
        params.add("GET");
        params.add("get_top3movies_watches)");
        params.add(username);
        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);
        Log.d("DashActvty:getRecMovies", "searching for highest rated movies");
        task.execute(paramsArr);
    }

    /**
     *  This method handles the getRecommendedMovies query response and should be called in onCreate
     *  It populates an array list of the contentIds (ints) for each of the top3 movies that were retrieved
     * */
    public void handleRecomMovieResponse(ContentIdList clist){
        if(clist==null) return;
        ArrayList<Top3Content> contentId = clist.getContentList();
        if(contentId==null) return;
        for(int i = 0; i < contentId.size(); i++){
            try {
                recommendedMovies.add(Integer.parseInt(contentId.get(i).getContentId()));
            }catch(NumberFormatException n){
                //if returned string is not an integer then this is a major issue
                n.printStackTrace();
                Log.e("handleRecMovie:Dash", "contentId string was not an int. this is a major problem; attempted parse: " + contentId.get(i).getContentId());
            }
        }

        //recommendedMovies should now be populated with no more than 3 contentIds
        if (recommendedMovies != null) {
            int size = recommendedMovies.size();
            if (size > 3) {
                Log.e("Dash:onCreate", "somehow recommended movies has more than 3 contentIds which is problematic.");
                for (int i = 0; i < size; i++) {
                    Log.e("Dash:onCreate:" + i, "ContentId: " + recommendedMovies.get(i));
                }
            }
            //need to retrieve thumbnail image links for retrieved movies (movie1 = index 0, movie2 = index 1, movie3 = index 2)
            for(int i = 0; i < size; i++){
                String currContentId = recommendedMovies.get(i).toString();
                getThumbnails(currContentId, Integer.toString(i));
            }

        }
    }

    /**
     * This method calls the script that retrieves a thumbnail image link from the ContentMeta table given a contentId
     * The method will cal NetflixAsyncTask
     * Will also take a param i which just needs to be passed on to the response for setting the resource images
     * */
    public void getThumbnails(String contentid, String i){
        NetflixAsyncTask task = new NetflixAsyncTask();
        task.dashboardActivity = this;

        ArrayList<String> params = new ArrayList<>();
        params.add("GET");
        params.add("get_movie_thumbnail]");
        params.add(contentid);
        params.add(i);
        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);
        Log.d("Dash:getThumbnails", "retrieving thumbnail image");
        task.execute(paramsArr);
    }

    /**
     * This method handles the getThumbnails query response and should be called in onCreate
     * It sets a public variable ThumbnailURL
     * */
    public void handleThumbnailResponse(SimpleResponseThumbnail response, int i){
        ThumbnailURL = response.getResponse().getThumbnail();
        if(response.getStatusCode() != 200){
            Log.e("Dash:handleTNR", response.getResponse().getThumbnail());
            ThumbnailURL = "";
        }
        //assume that ThumbnailURL has been appropriately set by now (i.e. the async task completed)
        if(ThumbnailURL.equals("")){
            //insert default image (or rather change the current default image to something else
            imgBtnRefs[i].setImageResource(R.drawable.default_movie_thumbnail);
            urls[i] = "";
        }
        else{
            //show retrieved image
            urls[i] = ThumbnailURL;
            new DownloadImageTask((ImageButton) imgBtnRefs[i], null).execute(ThumbnailURL);
        }
        if(i==0) recMovie1.setEnabled(true);
        else if(i==1) recMovie2.setEnabled(true);
        else if(i==2) recMovie3.setEnabled(true);
    }

    /**
     * This method calls the search content script and retrieves the movie data for the movie_info dialog
     *  given the contentId
     *  param i is the index that we hand over to the response handler for displaying the dialog
     * */
    public void searchForMovie(String contentId, String i){
        NetflixAsyncTask asyncTask = new NetflixAsyncTask();
        asyncTask.dashboardActivity = this;
        ArrayList<String> params = new ArrayList<>();
        params.add("GET");
        params.add("search_content_id+");
        params.add(contentId);
        params.add(i);
        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);
        Log.d("Dash:searchForMovie", "retrieving movie data");
        /**
        String waitResult = "";
        try{
            //NOTE: by calling get() on execute, we can effectively force the main thread to wait for doInBackground to complete
            waitResult = asyncTask.execute(paramsArr).get();
        } catch(Exception e){
            Log.e("Dash:searchForMovie", "wait failed");
            e.printStackTrace();
        }
        return waitResult;
         */
        asyncTask.execute(paramsArr);
    }

    /**
     * This method handles the searchForMovie query response and should be called in dialog creation
     * This method updates the movie_data array with the query response data
     * */
    public void handleMovieSearchResponse(ContentList contentList, int idx){
        if(contentList==null || contentList.getContentList().size()==0){
            for(int i = 0; i < movie_data.length; i++){
                movie_data[i] = "";
            }
        }
        else{
            Content content = contentList.getContentList().get(0);
            movie_data[0] = content.getContentId();
            movie_data[1] = content.getName();
            movie_data[2] = content.getGenre();
            movie_data[3] = content.getAvgRating();
            movie_data[4] = content.getReleaseDate();
            movie_data[5] = content.getLanguage();
            movie_data[6] = content.getDirector();
            movie_data[7] = content.getDuration();
        }

        //All of dialog code below. this is a really bad way of doing this but its too late to fix it now
        AlertDialog.Builder dialog = new AlertDialog.Builder(DashboardActivity.this);
        LayoutInflater inflater = LayoutInflater.from(DashboardActivity.this);
        final View customView = inflater.inflate(R.layout.movie_info_dialog, null);

        //assume that movie_data has been populated
        assert(movie_data[0]!=null);
        if(movie_data[0] == null || movie_data[0].equals("")) return;
        Log.d("recmovie1 onclick", "got past the second null check");
        if(movie_data[0].equals("")){
            Log.e("Dash:recMovie1 onclick", "response data failed to be parsed");
        }
        final String movie_title = movie_data[1];
        String tnURL = urls[idx];
        String director = movie_data[6];
        float rating;
        try {
            rating = Float.parseFloat(movie_data[3]);
        } catch(NumberFormatException ne){
            rating = 0.0f;
        }
        String duration = movie_data[7];
        String releaseDate = movie_data[4];

        RatingBar stars = (RatingBar) customView.findViewById(R.id.ratingBarMD);
        stars.setIsIndicator(true);
        stars.setRating((float)(rating/2.0));

        new DownloadImageTask(null, (ImageView) customView.findViewById(R.id.thumbnailViewMD)).execute(tnURL);

        TextView durationView = (TextView) customView.findViewById(R.id.durationViewMD);
        durationView.setText(duration + " minutes");

        TextView dateView = (TextView) customView.findViewById(R.id.releaseDateViewMD);
        dateView.setText("Release Date: " + releaseDate);

        TextView titleView = (TextView) customView.findViewById(R.id.movieTitleViewMD);
        titleView.setText(movie_title);

        TextView directorView = (TextView) customView.findViewById(R.id.directorViewMD);
        directorView.setText(director);

        final AlertDialog aDialog = dialog.setView(customView)
                .setPositiveButton("Add to Watched List", null)
                .setNegativeButton("Close", null)
                .create();
        aDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button addBtn = ((AlertDialog) aDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        aDialog.dismiss();
                        Intent intent = new Intent(DashboardActivity.this, AddToWatches.class);
                        intent.putExtra("username", username);
                        intent.putExtra("movie_name", movie_title);
                        startActivity(intent);
                    }
                });
                Button closeBtn = ((AlertDialog) aDialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        aDialog.dismiss();
                    }
                });
            }
        });

        aDialog.show();


    }

    /**
     * Private class for the purpose of retrieving image from url for imageButtons
     * From: https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
     * */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        ImageButton bmImage;
        ImageView iImage;
        public DownloadImageTask(ImageButton bmImage, ImageView iImage){
            this.bmImage = bmImage;
            this.iImage = iImage;
        }

        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            InputStream in = null;
            try{
                in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch(Exception e){
                Log.e("DownloadImageTask", e.getMessage());
                e.printStackTrace();
            } finally{
                try {
                    if (in != null) in.close();
                } catch(IOException ie){
                    Log.e("DownloadImageTask", "There was a problem closing the input stream");
                    ie.printStackTrace();
                }
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result){
            if(bmImage!=null) bmImage.setImageBitmap(result);
            if(iImage!=null) iImage.setImageBitmap(result);
        }
    }

}
