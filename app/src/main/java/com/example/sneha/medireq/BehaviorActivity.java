package com.example.sneha.medireq;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class BehaviorActivity extends Activity {

    private BackgroundService mBoundService;
    private boolean mIsBound;
    private Profile profile;
    private CheckBox alcohol, coffee, tobacco, exercise;
    private Button mSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        Intent intent = new Intent(this, BackgroundService.class);
        if (!BackgroundService.STARTED) {
            startService(intent);
        }
        doBindService();
    }

    private void init(){
        Intent intent = getIntent();
        final String filename = intent.getStringExtra(NavigationDrawer.PROFILE);
        profile = mBoundService.profiles.get(filename);
        alcohol = (CheckBox) findViewById(R.id.checkbox_alcohol);
        System.out.println(alcohol);
        System.out.println(profile.alcohol);
        tobacco = (CheckBox) findViewById(R.id.checkbox_tobacco);
        coffee = (CheckBox) findViewById(R.id.checkbox_coffee);
        exercise = (CheckBox) findViewById(R.id.checkbox_exercise);
        mSave = (Button) findViewById(R.id.bttn_save_behavior);
        alcohol.setChecked(profile.alcohol);
        tobacco.setChecked(profile.tobacco);
        coffee.setChecked(profile.coffee);
        exercise.setChecked(profile.exercise);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.alcohol = alcohol.isChecked();
                profile.tobacco = tobacco.isChecked();
                profile.coffee = coffee.isChecked();
                profile.exercise = exercise.isChecked();
                mBoundService.saveProfile(filename, profile);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medical_allergies, menu);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsBound) {
            doUnbindService();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((BackgroundService.LocalBinder)service).getService();
            init();
        }

        public void onServiceDisconnected(ComponentName className){ mBoundService = null;}

    };

    private void doBindService(){
        bindService(new Intent(BehaviorActivity.this, BackgroundService.class), mConnection, 0);
        mIsBound = true;
    }

    private void doUnbindService(){
        if (mIsBound){
            unbindService(mConnection);
            mIsBound = false;
        }
    }
}
