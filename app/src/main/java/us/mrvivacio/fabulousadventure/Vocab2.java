package us.mrvivacio.fabulousadventure;

import android.content.Context;
import android.provider.UserDictionary;
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

import java.util.ArrayList;
import java.util.List;


public class Vocab2 extends AppCompatActivity {

    private static final String TAG = "brett VocabActivity";

    private ListView list1;
    ArrayAdapter<String> adapter;
    EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: entry point");
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

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Log.d(TAG, "onCreate: getSupportActionBar");

        list1 = (ListView)findViewById(R.id.words);

        list1.setAdapter(new MyListAdapter(this, R.layout.word_item, Word.pullRandom()));

        Log.d(TAG, "onCreate: Setting adapter");

        final Button refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(v -> {
            Log.d(TAG, "Refresh button clicked");
            list1.setAdapter(new MyListAdapter(this, R.layout.word_item, Word.pullRandom()));
        });

        Log.d(TAG, "onCreate: finished");
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

    public class ViewHolder {
        TextView wordReference;
        TextView defReference;
        Button getDefinitionReference;
    }
}
