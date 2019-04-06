package us.mrvivacio.fabulousadventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class questionslist extends AppCompatActivity {


    /**
     * QUESTION CONTENTS
     */
    String[] string_content = new String[21];
    String[] string_choice1 = new String[21];
    String[] string_choice2 = new String[21];
    String[] string_choice3 = new String[21];
    String[] string_choice4 = new String[21];
    public static int[] Choices = new int[21];


    /**
     * PREVIOUS QUESTION BUTTON
     * NEXT QURSTION BUTTON
     * SUBMITALL BUTTON
     */
    private Button previousQuestion;
    private Button nextQuestion;
    private Button submitAll;

    int x = 0;
    public int s = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionslist);

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.username);

        TextView MessageWindow = (TextView) findViewById(R.id.messageWindow2);
        StringBuilder stringBuilder = new StringBuilder();
        String someMessage = "";
        InputStream is = this.getResources().openRawResource(R.raw.sample);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(is));


        /**
         * STRING BUILDER TO READ FROM THE PLAIN TEXT DOCUMENT FOR ARTICLES
         */
        if (is != null) {

            try {
                while ((someMessage = reader2.readLine()) != null) {
                    stringBuilder.append(someMessage);
                }
                is.close();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }

        MessageWindow.setText(stringBuilder.toString());

        /*for (int i = 1; i <= 20; i++) {
            Choices[i] = 0;
        }*/


        x = 1;

        /**
         * CHECKBOX CHANGER
         */
        previousQuestion = (Button) findViewById(R.id.previousQuestion);
        previousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (x != 1) {
                    x--;
                    goto_previousQuestion(x);
                }
            }
        });

        nextQuestion = (Button) findViewById(R.id.nextQuestion);
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (x != s) {
                    x++;
                    goto_nextQuestion(x);
                }
            }
        });

        submitAll = (Button) findViewById(R.id.submitAll);
        submitAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answerspage();
            }
        });

        final CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox1.isChecked()) {
                    Choices[x] = myChecked(x, 8);
                } else {
                    Choices[x] = myNotChecked(x, 8);
                }
            }
        });

        final CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox2.isChecked()) {
                    Choices[x] = myChecked(x, 4);
                } else {
                    Choices[x] = myNotChecked(x, 4);
                }
            }
        });

        final CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox3.isChecked()) {
                    Choices[x] = myChecked(x, 2);
                } else {
                    Choices[x] = myNotChecked(x, 2);
                }
            }
        });

        final CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox4.isChecked()) {
                    Choices[x] = myChecked(x, 1);
                } else {
                    Choices[x] = myChecked(x, 1);
                }
            }
        });

        StringBuilder stringBuilder_content = new StringBuilder();
        StringBuilder stringBuilder_choice1 = new StringBuilder();
        StringBuilder stringBuilder_choice2 = new StringBuilder();
        StringBuilder stringBuilder_choice3 = new StringBuilder();
        StringBuilder stringBuilder_choice4 = new StringBuilder();


        String readIn = "";
        InputStream iss = this.getResources().openRawResource(R.raw.samplequestions);
        BufferedReader reader = new BufferedReader(new InputStreamReader(iss));

        /**
         * THE READ IN PART FROM THE TXT DOCUMENT
         */

        if (iss != null) {
            try {
                while ((readIn = reader.readLine()) != null) {

                    /**
                     * FIND QUESTION CONTENT AND CHOICE CONTENTS
                     */

                    if (readIn.charAt(0) != '(') {

                        stringBuilder_content.append(readIn);
                    }
                    if (readIn.charAt(0) == '(') {


                        if (readIn.charAt(1) == 'A') {
                            stringBuilder_choice1.append(readIn);
                        }
                        if (readIn.charAt(1) == 'B') {
                            stringBuilder_choice2.append(readIn);
                        }
                        if (readIn.charAt(1) == 'C') {
                            stringBuilder_choice3.append(readIn);
                        }
                        if (readIn.charAt(1) == 'D') {

                            stringBuilder_choice4.append(readIn);

                            s++;

                            string_content[s] = stringBuilder_content.toString();
                            string_choice1[s] = stringBuilder_choice1.toString();
                            string_choice2[s] = stringBuilder_choice2.toString();
                            string_choice3[s] = stringBuilder_choice3.toString();
                            string_choice4[s] = stringBuilder_choice4.toString();
                            //questionContent.setText(string_content[s]);

                            stringBuilder_content.delete(0, stringBuilder_content.length());
                            stringBuilder_choice1.delete(0, stringBuilder_choice1.length());
                            stringBuilder_choice2.delete(0, stringBuilder_choice2.length());
                            stringBuilder_choice3.delete(0, stringBuilder_choice3.length());
                            stringBuilder_choice4.delete(0, stringBuilder_choice4.length());
                        }
                    }
                }
                iss.close();
            }catch(Exception e) {
                e.printStackTrace();
            }
            mySet(x);

        }

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

    /**
     *
     * @param i THE QUESTION ID
     * @param j THE BIT NUMBER OF CHECKBOX STATUS
     * @return THE CHANGED BIT NUMBER
     */
    public int myChecked(int i,int j){
        return Choices[i] | j;
    }

    /**
     *
      * @param i THE QUESTION ID
     * @param j THE BIT NUMBER OF CHECKBOX STATUS
     * @return THE CHANGED BIT NUMBER
     */
    public int myNotChecked(int i,int j){
        return Choices[i] & (15 - j);
    }

    /**
     * HELP THE QUESTION'S CHECKBOXES
     * @param num QUESTION ID
     */
    public void mySet(int num) {

        TextView questionContent = (TextView) findViewById(R.id.questionContent);
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        questionContent.setText(string_content[num]);
        checkBox1.setText(string_choice1[num]);
        checkBox2.setText(string_choice2[num]);
        checkBox3.setText(string_choice3[num]);
        checkBox4.setText(string_choice4[num]);
        checkBox1.setChecked((Choices[num] & 8) != 0);
        checkBox2.setChecked((Choices[num] & 4) != 0);
        checkBox3.setChecked((Choices[num] & 2) != 0);
        checkBox4.setChecked((Choices[num] & 1) != 0);

    }

    /**
     * HELP SET THE QUESTION'S CHECKBOXES
     * @param num QUESTION ID
     */
    public void goto_previousQuestion(int num){
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        checkBox4.setChecked(false);
        mySet(num);
    }

    /**
     * GOTO THE NEXT QUESTION
     * @param num QUESTION ID
     */
    public void goto_nextQuestion(int num) {
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        checkBox4.setChecked(false);
        mySet(num);
    }

    /**
     * OPEN THE ANSWERS PAGE
     */
    public void Answerspage() {
        Intent intent = new Intent(questionslist.this, Answerspage.class);
        startActivity(intent);

    }
}
