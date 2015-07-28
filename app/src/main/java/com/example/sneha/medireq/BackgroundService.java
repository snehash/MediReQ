package com.example.sneha.medireq;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.Attributes;

public class BackgroundService extends Service {
    public static boolean STARTED = false;
    public HashMap<String, Profile> profiles; //mapping of filename to profiles;
    public ArrayList<String> names; //need this for arrayAdapter ie UI

    public BackgroundService() {
    }

    public class LocalBinder extends Binder {
        BackgroundService getService(){ return BackgroundService.this;}
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate(){
        profiles = new HashMap<String, Profile>();
        names = new ArrayList<String>();
        System.out.println("Running onCreate for Service");
        ArrayList<String> profile_filenames = new ArrayList<String>();
        //TODO read from file
        try {
            InputStream inputStream = openFileInput(MainActivity.MASTER_FILE);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    profile_filenames.add(receiveString);
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        for(String filename: profile_filenames){
            profiles.put(filename, createFromFile(filename));
            names.add(profiles.get(filename).name);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        STARTED = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        STARTED = false;
        for(Profile profile: profiles.values()){
            writeToFile(profile);
        }

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(MainActivity.MASTER_FILE, Context.MODE_PRIVATE));
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            for(String filename: profiles.keySet()) {
                writer.write(filename);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    public void remove(String filename, int position){
        names.remove(position);
        //TODO:Delete this filename from master file
        //TODO: delete/clear profile file
        profiles.remove(filename);
    }

    public void saveProfile(String filename, Profile profile){
        profiles.put(filename, profile);
    }

    public void writeToFile(Profile profile){
        //TODO: code to write a profile to a file. Filename is profile.filename
    }

    public Profile createFromFile(String filename){
        //TODO: code to create profile from file. Use profile(name, filename) constructor not profile(name) constructor
        return null;
    }


}
