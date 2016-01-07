package com.app_labs.builditbigger;


import android.app.Application;
import android.test.ApplicationTestCase;
import android.text.TextUtils;

import java.util.concurrent.CountDownLatch;

/**
 * This checks that the AsyncTask always have a joke
 */
public class EndpointsAsyncTaskTest extends ApplicationTestCase<Application> {

    CountDownLatch mCountDownLatch = null;

    String mJoke = null;
    Exception mError = null;

    public EndpointsAsyncTaskTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        mCountDownLatch = new CountDownLatch(1);
    }

    @Override
    protected void tearDown() throws Exception {
        mCountDownLatch.countDown();
    }

    public void testAsyncTask() throws InterruptedException {

        MainActivity.EndpointsAsyncTask task = new MainActivity.EndpointsAsyncTask();
        task.setListener(new MainActivity.EndPointsGetTaskListener() {
            @Override
            public void onComplete(String jokeString, Exception e) {
                mJoke = jokeString;
                mError = e;
                mCountDownLatch.countDown();
            }
        }).execute();
        mCountDownLatch.await();
        assertFalse(TextUtils.isEmpty(mJoke));

    }
}