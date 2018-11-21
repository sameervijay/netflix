package com.cs411.netflix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {

    ImageButton recMovie1, recMovie2, recMovie3;
    TextView friend1, friend2, friend3;
    Button browseContentBtn, deleteBtn, updateBtn, addBtn;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recMovie1 = (ImageButton) findViewById(R.id.recomMovie1ImgBtn);
        recMovie2 = (ImageButton) findViewById(R.id.recomMovie2ImgBtn);
        recMovie3 = (ImageButton) findViewById(R.id.recomMovie3ImgBtn);

        friend1 = (TextView) findViewById(R.id.friend1TextViewDash);
        friend2 = (TextView) findViewById(R.id.friend2TextViewDash);
        friend3 = (TextView) findViewById(R.id.friend3TextViewDash);

        browseContentBtn = (Button) findViewById(R.id.browseContentDash);
        deleteBtn = (Button) findViewById(R.id.deleteButtonDash);
        updateBtn = (Button) findViewById(R.id.updateMovieDash);
        addBtn = (Button) findViewById(R.id.addMovieDash);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
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
                startActivity(intent);
            }
        });

        

    }
}
