package com.example.sneha.medireq;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SurgicalHistory extends Activity {
    private BackgroundService mBoundService;
    private boolean mIsBound;
    private Profile profile;
    private EditText history;
    private Button mButton;
    private Context context;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surgical_history);
        context = this;
        Intent intent = new Intent(this, BackgroundService.class);
        if (!BackgroundService.STARTED) {
            startService(intent);
        }
        doBindService();
    }

    private void init(){
        Intent intent = getIntent();

        filename = intent.getStringExtra(NavigationDrawer.PROFILE);
        profile = mBoundService.profiles.get(filename);
        history = (EditText) findViewById(R.id.edit_surgicalhistory);
        history.setText(profile.surgicalHistory);
        mButton = (Button) findViewById(R.id.bttn_save_surgical);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.surgicalHistory = history.getText().toString();
                mBoundService.saveProfile(filename, profile);
                Toast.makeText(context, "Changes saved successfully!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_surgical_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        switch(id){
            case R.id.sh_personal:
                Intent intent_personal = new Intent(context, ContactInformationActivity.class);
                intent_personal.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_personal);
                break;
            case R.id.sh_past_cond:
                Intent intent_pastcond = new Intent(context, PastConditionsActivity.class);
                intent_pastcond.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_pastcond);
                break;
            case R.id.sh_medi_allergies:
                Intent intent_allergies = new Intent(context, MedicalAllergies.class);
                intent_allergies.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_allergies);
                break;
            case R.id.sh_behav:
                Intent intent_behav = new Intent(context, BehaviorActivity.class);
                intent_behav.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_behav);
                break;
            case R.id.sh_famhis:
                Intent intent_fam = new Intent(context, FamilyHistoryActivity.class);
                intent_fam.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_fam);
                break;

        } */

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
        bindService(new Intent(SurgicalHistory.this, BackgroundService.class), mConnection, 0);
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
