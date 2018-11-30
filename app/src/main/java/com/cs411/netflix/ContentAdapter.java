package com.cs411.netflix;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cs411.netflix.GsonTemplates.Content;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.EmptyStackException;
import java.util.List;

/**
 * Created by sameervijay on 11/24/18.
 */

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {
    private LayoutInflater inflater;
    private ContentActivity contentActivity;

    public ContentAdapter(Context context) {
        contentActivity = (ContentActivity) context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    /**
     * Number of rows in the Recyclerview
     */
    public int getItemCount() {
        int count = (int)Math.ceil((double)contentActivity.getContentList().size() / 3);
        return count;
    }

    /**
     * Sets the content in the row's views to the corresponding Content in the ArrayList
     */

    @Override
    public void onBindViewHolder(ContentViewHolder viewHolder, int i) {
        List<Content> contentList = contentActivity.getContentList();
        final Content[] shows = new Content[3];
        int scaled = 3 * i;
        if (contentList.size() > scaled) shows[0] = contentList.get(scaled);
        if (contentList.size() > scaled + 1) shows[1] = contentList.get(scaled + 1);
        if (contentList.size() > scaled + 2) shows[2] = contentList.get(scaled + 2);
        setContentViews(viewHolder, shows);

        viewHolder.watchButton1.setContentDescription(Integer.toString(i));
        viewHolder.watchButton2.setContentDescription(Integer.toString(i));
        viewHolder.watchButton3.setContentDescription(Integer.toString(i));
        viewHolder.imgButton1.setContentDescription(Integer.toString(i));
        viewHolder.imgButton2.setContentDescription(Integer.toString(i));
        viewHolder.imgButton3.setContentDescription(Integer.toString(i));
    }

    private void changeWatchedButton(Button button, boolean watched) {
        if (watched) {
            button.setTextColor(contentActivity.getResources().getColor(R.color.green));
            button.setText("Watched");
        } else {
            button.setTextColor(contentActivity.getResources().getColor(R.color.red));
            button.setText("Not watched");
        }
    }

    private void setContentViews(final ContentViewHolder viewHolder, Content[] shows) {
        Content content1 = shows[0];
        Content content2 = shows[1];
        Content content3 = shows[2];

        final Context context = viewHolder.itemView.getContext();
        if (content1 != null) {
            viewHolder.nameView1.setVisibility(View.VISIBLE);
            viewHolder.watchButton1.setVisibility(View.VISIBLE);
            viewHolder.imgButton1.setVisibility(View.VISIBLE);
            viewHolder.nameView1.setText(content1.getName());
            //Log.d("ContentAdapter", "content1 not null");
            if (content1.getThumbnail() != null && !content1.getThumbnail().isEmpty()) {
                Log.d("ContentAdapter", "content1 thumbnail exists");
                Picasso.with(context).load(content1.getThumbnail()).resize(2000, 2960).onlyScaleDown().into(viewHolder.imgButton1);
                //Picasso.with(context).load(content1.getThumbnail()).into(viewHolder.imgButton1);
            }

            changeWatchedButton(viewHolder.watchButton1,
                    contentActivity.getIfWatched(Integer.parseInt(content1.getContentId())) != null);
        } else {
            viewHolder.nameView1.setVisibility(View.INVISIBLE);
            viewHolder.watchButton1.setVisibility(View.INVISIBLE);
            viewHolder.imgButton1.setVisibility(View.INVISIBLE);
        }

        if (content2 != null) {
            viewHolder.nameView2.setVisibility(View.VISIBLE);
            viewHolder.watchButton2.setVisibility(View.VISIBLE);
            viewHolder.imgButton2.setVisibility(View.VISIBLE);
            viewHolder.nameView2.setText(content2.getName());
            if (content2.getThumbnail() != null && !content2.getThumbnail().isEmpty()) {
                Log.d("ContentAdapter", "content2 thumbnail exists");
                //Picasso.with(context).load(content2.getThumbnail()).into(viewHolder.imgButton2);
                Picasso.with(context).load(content2.getThumbnail()).resize(2000, 2960).onlyScaleDown().into(viewHolder.imgButton2);
            }
            changeWatchedButton(viewHolder.watchButton2,
                    contentActivity.getIfWatched(Integer.parseInt(content2.getContentId())) != null);
        } else {
            viewHolder.nameView2.setVisibility(View.INVISIBLE);
            viewHolder.watchButton2.setVisibility(View.INVISIBLE);
            viewHolder.imgButton2.setVisibility(View.INVISIBLE);
        }

        if (content3 != null) {
            viewHolder.nameView3.setVisibility(View.VISIBLE);
            viewHolder.watchButton3.setVisibility(View.VISIBLE);
            viewHolder.imgButton3.setVisibility(View.VISIBLE);
            viewHolder.nameView3.setText(content3.getName());
            if (content3.getThumbnail() != null && !content3.getThumbnail().isEmpty()) {
                Log.d("ContentAdapter", "content3 thumbnail exists");
                //Picasso.with(context).load(content3.getThumbnail()).into(viewHolder.imgButton3);
                Picasso.with(context).load(content3.getThumbnail()).resize(2000, 2960).onlyScaleDown().into(viewHolder.imgButton3);
            }
            changeWatchedButton(viewHolder.watchButton3,
                    contentActivity.getIfWatched(Integer.parseInt(content3.getContentId())) != null);
        } else {
            viewHolder.nameView3.setVisibility(View.INVISIBLE);
            viewHolder.watchButton3.setVisibility(View.INVISIBLE);
            viewHolder.imgButton3.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Inflates the ContentViewHolder
     */
    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = this.inflater.inflate(R.layout.content_row, viewGroup, false);
        return new ContentViewHolder(itemView);
    }

    /**
     * Class for a container view of a single row in the RecyclerView
     */
    public class ContentViewHolder extends RecyclerView.ViewHolder {
        private View containerView;
        private ImageButton imgButton1, imgButton2, imgButton3;
        private Button watchButton1, watchButton2, watchButton3;
        private TextView nameView1, nameView2, nameView3;

        public ContentViewHolder(View view) {
            super(view);
            this.containerView = view;

            // Retrieves all views in the row by ID
            this.imgButton1 = view.findViewById(R.id.cellThumbnail1);
            this.imgButton2 = view.findViewById(R.id.cellThumbnail2);
            this.imgButton3 = view.findViewById(R.id.cellThumbnail3);
            this.watchButton1 = view.findViewById(R.id.changeWatchesButton1);
            this.watchButton2 = view.findViewById(R.id.changeWatchesButton2);
            this.watchButton3 = view.findViewById(R.id.changeWatchesButton3);
            this.nameView1 = view.findViewById(R.id.cellNameView1);
            this.nameView2 = view.findViewById(R.id.cellNameView2);
            this.nameView3 = view.findViewById(R.id.cellNameView3);
        }
    }
}
