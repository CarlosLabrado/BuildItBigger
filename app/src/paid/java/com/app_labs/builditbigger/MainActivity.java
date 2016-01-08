package com.app_labs.builditbigger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app_labs.backend.myApi.MyApi;
import com.app_labs.jokedisplaylibrary.JokeActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EndpointsAsyncTask task = new EndpointsAsyncTask();
                task.setListener(new EndPointsGetTaskListener() {
                    @Override
                    public void onComplete(String jokeString, Exception e) {
                        callsToJokeDisplayLibrary(jokeString);
                    }
                }).execute();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callsToJokeDisplayLibrary(String joke) {
        Intent intent = new Intent(MainActivity.this, JokeActivity.class);
        intent.putExtra(JokeActivity.EXTRA_JOKE, joke);

        startActivity(intent);

    }

    public static class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
        private MyApi myApiService = null;
        private EndPointsGetTaskListener mListener = null;
        private Exception mError = null;

        @Override
        protected String doInBackground(Void... params) {
            if(myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl("http://10.0.3.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }

            try {
                return myApiService.tellJoke().execute().getData();
            } catch (IOException e) {
                mError = e;
                return e.getMessage();
            }
        }

        /**
         * Using the listener pattern to help testing
         *
         * @param listener we use this to call onComplete
         * @return listener
         */
        public EndpointsAsyncTask setListener(EndPointsGetTaskListener listener) {
            this.mListener = listener;
            return this;
        }

        @Override
        protected void onPostExecute(String result) {
            if (this.mListener != null) {
                this.mListener.onComplete(result, mError);
            }
        }

        @Override
        protected void onCancelled() {
            if (this.mListener != null) {
                mError = new InterruptedException("AsyncTask cancelled");
                this.mListener.onComplete(null, mError);
            }
        }

    }

    public static interface EndPointsGetTaskListener {
        public void onComplete(String jokeString, Exception e);
    }

}
