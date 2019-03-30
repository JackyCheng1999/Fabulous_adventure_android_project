package us.mrvivacio.fabulousadventure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MockExam extends AppCompatActivity {


    private Button Beginexam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_exam);

        Beginexam = (Button) findViewById(R.id.Beginexam);
        Beginexam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Startexam();
            }
        });
    }

    public void Startexam() {
        Intent intent = new Intent(MockExam.this, questionslist.class);
        startActivity(intent);

    }
}
