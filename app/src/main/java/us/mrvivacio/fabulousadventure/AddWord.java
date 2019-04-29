package us.mrvivacio.fabulousadventure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mylibrary.Word;

public class AddWord extends AppCompatActivity {

    private static final String TAG = "brett AddWord";
    //TextBox for the word
    private EditText word;
    //TextBox for the definition
    private EditText definition;
    //Button to submit
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: entry point");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        Log.d(TAG, "onCreate: super oncreate");

        //https://www.youtube.com/watch?v=LD2zsCAAVXw
        //Displays toolbar with the title to the user's email
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Word.username);

        Log.d(TAG, "onCreate: getSupportActionBar");

        //Maps the EditText and buttons to variables
        word = (EditText) findViewById(R.id.editWord);
        definition = (EditText) findViewById(R.id.editDefinition);
        submit = (Button) findViewById(R.id.submit);

        //On click, adds word to allWords and displays a toast confirmation
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wordInfo = word.getText().toString();
                String defInfo = definition.getText().toString();
                Word.allWords.add(new Word(wordInfo, defInfo, 1));
                Toast.makeText(getApplicationContext(), "Word Added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
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
}
