package com.example.sneha.medireq;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class NavigationDrawer extends ActionBarActivity {
    public static String PROFILE = "com.example.MediReQ.profile";
    public static int UPDATEPROFILE = 6;

    private String[] categories;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private Context context;
    private Profile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Intent intent = getIntent();
        String name = intent.getStringExtra(PROFILE);
        try {
            InputStream inputStream = openFileInput("com.example.MediReQ." + name);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String Address = bufferedReader.readLine();
                String Email = bufferedReader.readLine();
                String Phone = bufferedReader.readLine();
                String EmerCon = bufferedReader.readLine();
                String EmPhone = bufferedReader.readLine();
                profile = new Profile(name, Address, Email, Phone, EmerCon, EmPhone);
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("main activity", "File not found: " + e.toString());
            profile = new Profile();
            profile.setName(name);
        } catch (IOException e) {
            Log.e("main activity", "Can not read file: " + e.toString());
        }


        mListView = (ListView) findViewById(R.id.lv_categories);
        context = this;
        categories = getResources().getStringArray(R.array.categories);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,categories);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = categories[position];
                switch(selection) {
                    case "Contact Information":
                        Intent intent = new Intent(context, ContactInformationActivity.class);
                        intent.putExtra(PROFILE, profile);
                        startActivityForResult(intent, UPDATEPROFILE);
                        break;
                }


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


            if(resultCode == RESULT_OK){
                profile = data.getParcelableExtra(PROFILE);
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }

    }

    @Override
    protected void onResume(){
        super.onResume();
        System.out.println(profile);
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

    public void transferData(View view){
        //code for data transfer goes here
    }
}
