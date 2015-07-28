package com.example.sneha.medireq;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

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
        //TODO: read master file and populate profile_filenames
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

        //TODO: write filenames to master file
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
