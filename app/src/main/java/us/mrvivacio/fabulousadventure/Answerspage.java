package us.mrvivacio.fabulousadventure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Answerspage extends questionslist {

    private TextView yourAnswer;
    private TextView correctAnswer;
    private ProgressBar progressBar;
    private TextView rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerspage);
        yourAnswer = (TextView) findViewById(R.id.yourAnswer);
        correctAnswer = (TextView) findViewById(R.id.correctAnswer);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        rating = (TextView) findViewById(R.id.textView);

        /**
         * The following part is to find out the answers the user made in the reading
         */
        for (int i = 1;i <= s; i++) {
            if (Choices[i] == 0) {
                yourAnswer.append("?");
            }
            if ((Choices[i] & 8) != 0) {
                yourAnswer.append("A");
            }
            if ((Choices[i] & 4) != 0) {
                yourAnswer.append("B");
            }
            if ((Choices[i] & 2) != 0) {
                yourAnswer.append("C");
            }
            if ((Choices[i] & 1) != 0) {
                yourAnswer.append("D");
            }
            yourAnswer.append(" ");
        }

        String someMessage = "";
        //String someMessage2 = "";
        InputStream is = this.getResources().openRawResource(R.raw.answers);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(is));
        int passageIDcounter = 0;

        /**
         * find out the correct answer for the reading
         */

        if (is != null) {

            try {
                while ((someMessage = reader2.readLine()) != null) {
                    if (someMessage.charAt(0) == '6' &&
                            someMessage.charAt(1) == '7' &&
                            someMessage.charAt(2) == '9' &&
                            someMessage.charAt(3) == '3' &&
                            someMessage.charAt(4) == '1') {
                        passageIDcounter++;
                        continue;
                    }
                    if (passageIDcounter == passageID) {
                        correctAnswer.append(someMessage);
                        break;
                    }
                }
                is.close();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }

        int j = 1;
        int linshians = 0;
        int correctness = 0;

        /**
         * Comparing the correct answers to the user's answers
         * find out the correctness
         */

        for (int i = 0; i < someMessage.length(); i++) {
            if (someMessage.charAt(i) == ' ') {
                if (linshians == Choices[j]) {
                    correctness++;
                }
                j++;
                linshians = 0;
            }
            if (someMessage.charAt(i) == 'A') {
                linshians = linshians | 8;
            }
            if (someMessage.charAt(i) == 'B') {
                linshians = linshians | 4;
            }
            if (someMessage.charAt(i) == 'C') {
                linshians = linshians | 2;
            }
            if (someMessage.charAt(i) == 'D') {
                linshians = linshians | 1;
            }
        }
        if (linshians == Choices[j]) {
            correctness++;
        }

        /**
         * change the seekbar to show the percentage the user made correct
         */
        progressBar.setProgress(correctness * 100 / s);
        rating.setText("YOU GOT " + correctness + "/" + s);
    }
}
