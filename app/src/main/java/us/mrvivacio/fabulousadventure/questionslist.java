package us.mrvivacio.fabulousadventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class questionslist extends MockExam { //AppCompatActivity


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
     * previous question button
     * next question button
     * submitAll button
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

        /**
         * find the textview by ID
         * preparing to read from the documents
         */

        TextView MessageWindow = (TextView) findViewById(R.id.messageWindow2);
        StringBuilder stringBuilder = new StringBuilder();
        String someMessage = "";
        InputStream is = this.getResources().openRawResource(R.raw.sample);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(is));
        int passageIDcounter = 0;

        /**
         * stringBuilder to read from a plain text document
         */

        if (is != null) {

            try {
                while ((someMessage = reader2.readLine()) != null) {

                    /**
                     * The "67931" is the starting code of each seperate passage
                     */
                    if (someMessage.charAt(0) == '6' &&
                            someMessage.charAt(1) == '7' &&
                            someMessage.charAt(2) == '9' &&
                            someMessage.charAt(3) == '3' &&
                            someMessage.charAt(4) == '1') {
                        passageIDcounter++;
                        continue;
                    }
                    if (passageIDcounter == passageID) {
                        stringBuilder.append(someMessage);
                    }
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
         * goto previous question
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

        /**
         * goto next question
         */

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

        /**
         * The OnClickListener function of the submitAll button
         */

        submitAll = (Button) findViewById(R.id.submitAll);
        submitAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answerspage();
            }
        });

        /**
         * Check or uncheck the A choice (8 = 1000(2))
         */

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

        /**
         * Check or uncheck the B choice (4 = 100(2))
         */

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

        /**
         * Check or uncheck the C choice (2 = 10(2))
         */

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

        /**
         * Check or uncheck the D choice (1 = 1(2))
         */

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

        /**
         * Five different stringBuilders for question content and four options
         */

        StringBuilder stringBuilder_content = new StringBuilder();
        StringBuilder stringBuilder_choice1 = new StringBuilder();
        StringBuilder stringBuilder_choice2 = new StringBuilder();
        StringBuilder stringBuilder_choice3 = new StringBuilder();
        StringBuilder stringBuilder_choice4 = new StringBuilder();


        String readIn = "";
        InputStream iss = this.getResources().openRawResource(R.raw.samplequestions);
        BufferedReader reader = new BufferedReader(new InputStreamReader(iss));

        /**
         * THE READ IN PART FROM THE TXT DOCUMENTS
         */

        passageIDcounter = 0;

        if (iss != null) {
            try {
                while ((readIn = reader.readLine()) != null) {

                    /**
                     * find question contents and choice contents
                     */
                    if (readIn.charAt(0) == '6' &&
                            readIn.charAt(1) == '7' &&
                            readIn.charAt(2) == '9' &&
                            readIn.charAt(3) == '3' &&
                            readIn.charAt(4) == '1') {
                        passageIDcounter++;
                        continue;
                    }

                    if (passageIDcounter != passageID) {
                        continue;
                    }

                    /**
                     * Find out if the read-in line is an option or a question
                     * Options must start with forms of (A)(B)(C)(D)
                     * Questions must start with forms of 1. 2. and so on
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

                            /**
                             * push the stringBuilder's value to the choices list and question content list
                             */

                            string_content[s] = stringBuilder_content.toString();
                            string_choice1[s] = stringBuilder_choice1.toString();
                            string_choice2[s] = stringBuilder_choice2.toString();
                            string_choice3[s] = stringBuilder_choice3.toString();
                            string_choice4[s] = stringBuilder_choice4.toString();
                            //questionContent.setText(string_content[s]);

                            /**
                             * Clear the stringBuilders for the new question
                             */
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
     * HELP SET THE QUESTION'S CHECKBOXES
     * @param num QUESTION ID and hh
     */

    public void mySet(int num) {

        /**
         * Find out the four checkboxes by ID
         */

        TextView questionContent = (TextView) findViewById(R.id.questionContent);
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);

        /**
         * Set text the content textView and four options' textview
         */
        questionContent.setText(string_content[num]);
        checkBox1.setText(string_choice1[num]);
        checkBox2.setText(string_choice2[num]);
        checkBox3.setText(string_choice3[num]);
        checkBox4.setText(string_choice4[num]);

        /**
         * Set the checkboxes boolean checked value according to the choices list
         */
        checkBox1.setChecked((Choices[num] & 8) != 0);
        checkBox2.setChecked((Choices[num] & 4) != 0);
        checkBox3.setChecked((Choices[num] & 2) != 0);
        checkBox4.setChecked((Choices[num] & 1) != 0);

    }

    /**
     * GO TO THE NEXT QUESTION
     * @param num question ID
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
     * GO TO THE NEXT QUESTION
     * @param num question ID
     */

    public void goto_nextQuestion(int num) {
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);

        /**
         * When going to the next question,
         * first clear all the checkboxes
         * then set the checkboxes as the list's number
         */
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
