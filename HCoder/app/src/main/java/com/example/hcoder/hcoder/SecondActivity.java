package com.example.hcoder.hcoder;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hcoder.hcoder.huffmanhelper.HuffmanHelper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.net.URLEncoder.encode;


public class SecondActivity extends AppCompatActivity {

    public String user;
    Button encode, decode;
    TextView text;
    EditText enteredText, enteredReceiver;
    String FileName = "text.txt";
    String EncodedFileName = "encoded_text.txt";

    CheckBox checkBoxCompressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle ComingData = getIntent().getExtras();
        if (ComingData == null) {
            return;
        }
        user = ComingData.getString("WhoAmI");

        encode = (Button) findViewById(R.id.encodeButton);
        decode = (Button) findViewById(R.id.decodeButton);
        text = (TextView) findViewById(R.id.textView3);
        enteredText = (EditText) findViewById(R.id.editText3);
        enteredReceiver = (EditText) findViewById(R.id.editText2);

        checkBoxCompressed = (CheckBox) findViewById(R.id.cbxSendCompressed);

        /* -------- GET  -   POST ------------- */

        encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                String encoded_text = Huffman.encode(readFile(FileName));
//                Toast.makeText(getApplicationContext(), encoded_text, Toast.LENGTH_SHORT).show();
                text.setText(encoded_text);
                saveEncodedFile(EncodedFileName, encoded_text);*/

                try{

                    String sender = user;

                    String toBeSentText= enteredText.getText().toString();
                    String toBeSentUser = enteredReceiver.getText().toString();


                    if(checkBoxCompressed.isChecked())
                    {
                        //Encode metodu username_encoded.txt olusturur.
                        String encodedString = HuffmanHelper.encode(toBeSentText, toBeSentUser);
                        String url = String.format("http://161.9.72.71:8080/sendmessagetouser?username=%s&message=%s", toBeSentUser, encodedString);
                        String message = new RetrieveFeedTask().execute(url).get();
                    }

                    else{
                        String url = String.format("http://161.9.72.71:8080/senduncompressedmessagetouser?username=%s&message=%s", toBeSentUser, toBeSentText);
                        String message = new RetrieveFeedTask().execute(url).get();
                    }
                }

                catch (Exception ex){

                    ex.printStackTrace();
                }

            }
        });

        decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{


                    //readencodedmessagefor



                    String sender = user;
                    String toBeSentText= enteredText.getText().toString();
                    String toBeSentUser = enteredReceiver.getText().toString();





                    if(checkBoxCompressed.isChecked())
                    {
                        String encodedString = HuffmanHelper.encode(toBeSentText, toBeSentUser);
                        String url = String.format("http://161.9.72.71:8080/readencodedmessagefor?username=%s", toBeSentUser);
                        String message = new RetrieveFeedTask().execute(url).get();
                    }

                    else{
                        String url = String.format("http://161.9.72.71:8080/readdecodedmessagefor?username=%s", toBeSentUser);
                        String message = new RetrieveFeedTask().execute(url).get();
                    }
                }

                catch (Exception ex){

                    ex.printStackTrace();
                }

            }
        });


    }
}
