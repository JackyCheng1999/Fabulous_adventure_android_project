package us.mrvivacio.fabulousadventure;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.ViewGroup;

public class Vocab extends AppCompatActivity {

    static String TAG = "brett";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab);
        //Toolbar toolbar = findViewById(R.id.action_bar);
        //setSupportActionBar(toolbar);

        Log.d(TAG, "onCreate: END OF ONCREATE");
    }

    public void toVocab(View view) {
        Log.d(TAG, "toVocab: STARTING TOVOCAB INTENT");
        Intent startNewActivity = new Intent(this, Vocab2.class);
        startActivity(startNewActivity);
        Log.d(TAG, "toVocab: FINISHED TOVOCAB INTENT");
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    */
}
