package com.example.sneha.medireq;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;

public class BackgroundService extends Service {
    public static boolean STARTED = false;
    public HashMap<String, Profile> profiles; //mapping of filename to profiles;
    public ArrayList<String> names; //need this for arrayAdapter ie UI
    public static String TAG = "BackgroundService";

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
        ArrayList<String> profile_filenames = readFilesFromFile();
        if (profile_filenames == null) { names = null; }
        for(String filename: profile_filenames){
            Profile p = createFromFile(filename);
            System.out.println("Reading: " + p.name + " " + p.address);
            profiles.put(filename, p);
            names.add(p.name);
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

        ArrayList<String> files = new ArrayList<String>();
        files.addAll(profiles.keySet());
        writeToFile(files);
    }

    public void remove(String filename, int position){
        names.remove(position);
        profiles.remove(filename);
    }

    public void saveProfile(String filename, Profile profile){
        profiles.put(filename, profile);
        try{
            writeToFile(profile);
        } catch (Exception exe){
            Log.e(TAG, "Error writing to file");
        }
    }

    public ArrayList<String> readFilesFromFile() {
        try {
            Cipher key = Crypto.getDecryptCipher(MainActivity.password);
            ObjectInputStream in = new ObjectInputStream(openFileInput(MainActivity.MASTER_FILE));
            SealedObject encrypted = (SealedObject) in.readObject();
            return (ArrayList<String>) encrypted.getObject(key);
        } catch (FileNotFoundException e) {
            return new ArrayList<String>();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void writeToFile(ArrayList<String> filenames) {
        try {
            Cipher key = Crypto.getEncryptCipher(MainActivity.password);
            ObjectOutputStream out = new ObjectOutputStream(openFileOutput(MainActivity.MASTER_FILE, Context.MODE_PRIVATE));
            SealedObject encrypted = new SealedObject(filenames, key);
            out.writeObject(encrypted);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(Profile profile){
        Gson json = new Gson();
        String profileJson = json.toJson(profile);
        String encryptedJson = Crypto.encrypt(profileJson, MainActivity.password);
        try {
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput(profile.filename, Context.MODE_PRIVATE));
            out.write(encryptedJson);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Profile createFromFile(String filename){
        Gson json = new Gson();
        try {
            InputStreamReader in = new InputStreamReader(openFileInput(filename));
            String encryptedJson = "";
            int c = 0;
            while ((c = in.read()) != -1)
                encryptedJson += (char) c;
            String origJson = Crypto.decrypt(encryptedJson, MainActivity.password);
            Profile p = (Profile) json.fromJson(origJson, Profile.class);
            return p;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
