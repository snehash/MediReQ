package com.example.sneha.medireq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.IBinder;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class NavigationDrawer extends Activity implements NfcAdapter.CreateNdefMessageCallback{
    public static String PROFILE = "com.example.MediReQ.profile";
    public static int UPDATEPROFILE = 6;

    private String[] categories;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private Context context;
    private Profile profile;
    private BackgroundService mBoundService;
    private boolean mIsBound;
    private NfcAdapter nfcAdapter;
    private String filename;
    private boolean isWriter = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Intent intent = new Intent(this, BackgroundService.class);
        if (!BackgroundService.STARTED) {
            startService(intent);
        }
        doBindService();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Register callback
        nfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
        MyApplication app = ((MyApplication)this.getApplication());
        if (System.currentTimeMillis() - app.mLastPause > 5000) {
            final LinearLayout ll = (LinearLayout) findViewById(R.id.navigationdrawer_ll);
            ll.setVisibility(View.INVISIBLE);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            final String pwd = pref.getString("password", "");
            if (pwd.length() > 0) {
                LayoutInflater inflater=NavigationDrawer.this.getLayoutInflater();
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
                                    ll.setVisibility(View.VISIBLE);
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
    public NdefMessage createNdefMessage(NfcEvent event) {
       String jsonProf = null;

        Gson json = new Gson();
        try {
            InputStreamReader in = new InputStreamReader(openFileInput(filename));
            String encryptedJson = "";
            int c = 0;
            while ((c = in.read()) != -1)
                encryptedJson += (char) c;
            jsonProf = Crypto.decrypt(encryptedJson, MainActivity.password);
            //Profile p = (Profile) json.fromJson(origJson, Profile.class);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Data Transfer Failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "Data Transfer Failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        System.out.println("Sending message");


        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { NdefRecord.createMime(
                        "text/plain", jsonProf.getBytes())
                        ,NdefRecord.createApplicationRecord("com.example.sneha.medireqreader")
                });
        return msg;
    }

    public void switchMode(View v) {
        Button b = (Button)v;
        if (isWriter) {
            b.setText("Reader");
            isWriter = false;
            nfcAdapter.setNdefPushMessageCallback(null, this);
        } else {
            b.setText("Writer");
            isWriter = true;
            nfcAdapter.setNdefPushMessageCallback(this, this);
        }
    }



    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        Toast.makeText(this, "Hi recieved", Toast.LENGTH_SHORT).show();
    }

    private void init(){

        Intent intent = getIntent();
        filename = intent.getStringExtra(NavigationDrawer.PROFILE);
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
                switch (selection) {
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
    public void onPause() {
        super.onPause();
        ((MyApplication)this.getApplication()).mLastPause = System.currentTimeMillis();
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
