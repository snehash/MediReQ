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

        //ToDo: read files and populate profile and name here
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        STARTED = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        STARTED = false;
        //Todo: Save name and profile data to file. Don't have to do this if you're saving everytime you make a change
    }

    public void remove(String filename, int position){
        names.remove(position);
        //ToDo:
        //change names file
        //delete/clear profile file
        profiles.remove(filename);
    }

    public void saveProfile(String filename, Profile profile){
        profiles.put(filename, profile);
        //TODO: save profile data;
    }


}
