package us.mrvivacio.fabulousadventure;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mylibrary.Word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Login extends AppCompatActivity {

    EditText e1, e2;

    Button b1;

    DatabaseHelper db;

    String fileName;

    public ArrayList<String> readIn = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHelper(this);
        e1 = (EditText) findViewById(R.id.email2);
        e2 = (EditText) findViewById(R.id.password2);
        b1 = (Button) findViewById(R.id.login);

        /**
         * login checking function with database helper. Once the login process is successful, we will parse the certain user's data file and update his priority value.
         */
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String password = e2.getText().toString();
                boolean chkemailpass = db.emailpassword(email, password);
                if (chkemailpass == true) {
                    /**
                     * everytime a user successfully log in, change update the attribute value for each word in the arraylist Wors.allwords according to the user's past data.
                     */
                    Word.username = email;
                    fileName = Word.username + ".txt";
                    readFile();
                    allUpdate();
                    Toast.makeText(getApplicationContext(), "Successfully Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainPage.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * checking function checking if we can read contents from the external storage.
     * @return a boolean value telling us whether we can read contents from the external storage.
     */
    private boolean isExternalStorageReadable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
            Log.i("State", "Yes, Readable");
            return true;
        } else {
            return false;
        }
    }

    /**
     * function parsing the user data file. read it in line as string, and put into an arraylist.
     */
    public void readFile() {
        if (isExternalStorageReadable()) {
            StringBuilder sb = new StringBuilder();
            try {
                File textFile = new File(Environment.getExternalStorageDirectory(), fileName);
                FileInputStream fis = new FileInputStream(textFile);

                if (fis != null) {
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader buff = new BufferedReader(isr);

                    String line = null;
                    while((line = buff.readLine()) != null) {
                        sb.append(line.toString());
                        readIn.add(line.toString());
                    }
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Cannot Read from External Storage.",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * If the login process is successful, we will call this function, to upadate the priority, mastery, and stalecount value for each words for the certain user.
     */
    public void allUpdate() {
        for (int i = 0; i < readIn.size(); i++) {
            for (int j = 0; j < Word.allWords.size(); j++) {
                if (readIn.get(i).split(" ")[0].equals(Word.allWords.get(j).getWord())) {
                    Word.allWords.get(j).pUpdate(Integer.parseInt(readIn.get(i).split(" ")[1]));
                    Word.allWords.get(j).mUpdate(Integer.parseInt(readIn.get(i).split(" ")[2]));
                    Word.allWords.get(j).sUpdate(Integer.parseInt(readIn.get(i).split(" ")[3]));
                }
            }
        }
    }

}
