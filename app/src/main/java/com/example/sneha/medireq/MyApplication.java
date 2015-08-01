package com.example.sneha.medireq;

/**
 * Created by Sneha on 8/1/2015.
 */
import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
    public long mLastPause;

    @Override
    public void onCreate() {
        super.onCreate();
        mLastPause = 0;
        Log.w("Application", "Launch");
    }
}