package com.example.hcoder.hcoder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connectClick(View view){
        Intent SecondPage = new Intent(this, SecondActivity.class);

        EditText username = (EditText)findViewById(R.id.editText);
        String EnteredUsername = username.getText().toString();

        if(EnteredUsername.contentEquals(""))
        {
            Toast error_message = Toast.makeText(getApplicationContext(), "Username cannot be empty !", Toast.LENGTH_SHORT);
            error_message.setGravity(Gravity.CENTER, 0, 0);
            error_message.show();
        }
        else
        {
            SecondPage.putExtra("WhoAmI", EnteredUsername);
            startActivity(SecondPage);
        }

    }
}
