package us.mrvivacio.fabulousadventure;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.UserDictionary;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.Word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

//Activity to display vocab words
public class Vocab2 extends AppCompatActivity {

    private static final String TAG = "brett VocabActivity";
    private String[] Permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int Permission_All = 1;
    private ListView list1;
    ArrayAdapter<String> adapter;

    EditText inputSearch;


    final String FILENAME = "customWords.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Checks permissions again
        if (!hasPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }

        Log.d(TAG, "onCreate: entry point");
        if (!Word.loaded) {
            generateList();
            Word.loaded = true;
        }
        String[] array = new String[5];
        List testVocab = Word.pullRandom();

        Log.d(TAG, "onCreate: pulledRandom = " + testVocab);

        for (int i = 0; i < testVocab.size(); i++) {
            array[i] = ((Word) testVocab.get(i)).getWord();
        }

        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: super oncreate");
        setContentView(R.layout.activity_vocab2);

        Log.d(TAG, "onCreate: setting content view again");

        //https://www.youtube.com/watch?v=LD2zsCAAVXw
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Word.username);

        Log.d(TAG, "onCreate: getSupportActionBar");

        list1 = (ListView)findViewById(R.id.words);

        list1.setAdapter(new MyListAdapter(this, R.layout.word_item, Word.pullRandom()));

        Log.d(TAG, "onCreate: Setting adapter");

        final Button refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(v -> {
            Log.d(TAG, "Refresh button clicked");
            list1.setAdapter(new MyListAdapter(this, R.layout.word_item, Word.pullRandom()));
        });

        final Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            Log.d(TAG, "Save button clicked");
            writeFile();
            Toast.makeText(getApplicationContext(), "Words Saved", Toast.LENGTH_SHORT).show();
        });

        Log.d(TAG, "onCreate: finished");

    }

    public void add(View view) {
        Log.d(TAG, "toVocab: STARTING ADD INTENT");
        Intent startNewActivity = new Intent(Vocab2.this, AddWord.class);
        startActivity(startNewActivity);
        Log.d(TAG, "toVocab: FINISHED ADD INTENT");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }


    public class MyListAdapter extends ArrayAdapter {
        private int layout;
        public MyListAdapter(Context context, int resource, List objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.defReference = (TextView) convertView.findViewById(R.id.definition);
                viewHolder.getDefinitionReference = (Button) convertView.findViewById(R.id.getDefinition);
                viewHolder.wordReference = (TextView) convertView.findViewById(R.id.actualWord);
                viewHolder.wordReference.setText(((Word) getItem(position)).getWord());
                viewHolder.defReference.setText(((Word) getItem(position)).getDefinition());
                viewHolder.getDefinitionReference.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.defReference.setVisibility(View.VISIBLE);
                        ((Word) getItem(position)).decMastery();
                        ((Word) getItem(position)).decMastery();
                    }
                });
                convertView.setTag(viewHolder);
            } else {
                ViewHolder mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.wordReference.setText(((Word) getItem(position)).getWord());
                mainViewHolder.defReference.setText(((Word) getItem(position)).getDefinition());
                mainViewHolder.getDefinitionReference.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainViewHolder.defReference.setVisibility(View.VISIBLE);
                        ((Word) getItem(position)).decMastery();
                        ((Word) getItem(position)).decMastery();
                    }
                });
            }

            return convertView;
        }
    }




    //Thanks, https://www.youtube.com/watch?v=ZtVcT38_7Gs
    public void writeFile() {
        String entry = "Word:Definition:Mastery\n";

        ArrayList<Word> copy = Word.allWords;

        for (Word current : copy) {
            String toApp = current.getWord() + ":" +
                    current.getDefinition() + ":" +
                    Integer.toString(current.getMastery()) + "\n";
            entry += toApp;
        }

        try {
            FileOutputStream out = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            out.write(entry.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates allWords List
     * If doesn't contain permissions, uses default list in raw
     * Reads in words as a csv
     */
    public void generateList() {

        //thanks, https://www.youtube.com/watch?v=THPUrQGv8Ww
        if (hasPermissions(this, Permissions)) {
            try {
                //File textFile = new File(Environment.getExternalStorageDirectory(), FILENAME);
                FileInputStream fis = openFileInput(FILENAME);
                Log.d(TAG, "Successfully loaded in file");
                if (fis != null) {
                    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                    BufferedReader reader = new BufferedReader(isr);

                    String line = "";
                    try {
                        reader.readLine();
                        while (((line = reader.readLine()) != null)) {
                            Log.d(TAG, "Line: " + line);
                            //Split by '|'
                            String[] tokens = line.split(":");

                            //Read the data
                            Word sample = new Word(tokens[0], tokens[1], Integer.parseInt(tokens[2]));
                            Word.allWords.add(sample);
                        }
                    } catch (IOException e) {
                        Log.wtf(TAG, "Error reading data file on line " + line, e);
                        e.printStackTrace();
                    }
                }
            } catch(FileNotFoundException e) {
                //thanks, https://www.youtube.com/watch?v=i-TqNzUryn8
                Log.d(TAG, "File not made yet");
                InputStream is = this.getResources().openRawResource(R.raw.default_vocab);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, Charset.forName("UTF-8"))
                );

                String line = "";
                try {
                    reader.readLine();
                    while (((line = reader.readLine()) != null)) {
                        Log.d(TAG, "Line: " + line);
                        //Split by '|'
                        String[] tokens = line.split(":");

                        //Read the data
                        Word sample = new Word(tokens[0], tokens[1], Integer.parseInt(tokens[2]));
                        Word.allWords.add(sample);
                    }
                } catch (IOException error) {
                    Log.wtf(TAG, "Error reading data file on line " + line, error);
                    e.printStackTrace();
                }
            }

        } else {
            InputStream is = this.getResources().openRawResource(R.raw.default_vocab);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8"))
            );

            String line = "";
            try {
                reader.readLine();
                while (((line = reader.readLine()) != null)) {
                    Log.d(TAG, "Line: " + line);
                    //Split by '|'
                    String[] tokens = line.split(":");

                    //Read the data
                    Word sample = new Word(tokens[0], tokens[1], Integer.parseInt(tokens[2]));
                    Word.allWords.add(sample);
                }
            } catch (IOException e) {
                Log.wtf(TAG, "Error reading data file on line " + line, e);
                e.printStackTrace();
            }
        }
    }

    public class ViewHolder {
        TextView wordReference;
        TextView defReference;
        Button getDefinitionReference;
    }

    public static boolean hasPermissions(Context context, String... permissions){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
            for(String permission: permissions) {
                if(ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
