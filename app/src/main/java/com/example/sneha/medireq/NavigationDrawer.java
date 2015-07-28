package com.example.sneha.medireq;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class NavigationDrawer extends Activity {
    public static String PROFILE = "com.example.MediReQ.profile";
    public static int UPDATEPROFILE = 6;

    private String[] categories;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private Context context;
    private Profile profile;
    private BackgroundService mBoundService;
    private boolean mIsBound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
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

        mListView = (ListView) findViewById(R.id.lv_categories);
        context = this;
        categories = getResources().getStringArray(R.array.categories);
        mAdapter = new ArrayAdapter<String>(this, R.layout.mytextview,categories);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = categories[position];
                switch(selection) {
                    case "Personal":
                        Intent intent_personal = new Intent(context, ContactInformationActivity.class);
                        intent_personal.putExtra(PROFILE, filename);
                        startActivity(intent_personal);
                        break;
                    case "Past Conditions":
                        Intent intent_pastcond = new Intent(context, PastConditionsActivity.class);
                        intent_pastcond.putExtra(PROFILE, filename);
                        startActivity(intent_pastcond);
                        break;
                    case "Surgical History":
                        Intent intent_surg = new Intent(context, SurgicalHistory.class);
                        intent_surg.putExtra(PROFILE, filename);
                        startActivity(intent_surg);
                        break;
                    case "Medical Allergies":
                        Intent intent_allergies = new Intent(context, MedicalAllergies.class);
                        intent_allergies.putExtra(PROFILE, filename);
                        startActivity(intent_allergies);
                        break;
                    case "Behavior":
                        Intent intent_behav = new Intent(context, BehaviorActivity.class);
                        intent_behav.putExtra(PROFILE, filename);
                        startActivity(intent_behav);
                        break;
                    case "Family History":
                        Intent intent_fam = new Intent(context, FamilyHistoryActivity.class);
                        intent_fam.putExtra(PROFILE, filename);
                        startActivity(intent_fam);
                        break;

                }


            }
        });


    }


    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation_drawer, menu);
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



    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((BackgroundService.LocalBinder)service).getService();
            init();
        }

        public void onServiceDisconnected(ComponentName className){ mBoundService = null;}

    };

    private void doBindService(){
        bindService(new Intent(NavigationDrawer.this, BackgroundService.class), mConnection, 0);
        mIsBound = true;
    }

    private void doUnbindService(){
        if (mIsBound){
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsBound) {
            doUnbindService();
        }
    }
}
