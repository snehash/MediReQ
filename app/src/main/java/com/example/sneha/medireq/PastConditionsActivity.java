package com.example.sneha.medireq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;


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
    private Context context;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_conditions);
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
        getActionBar().setTitle(profile.name);
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
                profile.hospitalized = hospitalized.isChecked();
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
                Toast.makeText(context, "Changes saved successfully!", Toast.LENGTH_SHORT).show();

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
        /*
        switch(id){
            case R.id.pc_personal:
                Intent intent_personal = new Intent(context, ContactInformationActivity.class);
                intent_personal.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_personal);
                break;
            case R.id.pc_surgical_history:
                Intent intent_surg = new Intent(context, SurgicalHistory.class);
                intent_surg.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_surg);
                break;
            case R.id.pc_medi_allergies:
                Intent intent_allergies = new Intent(context, MedicalAllergies.class);
                intent_allergies.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_allergies);
                break;
            case R.id.pc_behav:
                Intent intent_behav = new Intent(context, BehaviorActivity.class);
                intent_behav.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_behav);
                break;
            case R.id.pc_famhis:
                Intent intent_fam = new Intent(context, FamilyHistoryActivity.class);
                intent_fam.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_fam);
                break;

        } */

        return super.onOptionsItemSelected(item);
    }

    public void setChecked(View view){

        switch(view.getId()){
            case R.id.hospitalized:
                hospitalized.setChecked(!hospitalized.isChecked());
                break;
            case R.id.hep:
                hep.setChecked(!hep.isChecked());
                break;
            case R.id.hepA:
                hepA.setChecked(!hepA.isChecked());
                break;
            case R.id.hepB:
                hepB.setChecked(!hepB.isChecked());
                break;
            case R.id.std:
                std.setChecked(!std.isChecked());
                break;
            case R.id.heartdisease:
                heartdisease.setChecked(!heartdisease.isChecked());
                break;
            case R.id.highchol:
                highcol.setChecked(!highcol.isChecked());
                break;
            case R.id.highbp:
                highbp.setChecked(!highbp.isChecked());
                break;
            case R.id.lowbp:
                lowbp.setChecked(!lowbp.isChecked());
                break;
            case R.id.heartburn:
                heartburn.setChecked(!heartburn.isChecked());
                break;
            case R.id.anemia:
                anemia.setChecked(!anemia.isChecked());
                break;
            case R.id.swollenankles:
                ankles.setChecked(!ankles.isChecked());
                break;
            case R.id.shortbreath:
                breath.setChecked(!breath.isChecked());
                break;
            case R.id.asthma:
                asthma.setChecked(!asthma.isChecked());
                break;
            case R.id.cough:
                cough.setChecked(!cough.isChecked());
                break;
            case R.id.sinus:
                sinus.setChecked(!sinus.isChecked());
                break;
            case R.id.seasonalallergies:
                seasonal.setChecked(!seasonal.isChecked());
                break;
            case R.id.otherallergies:
                allergies.setChecked(!allergies.isChecked());
                break;
            case R.id.tonsils:
                tonsil.setChecked(!tonsil.isChecked());
                break;
            case R.id.earproblems:
                ear.setChecked(!ear.isChecked());
                break;
            case R.id.eyedisorder:
                eye.setChecked(!eye.isChecked());
                break;
            case R.id.seizures:
                seizure.setChecked(!seizure.isChecked());
                break;
            case R.id.stroke:
                stroke.setChecked(!stroke.isChecked());
                break;
            case R.id.headache:
                headache.setChecked(!headache.isChecked());
                break;
            case R.id.neuro:
                neuro.setChecked(!neuro.isChecked());
                break;
            case R.id.depression:
                depression.setChecked(!depression.isChecked());
                break;
            case R.id.psych:
                psych.setChecked(!psych.isChecked());
                break;
            case R.id.diabetes:
                diabetes.setChecked(!diabetes.isChecked());
                break;
            case R.id.kidney:
                kidney.setChecked(!kidney.isChecked());
                break;
            case R.id.liver:
                liver.setChecked(!liver.isChecked());
                break;
            case R.id.arthitis:
                arthritis.setChecked(!arthritis.isChecked());
                break;
            case R.id.cancer:
                cancer.setChecked(!cancer.isChecked());
                break;
            case R.id.thyroid:
                thyroid.setChecked(!thyroid.isChecked());
                break;
            case R.id.ulcer:
                ulcer.setChecked(!ulcer.isChecked());
                break;

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
    public void onPause() {
        super.onPause();
        ((MyApplication)this.getApplication()).mLastPause = System.currentTimeMillis();
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication app = ((MyApplication)this.getApplication());
        if (System.currentTimeMillis() - app.mLastPause > 5000) {
            final ScrollView sv = (ScrollView) findViewById(R.id.pastcond_sv);
            sv.setVisibility(View.INVISIBLE);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            final String pwd = pref.getString("password", "");
            if (pwd.length() > 0) {
                LayoutInflater inflater=PastConditionsActivity.this.getLayoutInflater();
                final View layout=inflater.inflate(R.layout.password, null);
                final AlertDialog d = new AlertDialog.Builder(context)
                        .setView(layout)
                        .setTitle("Enter Password")
                        .setPositiveButton(android.R.string.ok, null)
                        .setCancelable(false)
                                //.setNegativeButton(android.R.string.cancel, null)
                        .create();

                d.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {

                        Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                EditText new_password=(EditText)layout.findViewById(R.id.et_checkpassword);
                                String password1 = new_password.getText().toString();
                                if (password1.equals(pwd)) {
                                    sv.setVisibility(View.VISIBLE);
                                    d.dismiss();
                                }
                                else {
                                    Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });
                d.show();
            }
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
