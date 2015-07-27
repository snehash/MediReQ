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


public class PastConditionsActivity extends Activity {
    private BackgroundService mBoundService;
    private boolean mIsBound;
    private Profile profile;
    private CheckBox hospitalized, hep, hepA, hepB, std, heartdisease, highcol, highbp, lowbp,
    heartburn, anemia, ankles, breath, asthma, cough, sinus, seasonal, allergies, tonsil, ear,
    eye, seizure, stroke, headache, neuro, depression, psych, diabetes, kidney, liver,
    arthritis, cancer, ulcer, thyroid;
    private EditText details;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_conditions);
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
        hospitalized = (CheckBox) findViewById(R.id.checkbox_hospitalized);
        hep = (CheckBox) findViewById(R.id.checkbox_hep);
        hepA = (CheckBox) findViewById(R.id.checkbox_hepA);
        hepB = (CheckBox) findViewById(R.id.checkbox_hepB);
        std = (CheckBox) findViewById(R.id.checkbox_std);
        heartdisease = (CheckBox) findViewById(R.id.checkbox_heartdisease);
        highcol = (CheckBox) findViewById(R.id.checkbox_highchol);
        highbp = (CheckBox) findViewById(R.id.checkbox_highbp);
        lowbp = (CheckBox) findViewById(R.id.checkbox_lowbp);
        heartburn = (CheckBox) findViewById(R.id.checkbox_heartburn);
        anemia = (CheckBox) findViewById(R.id.checkbox_anemia);
        ankles = (CheckBox) findViewById(R.id.checkbox_swollenankles);
        breath = (CheckBox) findViewById(R.id.checkbox_shortbreath);
        asthma = (CheckBox) findViewById(R.id.checkbox_asthma);
        cough = (CheckBox) findViewById(R.id.checkbox_cough);
        sinus = (CheckBox) findViewById(R.id.checkbox_sinus);
        seasonal = (CheckBox) findViewById(R.id.checkbox_seasonalallergies);
        allergies = (CheckBox) findViewById(R.id.checkbox_otherallergies);
        tonsil = (CheckBox) findViewById(R.id.checkbox_tonsillitis);
        ear = (CheckBox) findViewById(R.id.checkbox_earproblem);
        eye = (CheckBox) findViewById(R.id.checkbox_eyedisorder);
        seizure = (CheckBox) findViewById(R.id.checkbox_seizures);
        stroke = (CheckBox) findViewById(R.id.checkbox_stroke);
        headache = (CheckBox) findViewById(R.id.checkbox_headache);
        neuro = (CheckBox) findViewById(R.id.checkbox_neuro);
        depression = (CheckBox) findViewById(R.id.checkbox_depression);
        psych = (CheckBox) findViewById(R.id.checkbox_psych);
        diabetes = (CheckBox) findViewById(R.id.checkbox_diabetes);
        kidney = (CheckBox) findViewById(R.id.checkbox_kidney);
        liver = (CheckBox) findViewById(R.id.checkbox_liver);
        arthritis = (CheckBox) findViewById(R.id.checkbox_arthitis);
        cancer = (CheckBox) findViewById(R.id.checkbox_cancer);
        ulcer = (CheckBox) findViewById(R.id.checkbox_ulcer);
        thyroid = (CheckBox) findViewById(R.id.checkbox_thyroid);
        details = (EditText) findViewById(R.id.edit_past_details);
        mButton = (Button) findViewById(R.id.bttn_save_past);

        hospitalized.setChecked(profile.hospitalized);
        hep.setChecked(profile.heptest);
        hepA.setChecked(profile.hepA);
        hepB.setChecked(profile.hepB);
        std.setChecked(profile.std);
        heartdisease.setChecked(profile.heartDisease);
        highcol.setChecked(profile.highcholest);
        highbp.setChecked(profile.highbp);
        lowbp.setChecked(profile.lowbp);
        heartburn.setChecked(profile.hearburn);
        anemia.setChecked(profile.anemia);
        ankles.setChecked(profile.swollenankles);
        breath.setChecked(profile.shortbreath);
        asthma.setChecked(profile.asthma);
        cough.setChecked(profile.lungprobs);
        sinus.setChecked(profile.sinus);
        seasonal.setChecked(profile.seasonalAllergies);
        allergies.setChecked(profile.otherAllergies);
        tonsil.setChecked(profile.tonsillitis);
        ear.setChecked(profile.earprob);
        eye.setChecked(profile.eye);
        seizure.setChecked(profile.seizure);
        stroke.setChecked(profile.stroke);
        headache.setChecked(profile.headache);
        neuro.setChecked(profile.neuro);
        depression.setChecked(profile.depression);
        psych.setChecked(profile.psych);
        diabetes.setChecked(profile.diabetes);
        kidney.setChecked(profile.kidney);
        liver.setChecked(profile.liver);
        arthritis.setChecked(profile.arthritis);
        cancer.setChecked(profile.cancer);
        ulcer.setChecked(profile.ulcer);
        thyroid.setChecked(profile.thyroid);
        details.setText(profile.pastConddetails);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospitalized.setChecked(profile.hospitalized);

                profile.heptest = hep.isChecked();
                profile.hepA = hepA.isChecked();
                profile.hepB = hepB.isChecked();
                profile.std = std.isChecked();
                profile.heartDisease = heartdisease.isChecked();
                profile.highcholest = highcol.isChecked();
                profile.highbp = highbp.isChecked();
                profile.lowbp = lowbp.isChecked();
                profile.hearburn = heartburn.isChecked();
                profile.anemia = anemia.isChecked();
                profile.swollenankles = ankles.isChecked();
                profile.shortbreath = breath.isChecked();
                profile.asthma = asthma.isChecked();
                profile.lungprobs = cough.isChecked();
                profile.sinus = sinus.isChecked();
                profile.seasonalAllergies = seasonal.isChecked();
                profile.otherAllergies = allergies.isChecked();
                profile.tonsillitis = tonsil.isChecked();
                profile.earprob = ear.isChecked();
                profile.eye = eye.isChecked();
                profile.seizure = seizure.isChecked();
                profile.stroke = stroke.isChecked();
                profile.headache = headache.isChecked();
                profile.neuro = neuro.isChecked();
                profile.depression = depression.isChecked();
                profile.psych = psych.isChecked();
                profile.diabetes = diabetes.isChecked();
                profile.kidney = kidney.isChecked();
                profile.liver = liver.isChecked();
                profile.arthritis = arthritis.isChecked();
                profile.cancer = cancer.isChecked();
                profile.ulcer = ulcer.isChecked();
                profile.thyroid = thyroid.isChecked();
                profile.pastConddetails = details.getText().toString();

                mBoundService.saveProfile(filename, profile);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_past_conditions, menu);
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
        bindService(new Intent(PastConditionsActivity.this, BackgroundService.class), mConnection, 0);
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
