package us.mrvivacio.fabulousadventure;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    //Thanks, https://www.youtube.com/watch?v=SMrB97JuIoM
    //Thanks, https://www.youtube.com/watch?v=iqFRdjYqGPo
    private int Permission_All = 1;

    DatabaseHelper db;

    EditText e1, e2, e3;

    Button b1, b2, b3;

    final String FILENAME = "customWords.csv";

    private static final String TAG = "brett VocabActivity";

    private String[] Permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public String folder_main = "Test-PreperUserData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] Permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }

        db = new DatabaseHelper(this);

        e1 = (EditText) findViewById(R.id.email);

        e2 = (EditText) findViewById(R.id.pass);

        e3 = (EditText) findViewById(R.id.cpass);

        b1 = (Button) findViewById(R.id.register);

        b2 = (Button) findViewById(R.id.contlogin);

        b3 = (Button) findViewById(R.id.skip);

        generateList();
        Word.loaded = true;


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainPage.class);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        /**
         * checking function for registering an account with database helper. If the registration is successful, we will call the writeFileInitial function, which generate the initial user's data file for a user.
         */
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = e1.getText().toString();
                String s2 = e2.getText().toString();
                String s3 = e3.getText().toString();
                if (s1.equals("") || s2.equals("") || s3.equals("")) {
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (s2.equals(s3)) {
                        boolean chkmail = db.chkmail(s1);
                        if (chkmail == true) {
                            boolean insert = db.insert(s1, s2);
                            if (insert == true) {
                                writeFileInitial();
                                Toast.makeText(getApplicationContext(), "Registered  Successfully", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Email Already Exists", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Password Do Not Match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
            for(String permission: permissions) {
                if(ActivityCompat.checkSelfPermission(context, permission)!=PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * function checking whether we have permission from manifest
     * @param permission permission name
     * @return a boolean value tells us whether we have the permission.
     */
    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * This is the function that initialize the user's data text document.All words' priority value is set to one.
     */
    public void writeFileInitial() {
        if (isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File f = new File(Environment.getExternalStorageDirectory(), folder_main);
            if (!f.exists()) {
                f.mkdirs();
            }
            File textFile = new File(Environment.getExternalStorageDirectory() + "/" + folder_main, e1.getText().toString() + ".txt");
            try {
                FileOutputStream fos = new FileOutputStream(textFile, false);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                for (int i = 0; i < Word.allWords.size(); i++) {
                    bw.write(Word.allWords.get(i).getWord() + " " + "1");
                    bw.newLine();
                }
                bw.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this,"Cannot Write To External Storage",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * a function checking whether we can write things into external storage.
     * @return a boolean value telling us whether we can or cannot write things into external storage.
     */
    private boolean isExternalStorageWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("State", "Yes, Writable");
            return true;
        } else {
            return false;
        }
    }

    /**
     * copy from Brett Patterson's branch, generate the word list at rhe start of the application to change these priority value in advance.
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
}
