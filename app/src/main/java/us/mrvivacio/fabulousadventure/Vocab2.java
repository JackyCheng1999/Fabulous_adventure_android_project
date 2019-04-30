package us.mrvivacio.fabulousadventure;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.UserDictionary;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.Word;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

//Activity to display vocab words
public class Vocab2 extends AppCompatActivity {

    //Tag log
    private static final String TAG = "brett VocabActivity";

    //String to store both read and write permissions
    private String[] Permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //integer to store permissions
    private int Permission_All = 1;

    //ListView used to populate the screen
    private ListView list1;

    //File name for the stored csv file
    final String FILENAME = "customWords.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Checks permissions again
        if (!hasPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }

        Log.d(TAG, "onCreate: entry point");

        //This checks if the words were loaded before -
        //need to save variable in external class
        //because the variable will reset on the activity
        // used to avoid duplicate words
        if (!Word.loaded) {
            generateList();
            Word.loaded = true;
        }

        //String array to hold the words displayed on the screen
        String[] array = new String[5];

        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: super oncreate");

        setContentView(R.layout.activity_vocab2);

        Log.d(TAG, "onCreate: setting content view again");

        //https://www.youtube.com/watch?v=LD2zsCAAVXw
        //Displays toolbar with the title to the user's email
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Word.username);

        Log.d(TAG, "onCreate: getSupportActionBar");

        //Sets list1 to the listview in the Vocab2 xml
        list1 = (ListView)findViewById(R.id.words);

        //Populates the listview in the xml to display individual word_item xml for each word
        //The word_item is populated with the list from Word.pullRandom()
        //MyListAdapter is a custom class created below
        list1.setAdapter(new MyListAdapter(this, R.layout.word_item, Word.pullRandom()));

        Log.d(TAG, "onCreate: Setting adapter");

        //Sets the refresh button to repopulate the screen with new Word.pullRandom words
        final Button refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(v -> {
            Log.d(TAG, "Refresh button clicked");
            list1.setAdapter(new MyListAdapter(this, R.layout.word_item, Word.pullRandom()));
            writeTextFile();
        });

        //Maps the save button to the writeFile function below
        final Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            Log.d(TAG, "Save button clicked");
            writeFile();
            Toast.makeText(getApplicationContext(), "Words Saved", Toast.LENGTH_SHORT).show();
        });

        Log.d(TAG, "onCreate: finished");

    }

    /**
     * Launches a new intent to the AddWord class
     * @param view the button called add word
     */
    public void add(View view) {
        Log.d(TAG, "toVocab: STARTING ADD INTENT");
        Intent startNewActivity = new Intent(Vocab2.this, AddWord.class);
        startActivity(startNewActivity);
        Log.d(TAG, "toVocab: FINISHED ADD INTENT");
    }


    /**
     *
     * @param menu- toolbar inflater with settings
     * @return returns true if successful
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *
     * @param item - maps each individual menu item to a function
     *             Since there is only one item (settings) it launches an intent to settings
     * @return returns true if successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }


    /**
     * Public class made to map each Word object to the ListView
     */
    public class MyListAdapter extends ArrayAdapter {
        private int layout;

        //Constructor for the class (Same as parent class)
        public MyListAdapter(Context context, int resource, List objects) {
            super(context, resource, objects);
            layout = resource;
        }

        /**
         *
         * @param position
         * @param convertView
         * @param parent
         * @return ListView to display- automatic
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //if there is no current view, we make one
            if(convertView == null) {

                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                //Custom class created below- used to store variables
                ViewHolder viewHolder = new ViewHolder();

                //Sets the viewHolder variables to their respective views
                viewHolder.defReference = (TextView) convertView.findViewById(R.id.definition);
                viewHolder.getDefinitionReference = (Button) convertView.findViewById(R.id.getDefinition);
                viewHolder.wordReference = (TextView) convertView.findViewById(R.id.actualWord);

                //Sets the values of the textViews to be displayed
                viewHolder.wordReference.setText(((Word) getItem(position)).getWord());

                //This is set as invisible by default
                viewHolder.defReference.setText(((Word) getItem(position)).getDefinition());

                //Since getDefinition is a button, we need to add a function to it
                viewHolder.getDefinitionReference.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //bounce animation for the getDefinition Button
                        Animation anime = AnimationUtils.loadAnimation(Vocab2.this, R.anim.bounce);
                        viewHolder.getDefinitionReference.startAnimation(anime);
                        //sets the visibility of the definition to visible
                        viewHolder.defReference.setVisibility(View.VISIBLE);

                        //must decrement the mastery of the word twice because presenting the
                        //word on the screen increments the mastery automatically
                        //Net decrease == 1 instead of 0 because of this
                        ((Word) getItem(position)).decMastery();
                        ((Word) getItem(position)).decMastery();
                        writeTextFile();
                    }
                });
                //sets the tags of the view passed
                convertView.setTag(viewHolder);
            } else {
                //Since the view is already made, we just need to retrieve the tags and modify them
                ViewHolder mainViewHolder = (ViewHolder) convertView.getTag();

                //Same code as above
                mainViewHolder.wordReference.setText(((Word) getItem(position)).getWord());
                mainViewHolder.defReference.setText(((Word) getItem(position)).getDefinition());
                mainViewHolder.getDefinitionReference.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainViewHolder.defReference.setVisibility(View.VISIBLE);
                        ((Word) getItem(position)).decMastery();
                        ((Word) getItem(position)).decMastery();
                        writeTextFile();
                    }
                });
            }
            //returns the view to the ListView knows how to display the information
            return convertView;
        }
    }


    /**
     * Custom class used to store buttons and textViews for the listView
     */
    public class ViewHolder {
        TextView wordReference;
        TextView defReference;
        Button getDefinitionReference;
    }


    //Thanks, https://www.youtube.com/watch?v=ZtVcT38_7Gs

    /**
     * Writes the words stored in allWords (ArrayList in the Word class)
     * to a csv file in external storage
     */
    public void writeFile() {
        //Sets the headers for the csv
        //Needs to be a long string to write to the file
        String entry = "Word:Definition:Mastery\n";

        //Copies the allWords ArrayList
        ArrayList<Word> copy = Word.allWords;

        //Adds each individual word to the big string according to the header
        for (Word current : copy) {
            String toApp = current.getWord() + ":" +
                    current.getDefinition() + ":" +
                    Integer.toString(current.getMastery()) + "\n";
            entry += toApp;
        }

        //Checks permissions again before going
        if (hasPermissions(this, Permissions)) {
            //Uses try in case there is a file output
            try {
                //Makes a new file or overwrites the current file
                FileOutputStream out = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                //Writes the string as bytes
                out.write(entry.getBytes());
                //Closes the file stream
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //Lets user know they don't have the permissions
            Toast.makeText(getApplicationContext(), "You don't have the required permissions", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Generates allWords List
     * If doesn't contain permissions, uses default list in raw
     * Reads in words as a csv
     */
    public void generateList() {

        //thanks, https://www.youtube.com/watch?v=THPUrQGv8Ww
        //Checks permissions
        if (hasPermissions(this, Permissions)) {
            //Uses try in case the file was never created before
            try {
                //thanks, https://www.youtube.com/watch?v=4HI1Sf_2F5Y
                //Opens file
                FileInputStream fis = openFileInput(FILENAME);
                Log.d(TAG, "Successfully loaded in file");
                if (fis != null) {
                    //Reads in the files as a whole
                    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                    //Reads in the file line by line to parse individually
                    BufferedReader reader = new BufferedReader(isr);

                    String line = "";
                    try {
                        //skip the header
                        reader.readLine();

                        //Reads in each of the words and adds them to allWords in Words class
                        while (((line = reader.readLine()) != null)) {
                            Log.d(TAG, "Line: " + line);
                            //Split by ':'
                            String[] tokens = line.split(":");

                            //Read the data by Word:Definition:Mastery
                            Word sample = new Word(tokens[0], tokens[1], Integer.parseInt(tokens[2]));
                            //Adds the Word to allWords
                            Word.allWords.add(sample);
                        }
                    } catch (IOException e) {
                        Log.wtf(TAG, "Error reading data file on line " + line, e);
                        e.printStackTrace();
                    }
                }
            } catch(FileNotFoundException e) {
                //thanks, https://www.youtube.com/watch?v=i-TqNzUryn8
                //So if the file is not found- just read in the csv from raw
                Log.d(TAG, "File not made yet");
                //Reads in the raw file as a whole
                InputStream is = this.getResources().openRawResource(R.raw.default_vocab);
                //Reads in raw line by line
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, Charset.forName("UTF-8"))
                );

                String line = "";
                //Same process as reading in file
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
            //Same code as above else statement (reads in csv from raw)
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

    /**
     *
     * @param context
     * @param permissions string containing the permissions
     * @return true if all permissions are satisfied
     */
    public static boolean hasPermissions(Context context, String... permissions){

        //Checks build version
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
            //Checks each permission individually
            for(String permission: permissions) {
                if(ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public void writeTextFile() {
        if (isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File textFile = new File(Environment.getExternalStorageDirectory(), Word.username + ".txt");
            try {
                FileOutputStream fos = new FileOutputStream(textFile);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                for (int i = 0; i < Word.allWords.size(); i++) {
                    bw.write(Word.allWords.get(i).getWord() + " " + Integer.toString(Word.allWords.get(i).getPriority()));
                    bw.newLine();
                }
                bw.close();
                //fos.write("1".getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this,"Cannot Write To External Storage",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isExternalStorageWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("State", "Yes, Writable");
            return true;
        } else {
            return false;
        }
    }
}
