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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class ContactInformationActivity extends Activity {
    private BackgroundService mBoundService;
    private boolean mIsBound;
    private Profile profile;
    private EditText mName;
    private EditText mAddress;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mEmer;
    private EditText mEmerNum;
    private EditText mAge;
    private EditText mBirthday;
    private EditText mWeight;
    private EditText mHeight;
    private EditText physicanName;
    private RadioButton male;
    private RadioButton female;
    private RadioButton other;
    private EditText physicanNo;
    private EditText lastVisit;
    private EditText medications;
    private EditText vitamins;
    private Button mButton;
    private Context context;
    private String currentGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_information);
        Intent intent = new Intent(this, BackgroundService.class);
        if (!BackgroundService.STARTED) {
            startService(intent);
        }
        doBindService();
    }

    private void init(){

        Intent intent = getIntent();

        int profile_no = intent.getIntExtra(NavigationDrawer.PROFILE, -1);
        profile = mBoundService.profiles.get(profile_no);
        mName = (EditText) findViewById(R.id.edit_name);
        mAddress = (EditText) findViewById(R.id.edit_address);
        mEmail = (EditText) findViewById(R.id.edit_email);
        mPhone = (EditText) findViewById(R.id.edit_phone);
        mEmer = (EditText) findViewById(R.id.edit_emer_name);
        mEmerNum = (EditText) findViewById(R.id.edit_emer_phone);
        mButton = (Button) findViewById(R.id.bttn_save_contact);
        mAge = (EditText) findViewById(R.id.tv_age);
        mBirthday = (EditText) findViewById(R.id.tv_birthdate);
        mWeight =  (EditText) findViewById(R.id.tv_weight);
        mHeight = (EditText) findViewById(R.id.tv_height);
        physicanName = (EditText) findViewById(R.id.tv_docname);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        other = (RadioButton) findViewById(R.id.other);
        physicanNo = (EditText) findViewById(R.id.tv_docnum);
        lastVisit = (EditText) findViewById(R.id.tv_lastvisit);
        medications = (EditText) findViewById(R.id.tv_medications);
        vitamins= (EditText) findViewById(R.id.tv_vitamins);

        context = this;

        mName.setText(profile.name);
        mAddress.setText(profile.address);
        mEmail.setText(profile.email);
        mPhone.setText(profile.phone);
        mEmer.setText(profile.emergencyContact);
        mEmerNum.setText(profile.emergencyNumber);
        mAge.setText(profile.age);
        mBirthday.setText(profile.birthday);
        mWeight.setText(profile.weight);
        mHeight.setText(profile.height);
        physicanName.setText(profile.docname);
        physicanNo.setText(profile.docno);
        lastVisit.setText(profile.lastVisit);
        medications.setText(profile.currMedi);
        vitamins.setText(profile.vitamins);

        switch(profile.gender){
            case "female":
                female.setChecked(true);
                male.setChecked(false);
                other.setChecked(false);
                break;
            case "male":
                female.setChecked(false);
                male.setChecked(true);
                other.setChecked(true);
                break;
            case "other":
                female.setChecked(false);
                male.setChecked(false);
                other.setChecked(true);
                break;
        }


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profile.name = mName.getText().toString();
                profile.age = Integer.parseInt(mAge.getText().toString());
                profile.address = mAddress.getText().toString();
                profile.email = mEmail.getText().toString();
                profile.phone = mPhone.getText().toString();
                profile.emergencyNumber = mEmerNum.getText().toString();
                profile.emergencyContact = mEmer.getText().toString();
                profile.birthday = mBirthday.getText().toString();
                profile.weight = Integer.parseInt(mWeight.getText().toString());
                profile.height = mHeight.getText().toString();
                profile.docname = physicanName.getText().toString();
                profile.docno = physicanNo.getText().toString();
                profile.lastVisit = lastVisit.getText().toString();
                profile.currMedi = medications.getText().toString();
                profile.vitamins = vitamins.getText().toString();
                profile.gender = currentGender;

                mBoundService.saveProfile(mBoundService.profiles.indexOf(profile));



                /*try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("com.example.MediReQ." + profile.getName(), Context.MODE_PRIVATE));
                    BufferedWriter writer = new BufferedWriter(outputStreamWriter);
                    writer.write(profile.getAddress());
                    writer.newLine();
                    writer.write(profile.getEmail());
                    writer.newLine();
                    writer.write(profile.getPhone());
                    writer.newLine();
                    writer.write(profile.getEmergencyContact());
                    writer.newLine();
                    writer.write(profile.getEmergencyNumber());
                    writer.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }


                Toast.makeText(context, "Changes saved!", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra(NavigationDrawer.PROFILE, profile);
                setResult(RESULT_OK, returnIntent);
                finish(); */
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_information, menu);
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
        bindService(new Intent(ContactInformationActivity.this, BackgroundService.class), mConnection, 0);
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
        mBoundService.stopSelf();
    }

    public void onGenderButtonClicked(View view){

        switch(view.getId()){
            case R.id.male:
                currentGender = "male";
                break;
            case R.id.female:
                currentGender = "female";
                break;
            case R.id.other:
                currentGender = "other";
                break;
        }
    }
}
