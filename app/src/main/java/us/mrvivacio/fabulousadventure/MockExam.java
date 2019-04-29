package us.mrvivacio.fabulousadventure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MockExam extends AppCompatActivity {

    public static int passageID, totalPassage;


    private Button Beginexam;
    private SeekBar seekBar;
    private TextView Hardness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_exam);

        totalPassage = 3;

        Beginexam = (Button) findViewById(R.id.Beginexam);
        seekBar = (SeekBar) findViewById(R.id.seekBar2);
        Hardness = (TextView) findViewById(R.id.textView4);
        Beginexam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Startexam();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 33) {
                    passageID = (int) ((Math.random()) * totalPassage / 3);
                    Hardness.setText("Easy");
                } else if (progress <= 67) {
                    passageID = (int) ((Math.random()) * totalPassage / 3 + totalPassage / 3);
                    Hardness.setText("Median");
                } else {
                    passageID = (int) ((Math.random()) * totalPassage / 3 + totalPassage / 3 * 2);
                    Hardness.setText("Hard");
                }
                passageID++;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void Startexam() {
        Intent intent = new Intent(MockExam.this, questionslist.class);
        startActivity(intent);

    }
}
