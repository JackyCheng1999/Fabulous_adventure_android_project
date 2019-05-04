package us.mrvivacio.fabulousadventure;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.Word;

public class Settings extends AppCompatActivity {

    Button b1, b2, b3;

    private static final int PICK_IMAGE = 100;

    Uri imageUri;

    ImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        b1 = (Button) findViewById(R.id.register2);

        b2 = (Button) findViewById(R.id.login2);

        b3 = (Button) findViewById(R.id.logout);

        TextView identity = (TextView) findViewById(R.id.textView5);

        avatar = (ImageView)  findViewById(R.id.avatar);

        identity.setText(Word.username);

        avatar.setImageResource(R.drawable.sampleavatar);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Login.class);
                startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word.username = "Anonymous User";
                for (int j = 0; j < Word.allWords.size(); j++) {
                    Word.allWords.get(j).pUpdate(1);
                    Word.allWords.get(j).mUpdate(1);
                    Word.allWords.get(j).sUpdate(1);
                }
                Toast.makeText(getApplicationContext(), "Log Out Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        //https://www.youtube.com/watch?v=LD2zsCAAVXw
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Word.username);
    }


    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            avatar.setImageURI(imageUri);
        }
    }
}
