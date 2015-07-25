package com.example.sneha.medireq;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class ContactInformationActivity extends ActionBarActivity {
    private Profile profile;
    private EditText mName;
    private EditText mAddress;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mEmer;
    private EditText mEmerNum;
    private Button mButton;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_information);
        Intent intent = getIntent();
        profile = intent.getParcelableExtra(NavigationDrawer.PROFILE);
        System.out.println(profile);
        mName = (EditText) findViewById(R.id.edit_name);
        mAddress = (EditText) findViewById(R.id.edit_address);
        mEmail = (EditText) findViewById(R.id.edit_email);
        mPhone = (EditText) findViewById(R.id.edit_phone);
        mEmer = (EditText) findViewById(R.id.edit_emer_name);
        mEmerNum = (EditText) findViewById(R.id.edit_emer_phone);
        mButton = (Button) findViewById(R.id.bttn_save_contact);
        context = this;

        if(profile.getName() != null){
            mName.setText(profile.getName());
        }
        if(profile.getAddress() != null){
            mAddress.setText(profile.getAddress());
        }
        if(profile.getEmail() != null){
            mEmail.setText(profile.getEmail());
        }
        if(profile.getPhone() != null){
            mPhone.setText(profile.getPhone());
        }
        if(profile.getEmergencyContact() != null){
            mEmer.setText(profile.getEmergencyContact());
        }
        if(profile.getEmergencyNumber() != null){
            mEmerNum.setText(profile.getEmergencyNumber());
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.setName(mName.getText().toString());
                profile.setAddress(mAddress.getText().toString());
                profile.setEmail(mEmail.getText().toString());
                profile.setPhone(mPhone.getText().toString());
                profile.setEmergencyContact(mEmer.getText().toString());
                profile.setEmergencyNumber(mEmerNum.getText().toString());
                try {
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
                finish();
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
}
