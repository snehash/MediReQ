package com.example.sneha.medireq;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private ListView mListView;
    private Button mButton;
    private ArrayAdapter<String> mAdapter;
    private Context context;
    private BackgroundService mBoundService;
    private boolean mIsBound;

    public static final String MASTER_FILE = "Profiles_MediReQ";
    public static String password = "123456789";
    private String new_Name;
    private boolean unlocked = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String pwd = pref.getString("password", "");
        if(pwd == null || pwd.equals("") || pwd.length() <= 0){
            LayoutInflater inflater=MainActivity.this.getLayoutInflater();
            final View layout=inflater.inflate(R.layout.new_password, null);
            final AlertDialog d = new AlertDialog.Builder(context)
                    .setView(layout)
                    .setTitle("Create Password")
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
                            EditText new_password=(EditText)layout.findViewById(R.id.EditText_Pwd1);
                            EditText confirm_password=(EditText)layout.findViewById(R.id.EditText_Pwd2);
                            String password1 = new_password.getText().toString();
                            String password2 = confirm_password.getText().toString();
                            if(password1.equals("")){
                                Toast.makeText(context, "Please enter valid password", Toast.LENGTH_SHORT).show();
                            }
                            else if (password1.equals(password2)) {
                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("password", password1);
                                editor.commit();
                                d.dismiss();
                                Intent intent = new Intent(context, BackgroundService.class);
                                if (!BackgroundService.STARTED) {
                                    startService(intent);
                                }
                                doBindService();
                            }
                            else {
                                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            });
            d.show();

        }
        else {
            Intent intent = new Intent(context, BackgroundService.class);
            if (!BackgroundService.STARTED) {
                startService(intent);
            }
            doBindService();
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
            final LinearLayout ll = (LinearLayout) findViewById(R.id.main_ll);
            ll.setVisibility(View.INVISIBLE);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            final String pwd = pref.getString("password", "");
            if (pwd.length() > 0) {
                LayoutInflater inflater=MainActivity.this.getLayoutInflater();
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

    private void init(){
        mAdapter = new ArrayAdapter<String>(context,R.layout.mytextview, mBoundService.names);
        mListView = (ListView) findViewById(R.id.profiles);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, NavigationDrawer.class);
                String filename = "";
                for (Profile profile : mBoundService.profiles.values()) {
                    if (profile.name.equals(mBoundService.names.get(position))) {
                        filename = profile.filename;
                    }
                }
                intent.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent);

            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int i = position;
                new AlertDialog.Builder(context)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this profile? You will not be able to recover it.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Todo: remove from files
                                String filename = "";
                                for (Profile profile: mBoundService.profiles.values()){
                                    if(profile.name.equals(mBoundService.names.get(i))){
                                        filename = profile.filename;
                                    }
                                }
                                mBoundService.remove(filename, i);

                                //also delete file here
                                mAdapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });






    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mIsBound){
            doUnbindService();
        }
        mBoundService.stopSelf();
        /*
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
                BufferedWriter writer = new BufferedWriter(outputStreamWriter);
                for(String name: names) {
                    writer.write(name);
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
            */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Profile for:");


        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new_Name = input.getText().toString();
                if(!mBoundService.names.contains(new_Name)) {
                    Profile profile;
                    if(mBoundService.profiles.containsKey("MediReQ_" + new_Name)) {
                        int i = 2;
                        String try_file = "MediReQ_" + new_Name + i;
                        while(mBoundService.profiles.containsKey(try_file)){
                            i++;
                            try_file = "MediReQ_" + new_Name + i;
                        }
                        profile = new Profile(new_Name, try_file);
                    }
                    else{
                        profile = new Profile(new_Name);
                    }

                    mBoundService.names.add(new_Name);
                    mBoundService.profiles.put(profile.filename, profile);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();



        return super.onOptionsItemSelected(item);
    }


    /*
    private void readNamesFromFile() {

        try {
            InputStream inputStream = openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    names.add(receiveString);
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

    }
    */

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((BackgroundService.LocalBinder)service).getService();
            init();
        }

        public void onServiceDisconnected(ComponentName className){ mBoundService = null;}

    };

    private void doBindService(){
        bindService(new Intent(MainActivity.this, BackgroundService.class), mConnection, 0);
        mIsBound = true;
    }

    private void doUnbindService(){
        if (mIsBound){
            unbindService(mConnection);
            mIsBound = false;
        }
    }


}
