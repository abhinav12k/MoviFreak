package com.apps.movifreak.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.apps.movifreak.Model.Movie;
import com.apps.movifreak.R;

public class DetailActivity extends AppCompatActivity {

    private TextView sampleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        sampleText = findViewById(R.id.sample_text);

        Intent incomingIntent = getIntent();

        Movie clickedMoview = (Movie) incomingIntent.getSerializableExtra("movie_details");

        sampleText.setText(clickedMoview.toString());
    }
}