package us.mrvivacio.fabulousadventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mylibrary.Word;

public class Login extends AppCompatActivity {

    EditText e1, e2;

    Button b1;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHelper(this);
        e1 = (EditText) findViewById(R.id.email2);
        e2 = (EditText) findViewById(R.id.password2);
        b1 = (Button) findViewById(R.id.login);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String password = e2.getText().toString();
                boolean chkemailpass = db.emailpassword(email, password);
                if (chkemailpass == true) {
                    Word.username = email;
                    Toast.makeText(getApplicationContext(), "Successfully Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainPage.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
