package us.mrvivacio.fabulousadventure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Answerspage extends questionslist {

    private TextView yourAnswer;
    private TextView correctAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerspage);
        yourAnswer = (TextView) findViewById(R.id.yourAnswer);
        correctAnswer = (TextView) findViewById(R.id.correctAnswer);

        /**
         * GET THE ANSWERS
         */
        for (int i = 1;i <= s; i++) {
            if (Choices[i] == 0) {
                yourAnswer.append("N/A");
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

        correctAnswer.append("C D A A C B C A B B D");
    }
}
