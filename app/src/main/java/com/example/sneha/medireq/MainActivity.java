package com.example.sneha.medireq;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

//TODO: If name is changed, filename will be a problem
public class MainActivity extends ActionBarActivity {
    private ArrayList<String> names;
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private Button mButton;
    private Context context;
    private String new_Name;
    private static final String filename = "com.example.MediReQ.names.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        names = new ArrayList<String>();
        readNamesFromFile();
        mAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, names);
        mListView = (ListView) findViewById(R.id.profiles);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, NavigationDrawer.class);
                intent.putExtra(NavigationDrawer.PROFILE, names.get(position));
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
                                // continue with delete
                                names.remove(i);
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


        mButton = (Button) findViewById(R.id.bttn_add_profiles);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Profile for:");


                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new_Name = input.getText().toString();
                        Profile profile = new Profile();
                        profile.setName(new_Name);
                        names.add(new_Name);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });



    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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


}
