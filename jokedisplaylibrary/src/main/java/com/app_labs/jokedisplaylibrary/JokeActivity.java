package com.app_labs.jokedisplaylibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    public static final String EXTRA_JOKE = "EXTRA_JOKE_THING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        TextView textViewJoke = (TextView) findViewById(R.id.textViewJoke);
        Intent intent = getIntent();
        String joke = intent.getStringExtra(EXTRA_JOKE);

        textViewJoke.setText(joke);
    }
}
