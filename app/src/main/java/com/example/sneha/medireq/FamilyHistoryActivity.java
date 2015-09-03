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
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;


public class FamilyHistoryActivity extends Activity {
    private BackgroundService mBoundService;
    private boolean mIsBound;
    private Profile profile;
    private RadioButton momLiving;
    private RadioButton momDead;
    private RadioButton momUnknown;
    private RadioButton dadLiving;
    private RadioButton dadDead;
    private RadioButton dadUnknown;
    private EditText momIllness;
    private EditText dadIllness;
    private EditText sibilingIllness;
    private CheckBox fam_anemia, fam_cancer, fam_diabetes, fam_eye, fam_heart, fam_highbp,
            fam_hiv, fam_depression, fam_stroke;
    private EditText other, momAge, dadAge;
    private String currentMom, currentDad;
    private Button mSave;
    private Context context;
    private String filename;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_family_history);
        Intent intent = new Intent(this, BackgroundService.class);
        if (!BackgroundService.STARTED) {
            startService(intent);
        }
        doBindService();
    }

    private void init(){

        Intent intent = getIntent();

        filename = intent.getStringExtra(NavigationDrawer.PROFILE);
        System.out.println("Filename is " + filename);
        profile = mBoundService.profiles.get(filename);
        getActionBar().setTitle(profile.name);
        momLiving = (RadioButton) findViewById(R.id.living_mom_yes);
        momDead = (RadioButton) findViewById(R.id.living_mom_no);
        momUnknown = (RadioButton) findViewById(R.id.living_mom_unknown);
        dadLiving = (RadioButton) findViewById(R.id.living_dad_yes);
        dadDead = (RadioButton) findViewById(R.id.living_dad_no);
        dadUnknown = (RadioButton) findViewById(R.id.living_dad_unknown);
        momIllness = (EditText) findViewById(R.id.edit_mom_illness);
        dadIllness = (EditText) findViewById(R.id.edit_dad_illness);
        sibilingIllness = (EditText) findViewById(R.id.edit_sib_illness);
        other = (EditText) findViewById(R.id.edit_fam_details);
        momAge = (EditText) findViewById(R.id.edit_mom_age);
        dadAge = (EditText) findViewById(R.id.edit_dad_age);
        fam_anemia = (CheckBox) findViewById(R.id.checkbox_fam_anemia);
        fam_cancer = (CheckBox) findViewById(R.id.checkbox_fam_cancer);
        fam_diabetes = (CheckBox) findViewById(R.id.checkbox_fam_diabetes);
        fam_eye = (CheckBox) findViewById(R.id.checkbox_fam_eyedisorder);
        fam_heart = (CheckBox) findViewById(R.id.checkbox_fam_heartdisease);
        fam_highbp = (CheckBox) findViewById(R.id.checkbox_fam_highbp);
        fam_hiv = (CheckBox) findViewById(R.id.checkbox_fam_hiv);
        fam_depression = (CheckBox) findViewById(R.id.checkbox_fam_depression);
        fam_stroke = (CheckBox) findViewById(R.id.checkbox_fam_stroke);
        mSave = (Button) findViewById(R.id.bttn_save_family_history);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!momAge.getText().toString().equals(""))
                    profile.momAge = Integer.parseInt(momAge.getText().toString());
                if(!dadAge.getText().toString().equals(""))
                    profile.dadAge = Integer.parseInt(dadAge.getText().toString());
                profile.momLiving = currentMom;
                profile.dadLiving = currentDad;

                profile.motherIllness = momIllness.getText().toString();
                profile.fatherIllness = dadIllness.getText().toString();
                profile.sibilingIllness = sibilingIllness.getText().toString();
                profile.otherFamilyHistory = other.getText().toString();
                profile.fam_anemia = fam_anemia.isChecked();
                profile.fam_cancer = fam_cancer.isChecked();
                profile.fam_diabetes = fam_diabetes.isChecked();
                profile.fam_glaucoma = fam_eye.isChecked();
                profile.fam_hd = fam_heart.isChecked();
                profile.fam_hbp = fam_highbp.isChecked();
                profile.fam_hiv = fam_hiv.isChecked();
                profile.fam_depression = fam_depression.isChecked();
                profile.fam_stroke = fam_stroke.isChecked();
                mBoundService.saveProfile(filename, profile);
                Toast.makeText(context, "Changes saved successfully!", Toast.LENGTH_SHORT).show();

            }
        });

        switch(profile.momLiving){
            case "living":
                momLiving.setChecked(true);
                momDead.setChecked(false);
                momUnknown.setChecked(false);
                currentMom = "living";
                break;
            case "dead":
                momLiving.setChecked(false);
                momDead.setChecked(true);
                momUnknown.setChecked(false);
                currentMom = "dead";
                break;
            case "unknown":
                momLiving.setChecked(false);
                momDead.setChecked(false);
                momUnknown.setChecked(true);
                currentMom = "unknown";
                break;
            default:
                currentMom = "";
        }

        switch(profile.dadLiving){
            case "living":
                dadLiving.setChecked(true);
                dadDead.setChecked(false);
                dadUnknown.setChecked(false);
                currentDad = "living";
                break;
            case "dead":
                dadLiving.setChecked(false);
                dadDead.setChecked(true);
                dadUnknown.setChecked(false);
                currentDad = "dead";
                break;
            case "unknown":
                dadLiving.setChecked(false);
                dadDead.setChecked(false);
                dadUnknown.setChecked(true);
                currentDad = "unknown";
                break;
            default:
                currentDad = "";
        }

        if(profile.momAge >= 0) {
            momAge.setText(((Integer)profile.momAge).toString());
        }
        if(profile.dadAge >= 0) {
            dadAge.setText(((Integer)profile.dadAge).toString());
        }

        momIllness.setText(profile.motherIllness);
        dadIllness.setText(profile.fatherIllness);
        sibilingIllness.setText(profile.sibilingIllness);
        other.setText(profile.otherFamilyHistory);
        fam_anemia.setChecked(profile.fam_anemia);
        fam_cancer.setChecked(profile.fam_cancer);
        fam_diabetes.setChecked(profile.fam_diabetes);
        fam_eye.setChecked(profile.fam_glaucoma);
        fam_heart.setChecked(profile.fam_hd);
        fam_highbp.setChecked(profile.fam_hbp);
        fam_hiv.setChecked(profile.fam_hiv);
        fam_depression.setChecked(profile.fam_depression);
        fam_stroke.setChecked(profile.fam_stroke);



    }

    public void setChecked(View view){

        switch(view.getId()) {
            case R.id.fam_cancer:
                fam_cancer.setChecked(!fam_cancer.isChecked());
                break;
            case R.id.fam_anemia:
                fam_anemia.setChecked(!fam_anemia.isChecked());
                break;
            case R.id.fam_depression:
                fam_depression.setChecked(!fam_depression.isChecked());
                break;
            case R.id.fam_diabetes:
                fam_diabetes.setChecked(!fam_diabetes.isChecked());
                break;
            case R.id.fam_eyedisorder:
                fam_eye.setChecked(!fam_eye.isChecked());
                break;
            case R.id.fam_hiv:
                fam_hiv.setChecked(!fam_hiv.isChecked());
                break;
            case R.id.fam_stroke:
                fam_stroke.setChecked(!fam_stroke.isChecked());
                break;
            case R.id.fam_heartdisease:
                fam_heart.setChecked(!fam_heart.isChecked());
                break;
            case R.id.fam_highbp:
                fam_highbp.setChecked(!fam_highbp.isChecked());
                break;
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
            final ScrollView sv = (ScrollView) findViewById(R.id.fam_sv);
            sv.setVisibility(View.INVISIBLE);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            final String pwd = pref.getString("password", "");
            if (pwd.length() > 0) {
                LayoutInflater inflater=FamilyHistoryActivity.this.getLayoutInflater();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_family_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*switch(id){
            case R.id.fam_personal:
                Intent intent_personal = new Intent(context, ContactInformationActivity.class);
                intent_personal.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_personal);
                break;
            case R.id.fam_past_cond:
                Intent intent_pastcond = new Intent(context, PastConditionsActivity.class);
                intent_pastcond.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_pastcond);
                break;
            case R.id.fam_surgical_history:
                Intent intent_surg = new Intent(context, SurgicalHistory.class);
                intent_surg.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_surg);
                break;
            case R.id.fam_medi_allergies:
                Intent intent_allergies = new Intent(context, MedicalAllergies.class);
                intent_allergies.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_allergies);
                break;
            case R.id.fam_behav:
                Intent intent_behav = new Intent(context, BehaviorActivity.class);
                intent_behav.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_behav);
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
        bindService(new Intent(FamilyHistoryActivity.this, BackgroundService.class), mConnection, 0);
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

    public void onMomClicked(View view){

        switch(view.getId()){
            case R.id.living_mom_yes:
                currentMom = "living";
                break;
            case R.id.living_mom_no:
                currentMom = "dead";
                break;
            case R.id.living_mom_unknown:
                currentMom = "unknown";
                break;
        }
    }


    public void onDadClicked(View view){

        switch(view.getId()){
            case R.id.living_dad_yes:
                currentDad = "living";
                break;
            case R.id.living_dad_no:
                currentDad = "dead";
                break;
            case R.id.living_dad_unknown:
                currentDad = "unknown";
                break;
        }
    }
}
